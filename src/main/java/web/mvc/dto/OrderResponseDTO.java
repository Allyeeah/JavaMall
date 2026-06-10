package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import web.mvc.domain.OrderLine;
import web.mvc.domain.Orders;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private int orderId;
    private String orderDate;
    private String userId;
    private String address;
    private int totalAmount;
    private List<OrderLineDTO> orderLines;

    public static OrderResponseDTO from(Orders orders) {
        List<OrderLineDTO> lines = orders.getOrderLineList().stream()
                .map(line -> new OrderLineDTO(
                        line.getGoodsId(),
                        line.getUnitPrice(),
                        line.getQty(),
                        line.getAmount()
                ))
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                orders.getOrderId(),
                orders.getOrderDate(),
                orders.getUserId(),
                orders.getAddress(),
                orders.getTotalAmount(),
                lines
        );
    }
}
