package web.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import web.mvc.controller.GoodsController;
import web.mvc.domain.Goods;
import web.mvc.service.GoodsService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GoodsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GoodsService goodsService;

    @Test
    @DisplayName("GET /api/goods - 상품 목록 페이징 조회")
    void getAllGoods() throws Exception {
        List<Goods> goodsList = List.of(
                new Goods("G001", "노트북", 1500000, 10, "2026-01-01")
        );
        Page<Goods> page = new PageImpl<>(goodsList, PageRequest.of(0, 10), 1);
        given(goodsService.goodsSelect(any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/api/goods"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].goodsId").value("G001"))
                .andExpect(jsonPath("$.content[0].goodsName").value("노트북"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("GET /api/goods/{goodsId} - 상품 단건 조회")
    void getGoods() throws Exception {
        Goods goods = new Goods("G001", "노트북", 1500000, 10, "2026-01-01");
        given(goodsService.goodsSelectById("G001")).willReturn(goods);

        mockMvc.perform(get("/api/goods/G001"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goodsId").value("G001"))
                .andExpect(jsonPath("$.goodsPrice").value(1500000));
    }
}
