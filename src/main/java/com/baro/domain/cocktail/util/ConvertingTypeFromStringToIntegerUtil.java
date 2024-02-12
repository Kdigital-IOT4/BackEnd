package com.baro.domain.cocktail.util;

import com.baro.domain.cocktail.repository.DAO.CartRequestDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class ConvertingTypeFromStringToIntegerUtil {
    public List<Integer> convertingString(CartRequestDAO requestDAO){
        log.info("request data : {}", requestDAO.getCartDataList());
        String jsonString= requestDAO.getCartDataList().toString();


        List<Integer> integerList = Arrays.stream(jsonString.replaceAll("[\\[\\]]", "").split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        for(Integer item : integerList){
            log.info("{}",item);
        }

        List<Integer> sortedIntegerList = integerList.stream()
                .sorted()
                .collect(Collectors.toList());

        return sortedIntegerList;
    }
}
