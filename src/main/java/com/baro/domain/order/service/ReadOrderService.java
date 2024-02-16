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

    public OrderReadDAO read_order_orderCode_data_service(String orderCode){
        OrderReadDAO orderReadData = null;
        
        try{
            Optional<Order> option_orderData =  orderRepository.findByOrderCode(orderCode);
            
            if(option_orderData.isEmpty()){
                //존재하지않음
                return null;
            }
            Order orderData = option_orderData.get();
            orderReadData = convertToOrderReadDAO(orderData);
        }
        catch (Exception e){
            log.warn("error - readOrder_orderCode : {}",e);
        }

       return orderReadData;

    }

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
