package com.baro.domain.order.domain;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "cocktailQueueChecker")
@Getter
@Setter
@Data
public class CocktailQueueChecker {
    @Id
    private String machineId;
    private int waitingLineChecker;
    private LocalDateTime createOrderTime;
    private LocalDateTime lastUpdateTime;
}
