package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.Goods;

public interface GoodsRepository extends JpaRepository<Goods, String> {
    @Modifying
    @Query("UPDATE Goods g SET g.stock = g.stock - :qty WHERE g.goodsId = :goodsId")
    void decreaseStock(@Param("goodsId") String goodsId, @Param("qty") int qty);
}
