package com.baro.domain.order.controller;

import com.baro.domain.order.repository.DAO.OrderCocktailDAO;
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
    public ResponseEntity order_cocktail_service(@RequestBody OrderCocktailDAO orderCocktailDAO){
        log.info("order_cocktail_start");

        OrderStoreDataDTO orderData = orderService.order_cocktail_service(orderCocktailDAO);
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

    @GetMapping("/makeCocktail/{orderSeq}")
    public ResponseEntity make_cocktail_service(@PathVariable String orderSeq){
        log.info("칵테일 제조를 위한 Data 전송을 시작합니다. order -> {}",orderSeq);

        return null;
    }
}
