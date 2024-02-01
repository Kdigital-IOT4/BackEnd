package com.baro.domain.order.repository.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderStoreDataDTO {
    private String orderCode;
    private String machine_id;
    private String user_phoneNumber;
    private int total_price;
    private LocalDateTime createOrderTime;
    private List<OrderStoreDataRecipeDTO> recipeList;
}
