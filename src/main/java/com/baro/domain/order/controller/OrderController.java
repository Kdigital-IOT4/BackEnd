package com.baro.domain.order.controller;

import com.baro.domain.order.repository.DAO.OrderCocktailDAO;
import com.baro.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        orderService.order_cocktail_service(orderCocktailDAO);

        return ResponseEntity.ok("check");
    }
}
