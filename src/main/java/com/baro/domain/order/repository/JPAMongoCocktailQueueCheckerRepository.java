package com.baro.domain.order.repository;

import com.baro.domain.order.domain.CocktailQueueChecker;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface JPAMongoCocktailQueueCheckerRepository extends MongoRepository<CocktailQueueChecker, String> {
    Optional<CocktailQueueChecker> findByMachineId(String machine_id);
}
