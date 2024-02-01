package com.baro.domain.order.repository.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderStoreDataRecipeBaseDTO {
    private String base_en_name;
    private String base_kr_name;
    private int amount;

}
