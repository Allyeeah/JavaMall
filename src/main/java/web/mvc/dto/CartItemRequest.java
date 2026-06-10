package web.mvc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    private String goodsId;
    private int quantity;
}
