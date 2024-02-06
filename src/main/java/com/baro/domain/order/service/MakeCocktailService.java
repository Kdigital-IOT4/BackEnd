package com.baro.domain.order.service;

import com.baro.domain.cocktail.repository.DAO.BaseMachineReadDAO;
import com.baro.domain.cocktail.repository.DAO.RecipeDAO;
import com.baro.domain.cocktail.service.CocktailService;
import com.baro.domain.cocktail.service.RecipeService;
import com.baro.domain.order.domain.Order;
import com.baro.domain.order.repository.DTO.OrderStoreDataRecipeDTO;
import com.baro.domain.order.repository.JPAMongoOrderRepository;
import com.baro.domain.order.repository.enumeration.OrderStatus;
import com.baro.domain.order.util.GcodeMoveCoordinateData;
import com.baro.domain.order.util.GcodeMoveSpeedData;
import com.baro.domain.order.util.GenerateOrderCodeUtil;
import com.baro.domain.user.repository.DAO.MachineBaseReadDAO;
import com.baro.domain.user.service.MachineBaseService;
import com.baro.domain.user.service.MachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MakeCocktailService {

    private final JPAMongoOrderRepository mongoOrderRepository;
    private final MachineBaseService machineBaseService;

    public boolean order_make_cocktail_inProgress(String orderCode) {
        // 주문 코드로 주문을 찾기
        Optional<Order> optionalOrder = mongoOrderRepository.findByOrderCode(orderCode);

        if (optionalOrder.isPresent()) {
            // Optional에서 Order 객체를 얻어와서 주문 상태를 업데이트
            Order order = optionalOrder.get();
            order.setStatus(OrderStatus.IN_PROGRESS);

            // MongoDB에 업데이트된 주문 저장
            mongoOrderRepository.save(order);

            return true; // 업데이트 성공
        } else {
            return false; // 주문이 없거나 업데이트 실패
        }

    }
    public StringBuilder order_make_cocktail_service(Order order , String speed){
        /**
         * 1. machine 에 대한 base 찾기 -> 순서도 받음
         * 2. make Gcode
         */
        String orderCode = order.getOrderCode();
        log.info("{} 에 대한 Gcode 생성을 시작합니다.",orderCode);
        List<Integer> baseLineList = order_machine_base_find(order);

        StringBuilder gcode = machine_GcodeMaker(baseLineList , speed);

        // G-code 로그 출력
        log.info("Generated G-code for {}: {}", orderCode, gcode);

        // gcode를 반환하거나 다른 작업 수행 (여기서는 반환만 하도록 함)
        return gcode;
    }

    private List<Integer> order_machine_base_find(Order order) {
        List<Integer> baseLineList = new ArrayList<>();

        String machineId = order.getMachineId();
        MachineBaseReadDAO machineBase = machineBaseService.read_machine_base_service(machineId);
        List<BaseMachineReadDAO> machineBaseList = machineBase.getBaseList();

        List<OrderStoreDataRecipeDTO> rootRecipeList = order.getRecipeList();
        for (OrderStoreDataRecipeDTO item : rootRecipeList) {
            List<RecipeDAO> recipeList = item.getBaseList();
            for (RecipeDAO detailItem : recipeList) {
                Long orderDataBaseSeq = detailItem.getBase_seq();

                boolean baseFound = false;
                for (BaseMachineReadDAO machineItem : machineBaseList) {
                    Long machineDataBaseSeq = machineItem.getBase_seq();

                    if (machineDataBaseSeq.equals(orderDataBaseSeq)) {
                        log.info("Found base!");
                        int baseLine = machineItem.getBase_line();
                        baseLineList.add(baseLine);
                        baseFound = true;
                        break;  // 찾았으면 내부 루프를 종료
                    }
                }

                if (!baseFound) {
                    // 해당하는 base를 찾지 못했을 때 예외를 던짐
                    throw new NoSuchElementException("Base not found for order_data_baseSeq: " + orderDataBaseSeq);
                }
            }
        }

        // 나머지 로직 작성하기 ... ?
        return  baseLineList;
    }

    private StringBuilder machine_GcodeMaker(List<Integer> baseLineList , String speed){
        StringBuilder gcodeBuilder = new StringBuilder();

        for (Integer baseLine : baseLineList) {
            GcodeMoveCoordinateData coordinateData = getCoordinateDataByBaseLine(baseLine);
            GcodeMoveSpeedData speedData = getMoveSpeedByMachine(speed);
            gcodeBuilder.append(String.format("$J=G53X%dY%dZ%dF%d\n", coordinateData.getX(), coordinateData.getY(), coordinateData.getZ(),speedData.getF()));
            log.info("success make Gcode");
        }

        return gcodeBuilder;
    }

    private GcodeMoveCoordinateData getCoordinateDataByBaseLine(int baseLine) {
        switch (baseLine) {
            case 1:
                return GcodeMoveCoordinateData.FIRST;
            case 2:
                return GcodeMoveCoordinateData.SECOND;
            case 3:
                return GcodeMoveCoordinateData.THIRD;
            case 4:
                return GcodeMoveCoordinateData.FORE;
            default:
                throw new IllegalArgumentException("Invalid baseLine: " + baseLine);
        }
    }

    private GcodeMoveSpeedData getMoveSpeedByMachine(String speed){
        if (speed == null) {
            return GcodeMoveSpeedData.MEDIUM; // 기본값으로 "medium" 설정
        }

        switch (speed){
            case "slow":
                return GcodeMoveSpeedData.SLOW;
            case "medium":
                return GcodeMoveSpeedData.MEDIUM;
            case "fast":
                return GcodeMoveSpeedData.FAST;
            default:
                throw new IllegalArgumentException("Invalid speed: " + speed);

        }
    }

}
