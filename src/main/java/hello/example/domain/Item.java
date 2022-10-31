package hello.example.domain;

import hello.example.constant.ItemSellStatus;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Table(name = "item")
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;                        //상품 코드

    @Column(nullable = false, length = 55)
    private String itemName;                //상품 명

    @Column(nullable = false, name = "price")
    private int price;                      //상품 가격

    @Column(nullable = false)
    private int stockNumber;                //재고 수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;              //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  //상품 판매 상태

    private LocalDateTime regTime;          //등록 시간

    private LocalDateTime updateTime;       //수정 시간
}

