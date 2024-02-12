package com.baro.domain.cocktail.controller;

import com.baro.domain.cocktail.repository.DAO.CartCocktailDetailDAO;
import com.baro.domain.cocktail.repository.DAO.CartRequestDAO;
import com.baro.domain.cocktail.repository.DAO.CocktailDAO;
import com.baro.domain.cocktail.repository.DAO.ListCockTailDAO;
import com.baro.domain.cocktail.service.CartDataService;
import com.baro.domain.cocktail.service.CocktailSortService;
import com.baro.domain.cocktail.util.ConvertingTypeFromStringToIntegerUtil;
import com.baro.domain.user.domain.Machine;
import com.baro.domain.user.repository.DAO.MachineBaseReadDAO;
import com.baro.domain.user.service.MachineBaseService;
import com.baro.domain.user.service.MachineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/cocktail/sort")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {"Authorization", "Content-Type"})
@RequiredArgsConstructor
public class CocktailSortController {
    private final MachineService machineService;
    private final MachineBaseService machineBaseService;
    private final CocktailSortService cocktailSortService;
    private final ConvertingTypeFromStringToIntegerUtil convertUtil;
    private final CartDataService cartDataService;
    @PostMapping("/cart")
    public ResponseEntity<Map<String, Object>> cart_cocktail_list_read_controller(@RequestBody CartRequestDAO requestDAO) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        List<Integer> cocktailSeqList = convertUtil.convertingString(requestDAO);
        List<CartCocktailDetailDAO> cocktailDataList= cartDataService.cart_data_find_service(cocktailSeqList);

        if(cocktailDataList == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Cocktail data not found - miss data"));
        }else if(cocktailDataList.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Cocktail data not found "));
        }

        response.put("status", "success");
        response.put("message", "success get cart data");
        response.put("data", cocktailDataList);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/{machine_id}")
    public ResponseEntity<Map<String, Object>> machineId_cocktail_list_read_controller(@PathVariable String machine_id) {
        Map<String, Object> response = new HashMap<>();

        if (!machineService.check_machine_id(machine_id)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Failed to place. Please check your input."));
        }

        List<CocktailDAO> cocktailList = cocktailSortService.cocktail_sort_for_machine(machine_id);
        if (cocktailList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Cocktail data not found for machineId: " + machine_id));
        } else {
            response.put("machine_id", machine_id);
            response.put("status", "success");
            response.put("message", "success get sort data");
            response.put("data", cocktailList);
        }

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> createErrorResponse(String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", errorMessage);
        return errorResponse;
    }
}
