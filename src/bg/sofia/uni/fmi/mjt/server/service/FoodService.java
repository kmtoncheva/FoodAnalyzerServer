package bg.sofia.uni.fmi.mjt.server.service;

import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.response.SearchApiResponseDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.MalformedRequestBodyException;

/**
 * Service interface responsible for handling business logic related to retrieving and processing
 * food data from an external food API.
 * It encapsulates the preparation of API requests, delegation to {@link bg.sofia.uni.fmi.mjt.server.http.HttpService},
 * response parsing and integrates with a {@link bg.sofia.uni.fmi.mjt.server.cache.CacheService} for local caching.
 */
public interface FoodService {
    /**
     * Searches for food items using the specified keywords by delegating the query to the external API.
     *
     * @param keywords an array of keywords to be used in the search query
     * @return a {@link SearchApiResponseDto} containing the list of food items matching the search criteria
     * @throws MalformedRequestBodyException if the API response JSON is malformed or cannot be deserialized
     * @throws ApiException                  if a general API error occurs (e.g., invalid request, network issue)
     */
    SearchApiResponseDto searchFood(String[] keywords) throws ApiException;

    /**
     * Retrieves a detailed report for a specific food item using its unique FDC ID.
     * This method delegates to the {@link bg.sofia.uni.fmi.mjt.server.cache.CacheService} to obtain
     * cached data if available. If no cache entry exists, the {@code CacheService} internally handles
     * fetching the data from the external API via {@link bg.sofia.uni.fmi.mjt.server.http.HttpService}
     * and storing the result for future use.
     *
     * @param fdcId the unique identifier of the food item
     * @return a {@link ReportFoodItemDto} representing the retrieved food item report
     * @throws MalformedRequestBodyException if the API response JSON is malformed or cannot be deserialized
     * @throws ApiException                  if the item is missing, undocumented, or an HTTP error occurs
     */
    ReportFoodItemDto getFoodReport(String fdcId) throws ApiException;
}