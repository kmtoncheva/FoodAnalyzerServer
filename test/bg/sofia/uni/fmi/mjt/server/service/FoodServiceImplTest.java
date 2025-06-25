package bg.sofia.uni.fmi.mjt.server.service;

import bg.sofia.uni.fmi.mjt.server.service.cache.CacheService;
import bg.sofia.uni.fmi.mjt.server.service.cache.FileCacheServiceImpl;
import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.SearchFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.response.SearchApiResponseDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiServiceUnavailableException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.MalformedRequestBodyException;
import bg.sofia.uni.fmi.mjt.server.service.http.HttpService;
import bg.sofia.uni.fmi.mjt.server.service.http.HttpServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static data.FoodData.brandedFoodItem;
import static data.FoodData.experimentalFoodItem;
import static data.FoodData.foundationFoodItem;
import static data.FoodData.srLegacyFoodItem;
import static data.FoodData.surveyFoodItem;
import static data.JsonData.emptyJsonResponse;
import static data.JsonData.sampleJsonResponse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FoodServiceImplTest {
    HttpService httpService = mock(HttpServiceImpl.class);
    CacheService cacheService = mock(FileCacheServiceImpl.class);

    FoodService foodService = new FoodServiceImpl(httpService, cacheService);

    @Test
    void testSearchFoodByKeywordsWithListOfFoods() throws ApiException {
        Mockito.when(httpService.get(anyString())).thenReturn(sampleJsonResponse);

        String[] keywords = {"raffaello", "treat"};
        SearchApiResponseDto result = foodService.searchFoodByKeywords(keywords);

        Assertions.assertNotNull(result, "Should have parsed correctly.");
        assertEquals(1, result.getFoods().size(), "Should have parsed one food item.");

        SearchFoodItemDto food = result.getFoods().get(0);
        assertEquals("415269", food.getFdcId());
        assertEquals("RAFFAELLO, ALMOND COCONUT TREAT", food.getDescription());
        assertEquals("009800146130", food.getGtinUpc());

        verify(httpService, times(1)).get(anyString());
    }

    @Test
    void testSearchFoodByKeywordsWithEmptyOfFoods() throws ApiException {
        Mockito.when(httpService.get(anyString())).thenReturn(emptyJsonResponse);

        String[] keywords = {"none"};
        SearchApiResponseDto result = foodService.searchFoodByKeywords(keywords);

        Assertions.assertNotNull(result, "Should have parsed correctly.");
        assertEquals(0, result.getFoods().size(), "Should have parsed no food items.");

        verify(httpService, times(1)).get(anyString());
    }

    @Test
    void testSearchFoodByKeywordsWithApiException() throws ApiException {
        when(httpService.get(anyString())).thenThrow(
            new ApiServiceUnavailableException("API failure", "API failure"));

        String[] keywords = {"none"};
        ApiException exception = assertThrows(ApiException.class, () -> {
            foodService.searchFoodByKeywords(keywords);
        });
        Assert.assertEquals("API failure", exception.getMessage());
    }

    @Test
    void testSearchFoodByKeywordsWithInvalidJSON() throws ApiException {
        Mockito.when(httpService.get(anyString())).thenReturn("{ invalid JSON }");

        String[] keywords = {"apple"};

        Assertions.assertThrows(MalformedRequestBodyException.class, () -> {
            foodService.searchFoodByKeywords(keywords);
        }, "Should throw an exception when the http service return invalid json.");
    }

    @Test
    void testGetFoodReportWithBrandedFoodItem() throws ApiException, IOException {
        when(cacheService.getReportById(anyString(), any())).thenReturn(brandedFoodItem);

        ReportFoodItemDto foodItem = foodService.getFoodReport("id");


        Assertions.assertNotNull(foodItem, "Should have parsed correctly.");
        assertEquals("NUT 'N BERRY MIX", foodItem.getDescription(), "Description of the food should be parsed.");
        Assertions.assertNotNull(foodItem.getIngredients(), "Should have parsed correctly.");
        Assertions.assertNotNull(foodItem.getLabelNutrients(), "Should have parsed correctly.");
        Assertions.assertNotNull(foodItem.getLabelNutrients().getCalories(), "Should have parsed correctly.");
        Assertions.assertNotNull(foodItem.getLabelNutrients().getFat(), "Should have parsed correctly.");
        Assertions.assertNull(foodItem.getLabelNutrients().getCarbohydrates(),
            "Should have handled correctly missing fields.");
        assertEquals(Float.valueOf("4.0"), foodItem.getLabelNutrients().getProtein().getValue(),
            "Values of the nutrients should be parsed.");

        verify(cacheService, times(1)).getReportById(anyString(), any());
    }

    @Test
    void testGetFoodReportWithFoundationFoodItem() throws ApiException, IOException {
        when(cacheService.getReportById(anyString(), any())).thenReturn(foundationFoodItem);
        ReportFoodItemDto foodItem = foodService.getFoodReport("id");

        Assertions.assertNotNull(foodItem, "Should have parsed correctly.");
        assertEquals("Almond butter, creamy", foodItem.getDescription(), "Description of the food should be parsed.");
        Assertions.assertNull(foodItem.getIngredients(), "Should have parsed correctly.");
        Assertions.assertNull(foodItem.getLabelNutrients(), "Should have parsed correctly.");

        verify(cacheService, times(1)).getReportById(anyString(), any());
    }

    @Test
    void testGetFoodReportWithSrLegacyFoodItem() throws ApiException, IOException {
        when(cacheService.getReportById(anyString(), any())).thenReturn(srLegacyFoodItem);
        ReportFoodItemDto foodItem = foodService.getFoodReport("id");

        Assertions.assertNotNull(foodItem, "Should have parsed correctly.");
        assertEquals("Abiyuch, raw", foodItem.getDescription(), "Description of the food should be parsed.");
        Assertions.assertNull(foodItem.getIngredients(), "Should have parsed correctly.");
        Assertions.assertNull(foodItem.getLabelNutrients(), "Should have parsed correctly.");

        verify(cacheService, times(1)).getReportById(anyString(), any());
    }

    @Test
    void testGetFoodReportWithSurveyFoodItem() throws ApiException, IOException {
        when(cacheService.getReportById(anyString(), any())).thenReturn(surveyFoodItem);
        ReportFoodItemDto foodItem = foodService.getFoodReport("id");

        Assertions.assertNotNull(foodItem, "Should have parsed correctly.");
        assertEquals("Alfredo sauce with poultry", foodItem.getDescription(),
            "Description of the food should be parsed.");
        Assertions.assertNull(foodItem.getIngredients(), "Should have parsed correctly.");
        Assertions.assertNull(foodItem.getLabelNutrients(), "Should have parsed correctly.");

        verify(cacheService, times(1)).getReportById(anyString(), any());
    }

    @Test
    void testGetFoodReportWithExperimentalFoodItem() throws ApiException, IOException {
        when(cacheService.getReportById(anyString(), any())).thenReturn(experimentalFoodItem);

        Assertions.assertThrows(FoodItemNotFoundException.class, () -> {
            foodService.getFoodReport("id");
        }, "Should throw an exception when food is not well documented.");
    }

    @Test
    void testGetFoodReportWithBadJsonSyntax() throws ApiException, IOException {
        when(cacheService.getReportById(anyString(), any())).thenReturn("bad JSON");

        Assertions.assertThrows(MalformedRequestBodyException.class, () -> {
            foodService.getFoodReport("id");
        }, "Should throw an exception when json is invalid.");
    }

    @Test
    void testGetFoodByBarcodeWithCachedItem() throws IOException, ApiException {
        when(cacheService.getIdFromIndex(anyString())).thenReturn("id");
        when(cacheService.getReportById(anyString(), any())).thenReturn(surveyFoodItem);
        ReportFoodItemDto foodItem = foodService.getFoodByBarcode("id");

        Assertions.assertNotNull(foodItem, "Should have parsed correctly.");
        assertEquals("Alfredo sauce with poultry", foodItem.getDescription(),
            "Description of the food should be parsed.");
        Assertions.assertNull(foodItem.getIngredients(), "Should have parsed correctly.");
        Assertions.assertNull(foodItem.getLabelNutrients(), "Should have parsed correctly.");

        verify(cacheService, times(1)).getIdFromIndex("id");
    }

    @Test
    void testGetFoodByBarcodeWithMissingItem() {
        when(cacheService.getIdFromIndex(anyString())).thenReturn(null);

        Assertions.assertThrows(FoodItemNotFoundException.class, () -> {
            foodService.getFoodByBarcode("id");
        }, "Should throw an exception when there is no info for the food item in the cache.");
    }
}