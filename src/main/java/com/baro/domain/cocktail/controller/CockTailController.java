package com.baro.domain.cocktail.controller;

import com.baro.domain.cocktail.domain.Base;
import com.baro.domain.cocktail.domain.Cocktail;
import com.baro.domain.cocktail.repository.DAO.CocktailDAO;
import com.baro.domain.cocktail.repository.DAO.ListCockTailDAO;
import com.baro.domain.cocktail.repository.DTO.BaseUploadDTO;
import com.baro.domain.cocktail.repository.DTO.CockTailUploadDTO;
import com.baro.domain.cocktail.service.BaseService;
import com.baro.domain.cocktail.service.CocktailService;
import com.baro.domain.cocktail.service.RecipeService;
import com.baro.domain.user.service.ImgUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/cocktail")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {"Authorization", "Content-Type"})
@RequiredArgsConstructor
public class CockTailController {
    private final BaseService baseService;
    private final ImgUploadService imgUploadService;

    private final CocktailService cocktailService;
    private final RecipeService recipeService;

    @GetMapping("/listCocktail")
    public ResponseEntity cocktail_list_read_controller(){
        List<ListCockTailDAO> cocktailList = cocktailService.cocktail_list_read_service();

        if(cocktailList.isEmpty()){
            log.warn("칵테일 정보를 찾을 수 없습니다.");
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("칵테일 정보를 찾을 수 없습니다.");
        }else{
            return ResponseEntity.ok(cocktailList);
        }
    }

    @GetMapping("/{seq}")
    public ResponseEntity cocktail_object_read_controller(@PathVariable Long seq){
       if(cocktailService.checkCocktailToSeq(seq)){
           //존재
            CocktailDAO cocktailDAO =
                    cocktailService.cocktail_object_read_service(seq);
            return ResponseEntity.ok(cocktailDAO);
       }else{
           //존재하지않음
           return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("칵테일 정보를 찾을 수 없습니다.");
       }
    }

    @GetMapping("/data/{en_name}")
    public ResponseEntity cocktail_object_name_read_controller(@PathVariable String en_name){
        if(cocktailService.checkCocktailToName(en_name)){
            Long seq = cocktailService.findCocktailSeqTOName(en_name);

            CocktailDAO cocktailDAO =
                    cocktailService.cocktail_object_read_service(seq);
            return ResponseEntity.ok(cocktailDAO);
        }else{
            //존재하지않음
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("칵테일 정보를 찾을 수 없습니다.");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity cocktail_upload_controller(@RequestParam("image") MultipartFile image,
                                                 @ModelAttribute CockTailUploadDTO cockTailUploadDTO , @RequestParam("baseList") List<String> baseList){
        String return_text;

        // CockTailUploadDTO 관련 로그
        log.info("EN_Name: {}", cockTailUploadDTO.getEN_Name());
        log.info("KR_Name: {}", cockTailUploadDTO.getKR_Name());
        log.info("Price: {}", cockTailUploadDTO.getPrice());
        log.info("Amount: {}", cockTailUploadDTO.getAmount());
        log.info("Alcohol: {}", cockTailUploadDTO.getAlcohol());
        log.info("Content: {}", cockTailUploadDTO.getContent());

        // baseList 관련 로그
        int baseListSize = baseList.size();
        int checkRecipeFlag = baseListSize;

        log.info("Base List: base Size :{}" , baseListSize);

        for (String base : baseList) {
            log.info(" - {}", base);
            if(baseService.checkBase(base)){
                //존재
                baseListSize--;
            }else{
                log.warn("존재하지않은 베이스");
            }
        }

        if(baseListSize == 0){
            log.info("베이스 체크 완료.. 다음스텝으로 넘어갑니다.");
            log.info("칵테일 이미지 업로드를 시작합니다.");
            String img_return_text = imgUploadService.uploadFile(image , "cocktail" , cockTailUploadDTO.getEN_Name());
            if(img_return_text.equals("fail")){
                return_text = "이미지 업로드에 실패하였습니다.";
                log.warn(return_text);
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
            }else{
                log.info("이미지 업로드에 성공하였습니다. \n다음 스텝을 시작합니다.");
                /**
                 * DB 저장시작... cocktail 정보
                 */
                if(cocktailService.checkCocktailToName(cockTailUploadDTO.getEN_Name())){
                    //이미 존재하는 칵테일
                    return_text ="이미 존재하는 칵테일입니다.";
                    log.warn(return_text);
                    return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(return_text);
                }else{
                    //존재하지 않은 칵테일
                    Cocktail cocktailData = cocktailService.cocktail_upload_service(img_return_text , cockTailUploadDTO);
                    if(cocktailData.equals(null)){
                        log.warn("칵테일 정보 저장중 문제가 발생하였습니다.");
                        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("칵테일 정보 저장중 문제가 발생하였습니다.");
                    }else{
                        log.info("칵테일 정보 저장 성공 .. 다음 스텝으로 넘어갑니다.");
                        /**
                         * 칵테일 찾기
                         * loop start
                         * 베이스 찾기 -> 이미 존재하는건 확인됨
                         * 칵테일 , 베이스 정보 전달
                         * 레시피 생성확인
                         * loop end
                         */

                        //칵테일찾기
                    }
                    log.info("레시피 저장을 시작합니다.");
                    Base base_data;
                    for (String base_name : baseList) {
                        log.info("base : {} 레시피 추가 -- 사작" , base_name);
                        log.info("base data 찾기 시작");
                        base_data = baseService.findBase(base_name);

                        String recipe_text = recipeService.recipe_upload_service(cocktailData , base_data);
                        if(recipe_text.equals("success")){
                            checkRecipeFlag--;
                        }else{
                            log.warn("레시피 저장중 문제 발생... 관리자 확인필요");
                        }
                    }

                    if(checkRecipeFlag == 0){
                        log.info("레시피 업로드 완료...");
                        return ResponseEntity.ok("success");
                    }else{
                        log.warn("레시피 업로드 중 문제발생");
                        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("칵테일 정보 저장중 문제가 발생하였습니다.");
                    }

                }

            }
        }else{
            return_text = "존재하지 않은 베이스가 포함되어있습니다.";
            log.warn(return_text);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(return_text);
        }
    }
}
