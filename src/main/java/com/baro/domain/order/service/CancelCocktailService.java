package com.baro.domain.order.service;

import com.baro.domain.order.domain.Order;
import com.baro.domain.order.repository.JPAMongoCocktailQueueCheckerRepository;
import com.baro.domain.order.repository.JPAMongoCocktailQueueRepository;
import com.baro.domain.order.repository.JPAMongoOrderRepository;
import com.baro.domain.order.repository.enumeration.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CancelCocktailService {

    private final JPAMongoOrderRepository mongoOrderRepository;
    private final JPAMongoCocktailQueueCheckerRepository mongoCocktailQueueCheckerRepository;
    private final JPAMongoCocktailQueueRepository mongoCocktailQueueRepository;
    private final OrderService orderService;
    public String cancel_cocktail_changeStatues_service(String orderCode){
        Optional<Order> Optional_orderData = mongoOrderRepository.findByOrderCode(orderCode);
        OrderStatus orderStatus =  Optional_orderData.get().getStatus();

        if(orderStatus.equals(OrderStatus.CANCELED)){
            //비정상적인 요청 -> 이미 취소된 요청
            log.info("비정상적인 오더 변경 요청 -> 이미 취소됨");
            return "fail";
        }

        if(Optional_orderData .isPresent()){
            Order order = Optional_orderData.get();
            order.setStatus(OrderStatus.CANCELED);
            try {
                mongoOrderRepository.save(order);
                return "success";
            }catch (Exception e){
                log.info("mongo db access error");
                return "error";
            }

        }else{
            log.info("mongo db access error");
            return "error";
        }


    }
}
