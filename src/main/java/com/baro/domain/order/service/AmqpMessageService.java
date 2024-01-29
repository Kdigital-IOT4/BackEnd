package com.baro.domain.order.service;

import com.baro.domain.order.repository.DTO.MessageDTO;
import com.baro.domain.order.repository.DTO.OrderStoreDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmqpMessageService {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public String sendOrderCode(OrderStoreDataDTO orderStoreDataDTO){
        log.info("rabbitMQ : send Code -> {}",orderStoreDataDTO.getOrderCode());
        rabbitTemplate.convertAndSend(exchangeName , routingKey , orderStoreDataDTO);
        return "success";
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void reciveMessage(OrderStoreDataDTO orderStoreDataDTO) {
        log.info("Received message: {}", orderStoreDataDTO.getOrderCode());
    }


}
