package com.baro.domain.order.repository.DTO;

import com.baro.domain.cocktail.repository.DAO.RecipeDAO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderStoreDataRecipeDTO {
    private String cocktail_en_name;
    private int cocktail_price;
    private List<RecipeDAO> baseList;

}
