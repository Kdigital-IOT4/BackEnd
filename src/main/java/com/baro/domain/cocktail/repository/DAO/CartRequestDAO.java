package com.baro.domain.cocktail.repository.DAO;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@Slf4j
public class CartRequestDAO {
    //cart가 가지고 있는 정보를 받아오기위한 객체
    private List<String> cartDataList;
}
