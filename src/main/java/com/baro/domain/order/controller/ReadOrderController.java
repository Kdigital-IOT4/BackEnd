package com.baro.domain.order.controller;

import com.baro.domain.order.repository.DAO.OrderReadDAO;
import com.baro.domain.order.repository.DAO.OrderWaitingListDAO;
import com.baro.domain.order.service.OrderService;
import com.baro.domain.order.service.ReadOrderService;
import com.baro.domain.user.service.MachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/order/read")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {"Authorization", "Content-Type"})
@RequiredArgsConstructor
public class ReadOrderController {
    private final MachineService machineService;
    private final ReadOrderService readOrderService;
    private final OrderService orderService;

    Map<String, Object> response = new HashMap<>();

    @GetMapping("/orderProcess/{orderCode}")
    public ResponseEntity<Map<String, Object>> read_order_orderCode_data_controller(@PathVariable String orderCode){
        if(! orderService.orderCode_check_service(orderCode)){
            //존재하지 않음
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Failed to place. Please check your OrderCode"));
        }

        OrderReadDAO orderData = readOrderService.read_order_orderCode_data_service(orderCode);
        if(orderData != null){
            response.put("orderCode", orderCode);
            response.put("status", "success");
            response.put("message", "success get order Data");
            response.put("data", orderData);
        }else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Order data not found for orderCode: " + orderCode));
        }

        return ResponseEntity.ok(response);

    }
    @GetMapping("/{machine_id}")
    public ResponseEntity<Map<String, Object>> read_order_waitingList_controller(@PathVariable String machine_id){
        log.info("reade order -> waiting List start");
        if (!machineService.check_machine_id(machine_id)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Failed to place. Please check your input."));
        }

        OrderWaitingListDAO orderData = readOrderService.read_order_waitingList_service(machine_id);


        if(orderData != null){
            response.put("machine_id", machine_id);
            response.put("status", "success");
            response.put("message", "success get order Data");
            response.put("data", orderData);
        }else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Order data not found for machineId: " + machine_id));
        }

        return ResponseEntity.ok(response);
    }


    private Map<String, Object> createErrorResponse (String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", errorMessage);
        return errorResponse;
    }
}
