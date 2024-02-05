package com.baro.domain.order.controller;

import com.baro.domain.order.repository.DTO.OrderCocktailDTO;
import com.baro.domain.order.repository.DTO.OrderStoreDataDTO;
import com.baro.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {"Authorization", "Content-Type"})
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @PostMapping("/orderCocktail")
    public ResponseEntity order_cocktail_service(@RequestBody OrderCocktailDTO orderCocktailDTO){
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

    @GetMapping("/makeCocktail/{orderCode}")
    public ResponseEntity make_cocktail_service(@PathVariable String orderCode){
        log.info("칵테일 제조를 위한 Data 전송을 시작합니다. order -> {}",orderCode);
        Map<String, Object> response = new HashMap<>();
        /**
         * 처리사항
         * order Code check
         * machine setting check
         * order 레시피 check
         * make Gcode
         * if -> next customer -> check message
         * else -> order Completed
         */

        //order code check
        if(! orderService.orderCode_check_service(orderCode)){
            //order checking 문제발생
            response.put("status", "error");
            response.put("message", "Failed to place order. Please check your input.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }




        return null;
    }
}
