package com.baro.domain.order.repository.DAO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderCocktailDAO {
    private String phoneNumber;
    private String machineId;
    private List<OrderCocktailDetailDAO> cocktailList;


}
