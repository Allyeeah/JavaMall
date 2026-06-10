package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartViewItem {
    private String goodsId;
    private String goodsName;
    private int goodsPrice;
    private int quantity;
    private int totalPrice;
}
