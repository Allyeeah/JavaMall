package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Goods;
import web.mvc.service.GoodsService;

@RestController
@RequestMapping("/api/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

    // GET /api/goods?page=0&size=10&sort=goodsPrice,asc
    @GetMapping
    public ResponseEntity<Page<Goods>> getAllGoods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "goodsId") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(goodsService.goodsSelect(pageable));
    }

    @GetMapping("/{goodsId}")
    public ResponseEntity<Goods> getGoods(@PathVariable String goodsId) {
        return ResponseEntity.ok(goodsService.goodsSelectById(goodsId));
    }
}
