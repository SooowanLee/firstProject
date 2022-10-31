package hello.example.domain;

import hello.example.constant.ItemSellStatus;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@ToString
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;                        //상품 코드
    private String itemName;                //상품 명
    private int price;                      //상품 가격
    private int stockNumber;                //재고 수량
    private String itemDetail;              //상품 상세 설명
    private ItemSellStatus itemSellStatus;  //상품 판매 상태
    private LocalDateTime regTime;          //등록 시간
    private LocalDateTime updateTime;       //수정 시간
}

