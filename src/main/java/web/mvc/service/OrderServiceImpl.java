package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Goods;
import web.mvc.domain.OrderLine;
import web.mvc.domain.Orders;
import web.mvc.dto.OrderLineRequest;
import web.mvc.exception.NotFoundException;
import web.mvc.repository.GoodsRepository;
import web.mvc.repository.OrdersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;
    private final GoodsRepository goodsRepository;

    @Override
    @Transactional
    public Orders insertOrders(String userId, String address, List<OrderLineRequest> items) {
        Orders order = new Orders();
        order.setUserId(userId);
        order.setAddress(address);

        int totalAmount = 0;
        for (OrderLineRequest item : items) {
            Goods goods = goodsRepository.findById(item.getGoodsId())
                    .orElseThrow(() -> new NotFoundException("상품 없음: " + item.getGoodsId()));
            if (goods.getStock() < item.getQty()) {
                throw new IllegalArgumentException("재고 부족: " + goods.getGoodsName());
            }

            OrderLine line = new OrderLine();
            line.setOrder(order);
            line.setGoodsId(item.getGoodsId());
            line.setUnitPrice(goods.getGoodsPrice());
            line.setQty(item.getQty());
            line.setAmount(goods.getGoodsPrice() * item.getQty());
            order.getOrderLineList().add(line);

            totalAmount += line.getAmount();
            goods.setStock(goods.getStock() - item.getQty());
        }

        order.setTotalAmount(totalAmount);
        return ordersRepository.save(order);
    }

    @Override
    @Transactional
    public Orders insertOrdersFromCart(String userId, String address, Map<String, Integer> cart) {
        List<OrderLineRequest> items = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            OrderLineRequest req = new OrderLineRequest();
            req.setGoodsId(entry.getKey());
            req.setQty(entry.getValue());
            items.add(req);
        }
        return insertOrders(userId, address, items);
    }

    @Override
    public List<Orders> selectOrdersByUserId(String userId) {
        List<Orders> list = ordersRepository.findByUserIdOrderByOrderIdDesc(userId);
        if (list.isEmpty()) throw new NotFoundException(userId + "의 주문내역이 없습니다.");
        return list;
    }
}
