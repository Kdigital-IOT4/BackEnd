package com.baro.domain.order.repository.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderCocktailDTO {
    private String phoneNumber;
    private String machineId;
    private List<OrderCocktailDetailDTO> cocktailList;


}
