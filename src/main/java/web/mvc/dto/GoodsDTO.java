package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import web.mvc.domain.Goods;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDTO {
    private String goodsId;
    private String goodsName;
    private int goodsPrice;
    private int stock;

    public static GoodsDTO from(Goods goods) {
        return new GoodsDTO(
                goods.getGoodsId(),
                goods.getGoodsName(),
                goods.getGoodsPrice(),
                goods.getStock()
        );
    }
}
