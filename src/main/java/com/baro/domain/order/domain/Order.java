package com.baro.domain.order.domain;

import com.baro.domain.order.repository.DTO.OrderStoreDataRecipeDTO;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Getter
@Setter
@Data
public class Order {

    @Id
    private String orderCode;
    private String machine_id;
    private String user_phoneNumber;
    private int total_price;
    private LocalDateTime createOrderTime;
    private List<OrderStoreDataRecipeDTO> recipeList;
}
