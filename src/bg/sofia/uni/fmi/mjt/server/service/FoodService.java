package bg.sofia.uni.fmi.mjt.server.service;

import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;

public interface FoodService {
    String searchFood(String[] keywords) throws ApiException;
    String getFoodReport(String fdcId);
}