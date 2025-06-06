package bg.sofia.uni.fmi.mjt.server.service;

import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.response.SearchApiResponseDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;

public interface FoodService {
    SearchApiResponseDto searchFood(String[] keywords) throws ApiException;
    ReportFoodItemDto getFoodReport(String fdcId) throws ApiException;
}