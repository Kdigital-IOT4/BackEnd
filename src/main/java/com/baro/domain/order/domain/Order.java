package com.baro.domain.order.domain;

import com.baro.domain.order.repository.DTO.OrderStoreDataRecipeDTO;
import com.baro.domain.order.repository.enumeration.OrderStatus;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Getter
@Setter
@Data
public class Order {
    @MongoId
    private String orderCode;

    private String machineId;
    private String userPhoneNumber;
    private OrderStatus status;
    private int totalPrice;
    private LocalDateTime createOrderTime;
    private List<OrderStoreDataRecipeDTO> recipeList;
}
