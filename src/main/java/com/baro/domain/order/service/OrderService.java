package com.baro.domain.order.service;

import com.baro.domain.cocktail.domain.Cocktail;
import com.baro.domain.cocktail.repository.DAO.RecipeDAO;
import com.baro.domain.cocktail.service.CocktailService;
import com.baro.domain.cocktail.service.RecipeService;
import com.baro.domain.order.domain.Order;
import com.baro.domain.order.repository.DTO.OrderCocktailDTO;
import com.baro.domain.order.repository.DTO.OrderCocktailDetailDTO;
import com.baro.domain.order.repository.DTO.OrderStoreDataDTO;
import com.baro.domain.order.repository.DTO.OrderStoreDataRecipeDTO;
import com.baro.domain.order.repository.JPAMongoOrderRepository;
import com.baro.domain.order.util.GenerateOrderCodeUtil;
import com.baro.domain.user.repository.DAO.MachineBaseReadDAO;
import com.baro.domain.user.service.MachineBaseService;
import com.baro.domain.user.service.MachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final MachineService machineService;
    private final GenerateOrderCodeUtil generateOrderCodeUtil;
    private final CocktailService cocktailService;
    private final RecipeService recipeService;
    private final JPAMongoOrderRepository mongoOrderRepository;
    private final CocktailQueueService cocktailQueueService;
    private final MachineBaseService machineBaseService;
    public boolean orderCode_check_service(String orderCode){
        //order code exists checking service

        if(mongoOrderRepository.existsByOrderCode(orderCode)){
            log.info("order Code check completed");
            return true;
        }else{
            log.warn("not exists orderCode -> please check connected");
            return false;
        }
    }
    public String order_machine_base_find_service(Order order){
        String machine_id = order.getMachineId();
        MachineBaseReadDAO machineBase = machineBaseService.read_machine_base_service(machine_id);
        List<OrderStoreDataRecipeDTO> rootRecipeList = order.getRecipeList();
        for(OrderStoreDataRecipeDTO item : rootRecipeList ) {
            List<RecipeDAO> recipeList =item.getBaseList();
            for(RecipeDAO detail_item : recipeList){
                detail_item.getBase_seq();
            }
        }
        return null;
    }
    public Optional<Order> order_data_find_service(String orderCode){
       Optional<Order> order = mongoOrderRepository.findByOrderCode(orderCode);

       return order;
    }

    public OrderStoreDataDTO order_cocktail_service(OrderCocktailDTO orderData){
        String userPhoneNumber = orderData.getPhoneNumber();
        String machineId = orderData.getMachineId();

        //check machineId;
        if(! machineService.check_machine_id(machineId)){
            log.warn("machineId fail");
            return null;
        }
        /**
         *     private String orderCode;x
         *     private String machine_id;x
         *     private String user_phoneNumber;x
         *     private int total_price;x
         *     private LocalDateTime createOrderTime;x
         *     private List<OrderStoreDataRecipeDTO> recipeList;x
         */
        OrderStoreDataDTO orderStoreData = new OrderStoreDataDTO();

        String orderCode = generateOrderCodeUtil.generateRandomString();
        log.info("add orderCode : {}",orderCode);

        // *private String orderCode ,machine_id ,user_phoneNumber;
        orderStoreData.setOrderCode(orderCode);
        orderStoreData.setMachine_id(machineId);
        orderStoreData.setUser_phoneNumber(userPhoneNumber);
        orderStoreData.setCreateOrderTime(LocalDateTime.now());

        // recipeList
        List<OrderStoreDataRecipeDTO> recipeList = order_cocktail_detail_service(orderData.getCocktailList());
        orderStoreData.setRecipeList(recipeList);

        //total price
        int total_price = order_totalPrice_calc_service(recipeList);
        orderStoreData.setTotal_price(total_price);

        // mongo register
        String return_text = order_save_service(orderStoreData);
        if(return_text.equals("success")){
            log.info("정상적인 업로드요청이 완료");
            /**
             * start queue register
             */
            log.info("queue 등록을 시작합니다.");
            String queue_upload_text = cocktailQueueService.register_cocktailQueue_service(machineId , orderCode);
            if(queue_upload_text.equals("success")){
                //큐등록성공
                log.info("큐 등록성공");
                return orderStoreData;
            }else{
                //실패
                log.info("큐 등록실패");
                return null;
            }

        }else{
            log.info("비정상적인 업로드요청");
            return null;
        }
    }
    private List<OrderStoreDataRecipeDTO> order_cocktail_detail_service(List<OrderCocktailDetailDTO> cocktailList) {
        List<OrderStoreDataRecipeDTO> orderDataList = new ArrayList<>();

        for (OrderCocktailDetailDTO item : cocktailList) {
            Long cocktailSeq = item.getCocktailSeq();
            Cocktail cocktail = cocktailService.findCocktailToSeq(cocktailSeq);
            String cocktail_en_name = cocktail.getName();
            int cocktail_price = cocktail.getPrice();

            List<RecipeDAO> recipeList = recipeService.recipe_read_service(cocktailSeq);

            OrderStoreDataRecipeDTO orderData = new OrderStoreDataRecipeDTO();
            orderData.setCocktail_en_name(cocktail_en_name);
            orderData.setCocktail_price(cocktail_price);
            orderData.setBaseList(recipeList);

            orderDataList.add(orderData);
        }

        return orderDataList;
    }

    private int order_totalPrice_calc_service(List<OrderStoreDataRecipeDTO> recipeList){
       int total_price = 0;

        for(OrderStoreDataRecipeDTO item : recipeList){
            total_price += item.getCocktail_price();
        }

        return total_price;
    }

    private String order_save_service(OrderStoreDataDTO orderStoreData ){
        //order 객체 생성
        Order order = new Order();
        order.setOrderCode(orderStoreData.getOrderCode());
        order.setMachineId(orderStoreData.getMachine_id());
        order.setUserPhoneNumber(orderStoreData.getUser_phoneNumber());
        order.setCreateOrderTime(orderStoreData.getCreateOrderTime());
        order.setTotalPrice(orderStoreData.getTotal_price());
        order.setRecipeList(orderStoreData.getRecipeList());
        // register order Bils
        try{
            mongoOrderRepository.save(order);
            return "success";
        }catch (Exception e) {
            log.warn("mongo order upload fail -->\n{}",e);
            return "fail";
        }

    }

}
