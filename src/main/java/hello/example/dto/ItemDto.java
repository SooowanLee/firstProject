package hello.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemDto {

    private Long id;
    private String itemName;
    private Integer price;
    private String itemDetail;
    private String sellStaCd;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
