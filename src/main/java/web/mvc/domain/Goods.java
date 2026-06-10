package web.mvc.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    @Id
    private String goodsId;
    private String goodsName;
    private int goodsPrice;
    private int stock;
    private String regDate;
}
