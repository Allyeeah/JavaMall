package web.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import web.mvc.domain.Goods;
import web.mvc.exception.NotFoundException;
import web.mvc.repository.GoodsRepository;
import web.mvc.service.GoodsServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GoodsServiceImplTest {

    @Mock
    private GoodsRepository goodsRepository;

    @InjectMocks
    private GoodsServiceImpl goodsService;

    @Test
    @DisplayName("상품 목록 페이징 조회 성공")
    void goodsSelect_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<Goods> goodsList = List.of(
                new Goods("G001", "노트북", 1500000, 10, "2026-01-01"),
                new Goods("G002", "마우스", 30000, 50, "2026-01-01")
        );
        Page<Goods> page = new PageImpl<>(goodsList, pageable, 2);
        given(goodsRepository.findAll(pageable)).willReturn(page);

        // when
        Page<Goods> result = goodsService.goodsSelect(pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getGoodsId()).isEqualTo("G001");
    }

    @Test
    @DisplayName("상품 목록 조회 실패 - 상품 없음")
    void goodsSelect_empty() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Goods> emptyPage = Page.empty(pageable);
        given(goodsRepository.findAll(pageable)).willReturn(emptyPage);

        // when & then
        assertThatThrownBy(() -> goodsService.goodsSelect(pageable))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("상품이 없습니다");
    }

    @Test
    @DisplayName("상품 단건 조회 성공")
    void goodsSelectById_success() {
        // given
        Goods goods = new Goods("G001", "노트북", 1500000, 10, "2026-01-01");
        given(goodsRepository.findById("G001")).willReturn(Optional.of(goods));

        // when
        Goods result = goodsService.goodsSelectById("G001");

        // then
        assertThat(result.getGoodsName()).isEqualTo("노트북");
        assertThat(result.getGoodsPrice()).isEqualTo(1500000);
    }

    @Test
    @DisplayName("상품 단건 조회 실패 - 존재하지 않는 상품")
    void goodsSelectById_notFound() {
        // given
        given(goodsRepository.findById("NONE")).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> goodsService.goodsSelectById("NONE"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("상품이 없습니다");
    }
}
