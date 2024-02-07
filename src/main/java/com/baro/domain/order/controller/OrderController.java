package com.baro.domain.order.controller;

import com.baro.domain.order.domain.Order;
import com.baro.domain.order.repository.DTO.OrderCocktailDTO;
import com.baro.domain.order.repository.DTO.OrderStoreDataDTO;
import com.baro.domain.order.service.CompletedCocktailService;
import com.baro.domain.order.service.MakeCocktailService;
import com.baro.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {"Authorization", "Content-Type"})
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MakeCocktailService makeCocktailService;
    private final CompletedCocktailService completedcocktailService;
    @PostMapping("/orderCocktail")
    public ResponseEntity order_cocktail_controller(@RequestBody OrderCocktailDTO orderCocktailDTO){
        log.info("order_cocktail_start");

        OrderStoreDataDTO orderData = orderService.order_cocktail_service(orderCocktailDTO);
        Map<String, Object> response = new HashMap<>();

        if(orderData != null){
            response.put("status", "success");
            response.put("message", "Order placed successfully");
            response.put("data", orderData);

            return ResponseEntity.ok(response);
        }
        else{
            response.put("status", "error");
            response.put("message", "Failed to place order. Please check your input.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @GetMapping("/makeCocktail/{orderCode}/{speed}")
    public ResponseEntity make_cocktail_controller(@PathVariable String orderCode ,  @PathVariable String speed) {
        log.info("칵테일 제조를 위한 Data 전송을 시작합니다. order -> {}", orderCode);
        Map<String, Object> response = new HashMap<>();
        /**
         * 처리사항
         * order Code check x
         * machine setting check x
         * order 레시피 check x
         * make Gcode x
         * if -> next customer -> check message
         * else -> order Completed
         */

        //order code check
        if (!orderService.orderCode_check_service(orderCode)) {
            //order checking 문제발생
            response.put("status", "error");
            response.put("message", "Failed to place order. Please check your input.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }


        // order data find
        Optional<Order> orderOptional = orderService.order_data_find_service(orderCode);

        if (orderOptional.isPresent()) {
            // order 데이터가 존재할 경우
            Order order = orderOptional.get();
            StringBuilder gcodeBuilder = makeCocktailService.order_make_cocktail_service(order, speed);

            // StringBuilder를 String으로 변환
            String gcode = gcodeBuilder.toString();

            // order status -> waiting -> in_progress
            int maxRetries = 3;
            int currentRetry = 0;

            while (currentRetry < maxRetries) {
                // 주문 상태 업데이트 시도
                if (!makeCocktailService.order_make_cocktail_inProgress(orderCode)) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    currentRetry++;
                } else {
                    // 성공한 경우 반복문 탈출
                    break;
                }
            }

            // 최대 재시도 횟수를 초과하여도 성공하지 못한 경우에 대한 처리
            if (currentRetry == maxRetries) {
                log.info("재시도 횟수를 초과하여 주문 상태 업데이트에 실패했습니다.");

                response.put("status", "error");
                response.put("message", "Failed to Server Status update -> please connect to developer");

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            /**
             * message send logic 추가하기
             */
            String userPhone = order.getUserPhoneNumber();

            // 처리 완료 후 응답
            response.put("orderCode" , orderCode);
            response.put("status", "success");
            response.put("message", "Order placed successfully.");
            response.put("gCode", gcode); // gcode를 JSON 응답

        } else {
            // order 데이터가 없는 경우
            response.put("status", "error");
            response.put("message", "Order data not found for orderCode: " + orderCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(response);

    }

    //order completed
    @GetMapping("/completedCocktail/{orderCode}")
    public ResponseEntity<Map<String, Object>> completeCocktailController(@PathVariable String orderCode) {
        Map<String, Object> response = new HashMap<>();
        log.info("완료된 칵테일 주문을 처리합니다.");
        try {
            String statusChangeResult = completedcocktailService.complete_cocktail_changeStatues_service(orderCode);
            log.info("statues 변경 : {}" , statusChangeResult);
            if ("success".equals(statusChangeResult)) {
                Integer lineNumber = completedcocktailService.complete_cocktail_changeQueueChecker_service(orderCode);

                if (lineNumber == null) {
                    // 서버 오류 발생
                    log.warn("Change Queue Checker Error");
                    return buildErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update server status.");
                }

                String machineId = orderService.order_data_find_service(orderCode).get().getMachineId();
                String nextOrderCode = completedcocktailService.find_next_orderCode_service(machineId, lineNumber);

                if ("fail".equals(nextOrderCode)) {
                    // 서버 오류 발생
                    log.warn("Next Order Code Find Error");
                    return buildErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to find next order.");
                } else if ("not exists next order".equals(nextOrderCode)) {
                    // 마지막 주문까지 완료
                    return buildSuccessResponse(response, orderCode, "Last order completed.", null);
                }

                // 다음 주문이 있는 경우
                return buildSuccessResponse(response, orderCode, "Order placed successfully.", nextOrderCode);
            } else {
                // 주문 에러 발생
                log.warn("Error occurred for cocktail order. OrderCode: {}", orderCode);
                String errorMessage = "Failed to place order.";

                if ("fail".equals(statusChangeResult)) {
                    errorMessage = "Order checking problem occurred.";
                    return buildErrorResponse(response, HttpStatus.BAD_REQUEST, errorMessage);
                }

                // 서버 오류 발생
                return buildErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
            }
        } catch (Exception e) {
            // 예외 처리
            log.error("Exception occurred: {}", e.getMessage(), e);
            return buildErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
        }
    }

    private ResponseEntity<Map<String, Object>> buildSuccessResponse(Map<String, Object> response, String orderCode,
                                                                     String message, String nextOrderCode) {
        response.put("orderCode", orderCode);
        response.put("status", "success");
        response.put("message", message);

        if (nextOrderCode != null) {
            response.put("nextOrderCode", nextOrderCode);
        }

        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(Map<String, Object> response, HttpStatus status,
                                                                   String errorMessage) {
        response.put("status", "error");
        response.put("message", errorMessage);
        return ResponseEntity.status(status).body(response);
    }
}
