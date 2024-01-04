package com.baro.domain.cocktail.service;

import com.baro.domain.cocktail.domain.Base;
import com.baro.domain.cocktail.domain.Cocktail;
import com.baro.domain.cocktail.domain.Recipe;
import com.baro.domain.cocktail.repository.DAO.RecipeDAO;
import com.baro.domain.cocktail.repository.JPARecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeService {

    private final JPARecipeRepository recipeRepository;
    private final CocktailService cocktailService;

    public List<RecipeDAO> recipe_read_service(Long cocktailSeq){
        Cocktail cocktail = cocktailService.findCocktailToSeq(cocktailSeq);

        List<Recipe> recipeList = recipeRepository.findByCocktail(cocktail);

        return recipeList.stream()
                .map(this::convertToRecipeDAO)
                .collect(Collectors.toList());
    }
    private RecipeDAO convertToRecipeDAO(Recipe recipe) {
        RecipeDAO recipeDAO = new RecipeDAO();

        Base base = recipe.getBase();
        recipeDAO.setBase_en_name(base.getName());
        recipeDAO.setBase_kr_name(base.getKrName());
        /**
         * 레시피에서 양 어떻게 설정할건지 결정되면 해당하는 하드코딩 변환!
         */
        recipeDAO.setAmount(100);

        return recipeDAO;
    }
    public String recipe_upload_service(Cocktail cocktail , Base base){
        String return_text;

        try {
            recipeRepository.save(
                    Recipe.builder()
                            .cocktail(cocktail)
                            .base(base)
                            .build()
            );
            return_text ="success";
        }catch (DataIntegrityViolationException e) {
            // 데이터베이스 무결성 제약 조건 위반 - 키 중복  or 조건 위배
            return_text = "사용자의 데이터 제대로 검증되지 않았습니다.";
            log.warn("admin_register_service : {}" , return_text);
        } catch (JpaSystemException e) {
            // JPA 연동 중 문제 발생
            return_text = "데이터베이스 연동 중 오류가 발생";
            log.warn("admin_register_service : {}" , return_text);
        } catch (DataAccessException e) {
            // 데이터 액세스 오류
            return_text = "데이터베이스 액세스 중 오류가 발생";
            log.warn("admin_register_service : {}" , return_text);
        } catch (Exception e) {
            // 다른 모든 예외 처리
            return_text = "알 수 없는 오류가 발생";
            log.warn(e.getMessage());
        }
        return return_text;
    }
}
