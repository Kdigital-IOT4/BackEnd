package com.baro.domain.order.service;

import com.baro.domain.order.domain.CocktailQueue;
import com.baro.domain.order.repository.JPAMongoCocktailQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CocktailQueueService {
    private final JPAMongoCocktailQueueRepository cocktailQueueRepository;
    public String register_cocktailQueue_service(String machine_id , String orderCode){
        log.info("{} 에 대한 queue 업로드를 시작합니다." , machine_id);
        CocktailQueue cocktailQueue = new CocktailQueue();

        /**
         * 없다. 첫주문 -> 1번
         * 있다. lastOrder -> ++
         */
        Optional<CocktailQueue> cocktailQueueOptional = cocktailQueueRepository.findByMachineId(machine_id);

        if (cocktailQueueOptional.isPresent()) {
            //존재함
            Optional<CocktailQueue> findQueueData = cocktailQueueRepository.findTopByMachineIdOrderByWaitingLineDesc(machine_id);
            int lineNumber = findQueueData.get().getWaitingLine();
            log.info("{}",lineNumber);

            lineNumber++;
            cocktailQueue.setMachineId(machine_id);
            cocktailQueue.setOrderCode(orderCode);
            cocktailQueue.setWaitingLine(lineNumber);
        } else {
            //존재하지않음 -> first order
            log.info("{} 첫주문 발생",machine_id);
            cocktailQueue.setMachineId(machine_id);
            cocktailQueue.setOrderCode(orderCode);
            cocktailQueue.setWaitingLine(1);
        }
        try {
            cocktailQueueRepository.save(cocktailQueue);
            return "success";
        }catch (Exception e){
            log.warn("큐 업로드중 문제발생 사유 : \n{}",e);
            return "error";
        }


    }

}
