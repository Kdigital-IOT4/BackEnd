package com.baro.domain.order.service;

import com.baro.domain.order.domain.CocktailQueue;
import com.baro.domain.order.domain.CocktailQueueChecker;
import com.baro.domain.order.repository.JPAMongoCocktailQueueCheckerRepository;
import com.baro.domain.order.repository.JPAMongoCocktailQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CocktailQueueService {
    private final JPAMongoCocktailQueueRepository cocktailQueueRepository;
    private final JPAMongoCocktailQueueCheckerRepository cocktailQueueCheckerRepository;
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
            cocktailQueue.setCreateOrderTime(LocalDateTime.now());
        } else {
            //존재하지않음 -> first order
            log.info("{} 첫주문 발생",machine_id);
            cocktailQueue.setMachineId(machine_id);
            cocktailQueue.setOrderCode(orderCode);
            cocktailQueue.setWaitingLine(1);
            cocktailQueue.setCreateOrderTime(LocalDateTime.now());

            //first order 발생시 Checker 발행
            String return_text = register_cocktailQueue_checker(machine_id);

            if(return_text.equals("fail")){
                return "error";
            }
        }

        try {
            cocktailQueueRepository.save(cocktailQueue);
            return "success";
        }catch (Exception e){
            log.warn("큐 업로드중 문제발생 사유 : \n{}",e);
            return "error";
        }


    }

    private String register_cocktailQueue_checker(String machine_id){
        log.info("머신 당 첫 주문 발생 -> checker 발행");

        CocktailQueueChecker cocktailQueueChecker = new CocktailQueueChecker();
        cocktailQueueChecker.setMachineId(machine_id);
        cocktailQueueChecker.setWaitingLineChecker(1);
        cocktailQueueChecker.setCreateOrderTime(LocalDateTime.now());
        cocktailQueueChecker.setLastUpdateTime(LocalDateTime.now());
        try {
            cocktailQueueCheckerRepository.save(cocktailQueueChecker);
            log.info("cocktail queue checker 등록을 성공하였습니다.");
            return "success";
        }catch (Exception e){
            log.warn("mongodb access error -> check 등록을 실패");
            log.warn("사유 : {}" ,e);
            return "fail";
        }


    }

}
