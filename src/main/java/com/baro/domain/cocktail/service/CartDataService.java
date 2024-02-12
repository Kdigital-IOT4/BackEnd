package com.baro.domain.cocktail.service;

import com.baro.domain.cocktail.domain.Cocktail;
import com.baro.domain.cocktail.repository.DAO.CartCocktailDetailDAO;
import com.baro.domain.cocktail.repository.DAO.CartDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartDataService {
    private final CocktailService cocktailService;
    public List<CartCocktailDetailDAO> cart_data_find_service(List<Integer> cocktailSeqList){
        List<CartCocktailDetailDAO> cocktailDetailList = new ArrayList<>();

        Long typeItem;
        Cocktail cocktailData;
        for (Integer item : cocktailSeqList){
            //item 존재하는지 체크
            typeItem = item.longValue();
            if(! cocktailService.checkCocktailToSeq(typeItem)){
                log.warn("cocktail 정보중 비정상적인 데이터 감지");
                return null;
            };

            cocktailData = cocktailService.findCocktailToSeq(typeItem);
            CartCocktailDetailDAO cocktailDetailData = convertCocktailToCartCocktailDetailDAO(cocktailData);
            cocktailDetailList.add(cocktailDetailData);
            log.info("{} 추가 완료" , typeItem);
        }

        return cocktailDetailList;
    }

    private CartCocktailDetailDAO convertCocktailToCartCocktailDetailDAO(Cocktail cocktail) {
        CartCocktailDetailDAO cartCocktailDetailDAO = new CartCocktailDetailDAO();
        cartCocktailDetailDAO.setSeq(cocktail.getSeq());
        cartCocktailDetailDAO.setEn_name(cocktail.getName()); // Assuming name is equivalent to en_name
        cartCocktailDetailDAO.setKr_name(cocktail.getKrName());
        cartCocktailDetailDAO.setPrice(cocktail.getPrice());
        cartCocktailDetailDAO.setImg_URL(cocktail.getFileURL());

        return cartCocktailDetailDAO;
    }
}
