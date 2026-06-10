package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Orders;
import web.mvc.dto.OrderRequestDTO;
import web.mvc.exception.NotFoundException;
import web.mvc.service.OrderService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> insertOrders(@RequestBody OrderRequestDTO req, Authentication auth) {
        String userId = getLoginUser(auth);
        Orders order = orderService.insertOrders(userId, req.getAddress(), req.getItems());
        return ResponseEntity.ok(Map.of("message", "주문완료", "orderId", order.getOrderId()));
    }

    @GetMapping
    public ResponseEntity<List<Orders>> selectOrdersByUserId(Authentication auth) {
        String userId = getLoginUser(auth);
        return ResponseEntity.ok(orderService.selectOrdersByUserId(userId));
    }

    private String getLoginUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new NotFoundException("로그인이 필요합니다.");
        }
        return auth.getName(); // JwtFilter에서 설정한 userId
    }
}
