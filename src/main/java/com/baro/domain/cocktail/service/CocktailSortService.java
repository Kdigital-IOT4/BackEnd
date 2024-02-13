package com.baro.domain.cocktail.service;

import com.baro.domain.cocktail.domain.Base;
import com.baro.domain.cocktail.domain.Cocktail;
import com.baro.domain.cocktail.domain.Recipe;
import com.baro.domain.cocktail.repository.DAO.BaseMachineReadDAO;
import com.baro.domain.cocktail.repository.DAO.CocktailDAO;
import com.baro.domain.cocktail.repository.JPARecipeRepository;
import com.baro.domain.user.repository.DAO.MachineBaseReadDAO;
import com.baro.domain.user.repository.JPAMachineBaseRepository;
import com.baro.domain.user.service.MachineBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CocktailSortService {
    private final MachineBaseService machineBaseService;
    private final BaseService baseService;
    private final CocktailService cocktailService;
    private final JPARecipeRepository recipeRepository;

    public List<CocktailDAO> cocktail_sort_for_machine(String machine_id) {
        MachineBaseReadDAO baseData = machineBaseService.read_machine_base_service(machine_id);
        List<BaseMachineReadDAO> baseListData = baseData.getBaseList();
        List<Long> cocktail_check_list = gather_cocktail_check_list(baseListData);
        int baseSize = baseListData.size();

        List<Cocktail> possibleCocktails = find_possible_cocktails(cocktail_check_list, baseSize);
        return convertToCocktailDAOList(possibleCocktails);
    }

    private List<Long> gather_cocktail_check_list(List<BaseMachineReadDAO> baseListData) {
        List<Long> cocktail_check_list = new ArrayList<>();

        for (BaseMachineReadDAO item : baseListData) {
            Base base_data = baseService.findBaseToSeq(item.getBase_seq());
            List<Recipe> recipeList = recipeRepository.findByBase(base_data);

            for (Recipe recipe_item : recipeList) {
                cocktail_check_list.add(recipe_item.getCocktail().getSeq());
            }
        }

        return cocktail_check_list;
    }

    private List<Cocktail> find_possible_cocktails(List<Long> cocktail_check_list, int baseSize) {
        Map<Long, Integer> cocktail_checkMapping = new HashMap<>();

        for (Long item : cocktail_check_list) {
            cocktail_checkMapping.put(item, cocktail_checkMapping.getOrDefault(item, 0) + 1);
        }

        List<Cocktail> possibleCocktails = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : cocktail_checkMapping.entrySet()) {
            Long item = entry.getKey();
            Integer count = entry.getValue();

            if (count.equals(baseSize)) {
                log.info("item {} 추가", item);
                possibleCocktails.add(cocktailService.findCocktailToSeq(item));
            }
        }

        return possibleCocktails;
    }

    private List<CocktailDAO> convertToCocktailDAOList(List<Cocktail> cocktails) {
        return cocktails.stream()
                .map(this::convertToCocktailDAO)
                .collect(Collectors.toList());
    }

    private CocktailDAO convertToCocktailDAO(Cocktail cocktail) {
        CocktailDAO cocktailDAO = new CocktailDAO();
        cocktailDAO.setSeq(cocktail.getSeq());
        cocktailDAO.setEN_Name(cocktail.getName());
        cocktailDAO.setKR_Name(cocktail.getKrName());
        cocktailDAO.setPrice(cocktail.getPrice());
        cocktailDAO.setAmount(cocktail.getAmount());
        cocktailDAO.setAlcohol(cocktail.getAlcohol());
        cocktailDAO.setContent(cocktail.getContent());
        cocktailDAO.setImgURL(cocktail.getFileURL());
        return cocktailDAO;
    }
}
