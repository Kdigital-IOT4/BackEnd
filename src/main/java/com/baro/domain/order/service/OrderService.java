package com.baro.domain.order.service;

import com.baro.domain.order.repository.DAO.OrderCocktailDAO;
import com.baro.domain.order.repository.DTO.OrderStoreDataDTO;
import com.baro.domain.order.util.GenerateOrderCodeUtil;
import com.baro.domain.user.service.MachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final MachineService machineService;
    private final GenerateOrderCodeUtil generateOrderCodeUtil;
    private final AmqpMessageService amqpMessageService;
    public String order_cocktail_service(OrderCocktailDAO orderData){
        String userPhoneNumber = orderData.getPhoneNumber();
        String machineId = orderData.getMachineId();

        //check machineId;
        if(! machineService.check_machine_id(machineId)){
            log.warn("machineId fail");
            return "machineId fail";
        }


        String orderCode = generateOrderCodeUtil.generateRandomString();
        log.info("add orderCode : {}",orderCode);
        OrderStoreDataDTO orderStoreData = new OrderStoreDataDTO();
        orderStoreData.setOrderCode(orderCode);

        amqpMessageService.sendOrderCode(orderStoreData);

        return null;
    }
}
