package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Goods;
import web.mvc.domain.Orders;
import web.mvc.dto.CartItemRequest;
import web.mvc.dto.CartViewItem;
import web.mvc.exception.NotFoundException;
import web.mvc.service.GoodsService;
import web.mvc.service.OrderService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final GoodsService goodsService;
    private final OrderService orderService;

    // 유저별 장바구니 임시 저장소 (서버 재시작 시 초기화됨)
    private static final Map<String, Map<String, Integer>> cartStore = new HashMap<>();

    /** 장바구니 담기 */ //
    @PostMapping
    public ResponseEntity<?> putCart(@RequestBody CartItemRequest req, Authentication auth) {
        String userId = getLoginUser(auth);
        Goods goods = goodsService.goodsSelectById(req.getGoodsId());
        if (goods.getStock() < req.getQuantity()) {
            return ResponseEntity.badRequest().body(Map.of("error", "재고 부족으로 장바구니에 담을 수 없습니다."));
        }

        Map<String, Integer> cart = cartStore.computeIfAbsent(userId, k -> new HashMap<>());
        cart.merge(req.getGoodsId(), req.getQuantity(), Integer::sum);

        return ResponseEntity.ok(Map.of("message", "장바구니에 담았습니다."));
    }

    /** 장바구니 보기 */
    @GetMapping
    public ResponseEntity<?> viewCart(Authentication auth) {
        String userId = getLoginUser(auth);

        Map<String, Integer> cart = cartStore.getOrDefault(userId, new HashMap<>());
        if (cart.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "장바구니가 비었습니다.", "items", List.of()));
        }

        List<CartViewItem> items = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            Goods goods = goodsService.goodsSelectById(entry.getKey());
            items.add(new CartViewItem(
                    goods.getGoodsId(), goods.getGoodsName(),
                    goods.getGoodsPrice(), entry.getValue(),
                    goods.getGoodsPrice() * entry.getValue()
            ));
        }
        return ResponseEntity.ok(items);
    }

    /** 장바구니에서 주문하기 */
    @PostMapping("/order")
    public ResponseEntity<?> orderFromCart(@RequestBody Map<String, String> body, Authentication auth) {
        String userId = getLoginUser(auth);
        String address = body.get("address");

        Map<String, Integer> cart = cartStore.getOrDefault(userId, new HashMap<>());
        if (cart.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "장바구니가 비었습니다."));
        }

        Orders order = orderService.insertOrdersFromCart(userId, address, cart);
        cartStore.remove(userId);
        return ResponseEntity.ok(Map.of("message", "주문 완료", "orderId", order.getOrderId()));
    }

    private String getLoginUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new NotFoundException("로그인이 필요합니다.");
        }
        return auth.getName();
    }
}