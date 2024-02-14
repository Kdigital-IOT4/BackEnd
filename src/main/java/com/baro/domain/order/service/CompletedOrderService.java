package com.baro.domain.order.service;

import com.baro.domain.order.domain.CocktailQueue;
import com.baro.domain.order.domain.CocktailQueueChecker;
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
public class CompletedOrderService {

    private final JPAMongoOrderRepository mongoOrderRepository;
    private final JPAMongoCocktailQueueCheckerRepository mongoCocktailQueueCheckerRepository;
    private final JPAMongoCocktailQueueRepository mongoCocktailQueueRepository;
    private final OrderService orderService;
    public String complete_cocktail_changeStatues_service(String orderCode){
       Optional<Order> Optional_orderData = mongoOrderRepository.findByOrderCode(orderCode);
       OrderStatus orderStatus =  Optional_orderData.get().getStatus();

       if(! orderStatus.equals(OrderStatus.IN_PROGRESS)){
           //비정상적인 요청 -> 프로그래스 상태가 아님
           log.info("비정상적인 오더 변경 요청 확인");
           return "fail";
       }

       if(Optional_orderData .isPresent()){
           Order order = Optional_orderData.get();
           order.setStatus(OrderStatus.COMPLETED);
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

    public Integer complete_cocktail_changeQueueChecker_service(String orderCode){
        //find machine_id;
        Optional<Order> optional_orderData = orderService.order_data_find_service(orderCode);
        String machine_id;

        if(! optional_orderData.isPresent()){
            //못찾은경우
            log.warn("mongo access fail");
            return null;
        }

        machine_id = optional_orderData.get().getMachineId();
        Optional<CocktailQueueChecker> optional_cocktailQueueChecker = mongoCocktailQueueCheckerRepository.findByMachineId(machine_id);
        if(! optional_cocktailQueueChecker.isPresent()){
            //못찾은경우
            log.warn("mongo access fail");
            return null;
        }

        int cocktail_line = (optional_cocktailQueueChecker.get().getWaitingLineChecker())+1;
        CocktailQueueChecker cocktailQueueChecker = optional_cocktailQueueChecker.get();
        cocktailQueueChecker.setWaitingLineChecker(cocktail_line);

        try {
            log.info("cocktail Queue checker update 를 시작합니다.");
            mongoCocktailQueueCheckerRepository.save(cocktailQueueChecker);
            log.info("cocktail Queue checker update 를 완료했습니다.");
            return cocktail_line;
        }catch (Exception e){
            log.warn("cocktail Queue checker update 에 문제가 발생하였습니다.");
            log.warn("사유 : {}", e);
            return null;
        }

    }

    public String find_next_orderCode_service(String machine_id , int lineNumber){
        //머신 아이디와 라인넘버를 통해 해당하는 orderCode를 주는 서비스
        if(mongoCocktailQueueRepository.existsByMachineIdAndWaitingLine(machine_id , lineNumber)){
            Optional<CocktailQueue> optional_cocktailQueue = mongoCocktailQueueRepository.findByMachineIdAndWaitingLine(machine_id, lineNumber);

            if(optional_cocktailQueue.isEmpty()){
                // cocktail queue 에서 데이터를 찾지 못함
                return "error";
            }

            String orderCode = optional_cocktailQueue.get().getOrderCode();

            Optional<Order> optional_orderData = orderService.order_data_find_service(orderCode); //왜 찾냐면 정상적인 데이터인지 check 한번 하고 보내려고

            if(optional_orderData.isPresent()){
                log.info("order data find success");
                return orderCode;
            }else{
                return "error";
            }
        }else{
            // 다음 주문이 없습니다.
            log.info("next order 가 존재하지 않습니다.");
            return "not exists next order";
        }


    }

}
