package com.baro.domain.order.repository;

import com.baro.domain.order.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface JPAMongoOrderRepository extends MongoRepository<Order , String> {

    boolean existsByOrderCode (String orderCode);
    Optional<Order> findByOrderCode(String orderCode);

}
