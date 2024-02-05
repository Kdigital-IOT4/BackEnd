package com.baro.domain.order.repository;

import com.baro.domain.order.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JPAMongoOrderRepository extends MongoRepository<Order , String> {


}
