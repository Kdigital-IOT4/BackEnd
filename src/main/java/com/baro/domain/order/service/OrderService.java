package com.baro.domain.order.service;

import com.baro.domain.cocktail.domain.Cocktail;
import com.baro.domain.cocktail.repository.DAO.RecipeDAO;
import com.baro.domain.cocktail.service.BaseService;
import com.baro.domain.cocktail.service.CocktailService;
import com.baro.domain.cocktail.service.RecipeService;
import com.baro.domain.order.repository.DAO.OrderCocktailDAO;
import com.baro.domain.order.repository.DAO.OrderCocktailDetailDAO;
import com.baro.domain.order.repository.DTO.OrderStoreDataDTO;
import com.baro.domain.order.repository.DTO.OrderStoreDataRecipeDTO;
import com.baro.domain.order.util.GenerateOrderCodeUtil;
import com.baro.domain.user.service.MachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final MachineService machineService;
    private final GenerateOrderCodeUtil generateOrderCodeUtil;
    private final CocktailService cocktailService;
    private final RecipeService recipeService;

    public String order_cocktail_service(OrderCocktailDAO orderData){
        String userPhoneNumber = orderData.getPhoneNumber();
        String machineId = orderData.getMachineId();

        //check machineId;
        if(! machineService.check_machine_id(machineId)){
            log.warn("machineId fail");
            return "machineId fail";
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

        // *     private String orderCode ,machine_id ,user_phoneNumber;
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

        return null;
    }
    private List<OrderStoreDataRecipeDTO> order_cocktail_detail_service(List<OrderCocktailDetailDAO> cocktailList) {
        List<OrderStoreDataRecipeDTO> orderDataList = new ArrayList<>();

        for (OrderCocktailDetailDAO item : cocktailList) {
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

}
