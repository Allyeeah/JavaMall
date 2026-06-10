package web.mvc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderLineRequest {
    private String goodsId;
    private int qty;
}
