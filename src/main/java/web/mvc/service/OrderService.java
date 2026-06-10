package web.mvc.service;

import web.mvc.domain.Orders;
import web.mvc.dto.OrderLineRequest;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Orders insertOrders(String userId, String address, List<OrderLineRequest> items);
    Orders insertOrdersFromCart(String userId, String address, Map<String, Integer> items);
    List<Orders> selectOrdersByUserId(String userId);
}
