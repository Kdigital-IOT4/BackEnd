package com.baro.domain.order.domain;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Document(collection = "cocktailQueue")
@Getter
@Setter
@Data
public class CocktailQueue {
    @MongoId
    private String machineId;
    private String orderCode;
    private int waitingLine;
    private LocalDateTime createOrderTime;
}
