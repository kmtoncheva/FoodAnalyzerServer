package bg.sofia.uni.fmi.mjt.server.service;

import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.response.SearchApiResponseDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.MalformedRequestBodyException;
import bg.sofia.uni.fmi.mjt.server.service.cache.CacheService;
import bg.sofia.uni.fmi.mjt.server.service.http.HttpService;

/**
 * Service interface responsible for handling business logic related to retrieving and processing
 * food data from an external food API.
 * It encapsulates the preparation of API requests, delegation to {@link HttpService},
 * response parsing and integrates with a {@link CacheService} for local caching.
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
    SearchApiResponseDto searchFoodByKeywords(String[] keywords) throws ApiException;

    /**
     * Retrieves a detailed report for a specific food item using its unique FDC ID.
     * This method delegates to the {@link CacheService} to obtain
     * cached data if available. If no cache entry exists, the {@code CacheService} internally handles
     * fetching the data from the external API via {@link HttpService}
     * and storing the result for future use.
     *
     * @param fdcId the unique identifier of the food item
     * @return a {@link ReportFoodItemDto} representing the retrieved food item report
     * @throws MalformedRequestBodyException if the API response JSON is malformed or cannot be deserialized
     * @throws ApiException                  if the item is missing, undocumented, or an HTTP error occurs
     */
    ReportFoodItemDto getFoodReport(String fdcId) throws ApiException;

    /**
     * Retrieves a detailed report for a specific 'branded' food item by its unique gtinUpc code.
     * The item's gtinUpc corresponding to an ID should be stored in the cache in advance (from previous requests).
     * After retrieving the ID from the cache the food is searched by it with the method above - in the cache if
     * present or sending new http request to the external API.
     *
     * @param gtinUpc the unique code every 'branded' food should have - it consists of numeric characters only.
     * @return a {@link ReportFoodItemDto} representing the retrieved food item report
     * @throws ApiException if the item is missing, undocumented, or an HTTP error occurs
     */
    ReportFoodItemDto getFoodByBarcode(String gtinUpc) throws ApiException;
}