package com.baro.domain.order.repository;

import com.baro.domain.order.domain.Order;
import com.baro.domain.order.repository.enumeration.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface JPAMongoOrderRepository extends MongoRepository<Order , String> {

    boolean existsByOrderCode (String orderCode);
    Optional<Order> findByOrderCodeAndStatus(String orderCode , OrderStatus statues);
    Optional<Order> findByOrderCode (String orderCode);

    List<Order> findByMachineIdAndStatus(String machineId , OrderStatus status);
}
