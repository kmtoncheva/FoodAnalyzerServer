package bg.sofia.uni.fmi.mjt.server.service;

import bg.sofia.uni.fmi.mjt.server.cache.FileCacheService;
import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.response.SearchApiResponseDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiServiceUnavailableException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.MalformedRequestBodyException;
import bg.sofia.uni.fmi.mjt.server.http.HttpService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.API_KEY_NAME;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.API_KEY_PARAM;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.BASE_URL;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.DELIMITER;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.QUERY_PARAM;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.REPLACEMENT_DELIMITER;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.REPORT_ENDPOINT;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.SEARCH_ENDPOINT;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.TARGET_DELIMITER;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.INVALID_JSON_PAYLOAD_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.NOT_WELL_DOCUMENTED_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.NO_MATCHING_FOODS_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.SERVER_UNABLE_ERROR_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.TRY_AGAIN_LATER_MSG;

/**
 * Implementation of the {@link FoodService} interface that prepares and delegates
 * requests related to food data. It constructs API URLs and uses the {@link HttpService}
 * to send requests and parse responses.
 *
 * This class handles business logic.
 */
public class FoodServiceImpl implements FoodService {
    private final HttpService httpService;
    private final FileCacheService fileCacheService;

    public FoodServiceImpl(HttpService httpService, FileCacheService fileCacheService) {
        this.httpService = httpService;
        this.fileCacheService = fileCacheService;
    }

    /**
     * Searches for food items using the provided keywords by preparing an API request,
     * delegating the call to {@link HttpService}, and parsing the response.
     *
     * @param keywords array of keywords to be used as the search query
     * @return a {@link SearchApiResponseDto} containing the list of matched food items
     * @throws MalformedRequestBodyException if the response JSON is malformed or cannot be deserialized
     * @throws ApiException if the response body cannot be parsed or if an HTTP error occurs
     */
    @Override
    public SearchApiResponseDto searchFood(String[] keywords) throws ApiException {
        String queryParams = encodeTokens(keywords);
        String apiKey = getApiKey();

        String url = BASE_URL + SEARCH_ENDPOINT + API_KEY_PARAM + apiKey + QUERY_PARAM + queryParams;
        String bodyResponse = httpService.get(url);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(bodyResponse, SearchApiResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new MalformedRequestBodyException(INVALID_JSON_PAYLOAD_MSG + e.getMessage(),
                SERVER_UNABLE_ERROR_MSG + TRY_AGAIN_LATER_MSG, e);
        }
    }

//    /**
//     * Searches for a specific food item using the provided ID by preparing an API request,
//     * delegating the call to {@link HttpService}, and parsing the response.
//     * It is responsible for handling the case where the item is missing or is not well-documented.
//     *
//     * @param fdcId ID that corresponds to the searched food item
//     * @return a {@link ReportFoodItemDto} containing the item that has been retrieved from the API
//     * @throws MalformedRequestBodyException if the response JSON is malformed or cannot be deserialized
//     * @throws ApiException if the searched item is missing or if an HTTP error occurs
//     */
//    @Override
//    public ReportFoodItemDto getFoodReport(String fdcId) throws ApiException {
//        String apiKey = getApiKey();
//
//        String url = BASE_URL + REPORT_ENDPOINT + fdcId + API_KEY_PARAM + apiKey;
//        String bodyResponse;
//
//        try {
//            bodyResponse = httpService.get(url);
//        } catch (FoodItemNotFoundException e) {
//            throw new FoodItemNotFoundException(null, NO_MATCHING_FOODS_MSG + fdcId);
//        }
//
//        try {
//            Gson gson = new Gson();
//            ReportFoodItemDto foodItem = gson.fromJson(bodyResponse, ReportFoodItemDto.class);
//
//            if (foodItem.getDescription() == null) {
//                throw new FoodItemNotFoundException(null, NOT_WELL_DOCUMENTED_MSG);
//            }
//
//            return foodItem;
//        } catch (JsonSyntaxException e) {
//            throw new MalformedRequestBodyException(INVALID_JSON_PAYLOAD_MSG + e.getMessage(),
//                SERVER_UNABLE_ERROR_MSG + TRY_AGAIN_LATER_MSG, e);
//        }
//    }


    @Override
    public ReportFoodItemDto getFoodReport(String fdcId) throws ApiException {
        String cacheKey = fdcId;

        String bodyResponse;
        try {
            bodyResponse = fileCacheService.get(cacheKey, () -> {
                String apiKey = getApiKey();
                String url = BASE_URL + REPORT_ENDPOINT + fdcId + API_KEY_PARAM + apiKey;

                try {
                    return httpService.get(url);
                } catch (FoodItemNotFoundException e) {
                    throw new FoodItemNotFoundException(null, NO_MATCHING_FOODS_MSG + fdcId);
                }
            });
        } catch (IOException e) {
            throw new ApiServiceUnavailableException(SERVER_UNABLE_ERROR_MSG, TRY_AGAIN_LATER_MSG, e);
        }

        try {
            Gson gson = new Gson();
            ReportFoodItemDto foodItem = gson.fromJson(bodyResponse, ReportFoodItemDto.class);

            if (foodItem.getDescription() == null) {
                throw new FoodItemNotFoundException(null, NOT_WELL_DOCUMENTED_MSG);
            }

            return foodItem;
        } catch (JsonSyntaxException e) {
            throw new MalformedRequestBodyException(INVALID_JSON_PAYLOAD_MSG + e.getMessage(),
                SERVER_UNABLE_ERROR_MSG + TRY_AGAIN_LATER_MSG, e);
        }
    }

    /**
     * Encodes the given keywords for use in a URL query parameter.
     *
     * @param keywords array of search keywords
     * @return a URL-encoded string representation of the joined keywords
     */
    private static String encodeTokens(String[] keywords) {
        String joined = String.join(DELIMITER, keywords);
        return URLEncoder.encode(joined, StandardCharsets.UTF_8).replace(TARGET_DELIMITER, REPLACEMENT_DELIMITER);
    }

    /**
     * Retrieves the API key used for authenticating API requests.
     *
     * @return the API key string from environment variables
     */
    private static String getApiKey() {
        return System.getenv(API_KEY_NAME);
    }
}