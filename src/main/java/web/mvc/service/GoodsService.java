package web.mvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.mvc.domain.Goods;

public interface GoodsService {
    Page<Goods> goodsSelect(Pageable pageable);
    Goods goodsSelectById(String goodsId);
}
