package com.baro.domain.order.service;

import com.baro.domain.order.domain.Order;
import com.baro.domain.order.repository.DAO.OrderReadDAO;
import com.baro.domain.order.repository.DAO.OrderWaitingListDAO;
import com.baro.domain.order.repository.JPAMongoOrderRepository;
import com.baro.domain.order.repository.enumeration.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReadOrderService {
    private final JPAMongoOrderRepository orderRepository;
    public OrderWaitingListDAO read_order_waitingList_service(String machine_id){
        OrderWaitingListDAO orderWaitingList = new OrderWaitingListDAO();

        List<Order> orderDataList = orderRepository.findByMachineIdAndStatus(machine_id , OrderStatus.WAITING);


        List<OrderReadDAO> orderReadList = new ArrayList<>();
        for (Order order : orderDataList) {
            OrderReadDAO orderReadDAO = convertToOrderReadDAO(order);
            orderReadList.add(orderReadDAO);
        }

        orderWaitingList.setOrderReadList(orderReadList);

        return orderWaitingList;
    }

    private OrderReadDAO convertToOrderReadDAO(Order order) {
        OrderReadDAO orderReadDAO = new OrderReadDAO();
        orderReadDAO.setOrderCode(order.getOrderCode());
        orderReadDAO.setUserPhoneNumber(order.getUserPhoneNumber());
        orderReadDAO.setRecipeList(order.getRecipeList());
        orderReadDAO.setCreateOrderTime(order.getCreateOrderTime());

        return orderReadDAO;
    }
}
