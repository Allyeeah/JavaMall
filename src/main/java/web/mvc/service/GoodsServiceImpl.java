package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import web.mvc.domain.Goods;
import web.mvc.exception.NotFoundException;
import web.mvc.repository.GoodsRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsServiceImpl implements GoodsService {
    private final GoodsRepository goodsRepository;

    @Override
    public Page<Goods> goodsSelect(Pageable pageable) {
        Page<Goods> page = goodsRepository.findAll(pageable);
        if (page.isEmpty()) throw new NotFoundException("현재 상품이 없습니다");
        return page;
    }

    @Override
    public Goods goodsSelectById(String goodsId) {
        return goodsRepository.findById(goodsId)
                .orElseThrow(() -> new NotFoundException(goodsId + " 상품이 없습니다."));
    }
}
