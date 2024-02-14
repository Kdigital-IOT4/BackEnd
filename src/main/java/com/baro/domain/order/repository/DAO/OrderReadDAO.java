package com.baro.domain.order.repository.DAO;

import com.baro.domain.order.repository.DTO.OrderStoreDataRecipeDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderReadDAO {
    private String orderCode;
    private String userPhoneNumber;
    private List<OrderStoreDataRecipeDTO> recipeList;
    private LocalDateTime createOrderTime;
}
