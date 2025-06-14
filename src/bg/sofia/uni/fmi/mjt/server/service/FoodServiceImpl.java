package bg.sofia.uni.fmi.mjt.server.service;

import bg.sofia.uni.fmi.mjt.server.service.cache.CacheService;
import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.SearchFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.response.SearchApiResponseDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.CacheException;
import bg.sofia.uni.fmi.mjt.server.exceptions.ConfigurationException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiServiceUnavailableException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.MalformedRequestBodyException;
import bg.sofia.uni.fmi.mjt.server.service.http.HttpService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.API_KEY_PARAM;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.QUERY_PARAM;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.REPORT_ENDPOINT;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.SEARCH_ENDPOINT;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.FAILED_TO_RETRIEVE_REPORT_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.INVALID_JSON_PAYLOAD_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.MISCONFIGURED_FOOD_SERVICE_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.NOT_WELL_DOCUMENTED_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.NO_INFO_IN_THE_CACHE_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.NO_MATCHING_FOODS_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.SERVER_UNABLE_ERROR_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.TRY_AGAIN_LATER_MSG;
import static bg.sofia.uni.fmi.mjt.server.utility.UrlUtil.buildUrl;
import static bg.sofia.uni.fmi.mjt.server.utility.UrlUtil.encodeTokens;
import static bg.sofia.uni.fmi.mjt.server.utility.UrlUtil.getApiKey;

/**
 * Default implementation of the {@link FoodService} interface that handles business logic
 * related to retrieving and processing food data from an external API.
 * This class constructs appropriate API URLs, sends HTTP requests via {@link HttpService},
 * and parses JSON responses into DTOs. It leverages a {@link CacheService} to store
 * and retrieve food reports, reducing redundant external API calls.
 *
 * @throws ConfigurationException if there is a misconfiguration during construction like null properties
 */
public final class FoodServiceImpl implements FoodService {
    private final HttpService httpService;
    private final CacheService fileCacheService;

    private final ObjectMapper mapper = new ObjectMapper();

    public FoodServiceImpl(HttpService httpService, CacheService fileCacheService) {
        if (httpService == null || fileCacheService == null) {
            throw new ConfigurationException(MISCONFIGURED_FOOD_SERVICE_MSG);
        }
        this.httpService = httpService;
        this.fileCacheService = fileCacheService;
    }

    @Override
    public SearchApiResponseDto searchFood(String[] keywords) throws ApiException {
        String bodyResponse = fetchSearchResultsFromApi(keywords);
        SearchApiResponseDto responseDto = parseAndValidateSearchResult(bodyResponse);

        try {
            cacheResponse(responseDto);
        } catch (CacheException e) {
            System.err.println("Warning: Cache service failed. Some operations may not work optimally.");
        }

        return responseDto;
    }

    @Override
    public ReportFoodItemDto getFoodReport(String fdcId) throws ApiException {
        String bodyResponse = getReport(fdcId);

        return parseAndValidateReport(bodyResponse);
    }

    @Override
    public ReportFoodItemDto getFoodByBarcode(String gtinUpc) throws ApiException {
        String id = fileCacheService.getIdFromIndex(gtinUpc);
        if (id == null) {
            throw new FoodItemNotFoundException(null, NO_INFO_IN_THE_CACHE_MSG);
        }

        return getFoodReport(id);
    }

    private String getReport(String fdcId) throws ApiException {
        try {
            return fileCacheService.getById(fdcId, () -> fetchReportFromApi(fdcId));
        } catch (IOException e) {
            throw new ApiServiceUnavailableException(FAILED_TO_RETRIEVE_REPORT_MSG,
                SERVER_UNABLE_ERROR_MSG + TRY_AGAIN_LATER_MSG, e);
        }
    }

    private String fetchSearchResultsFromApi(String[] keywords) throws ApiException {
        String queryParams = encodeTokens(keywords);
        String url = buildUrl(SEARCH_ENDPOINT, API_KEY_PARAM, getApiKey(), QUERY_PARAM, queryParams);

        return httpService.get(url);
    }

    private String fetchReportFromApi(String fdcId) throws ApiException {
        String url = buildUrl(REPORT_ENDPOINT, fdcId, API_KEY_PARAM, getApiKey());

        try {
            return httpService.get(url);
        } catch (FoodItemNotFoundException e) {
            throw new FoodItemNotFoundException(null, NO_MATCHING_FOODS_MSG + fdcId);
        }
    }

    private ReportFoodItemDto parseAndValidateReport(String json) throws ApiException {
        try {
            ReportFoodItemDto item = mapper.readValue(json, ReportFoodItemDto.class);

            if (item.getDescription() == null) {
                throw new FoodItemNotFoundException(null, NOT_WELL_DOCUMENTED_MSG);
            }

            return item;
        } catch (JsonProcessingException e) {
            throw new MalformedRequestBodyException(INVALID_JSON_PAYLOAD_MSG + e.getMessage(),
                SERVER_UNABLE_ERROR_MSG + TRY_AGAIN_LATER_MSG, e);
        }
    }

    private SearchApiResponseDto parseAndValidateSearchResult(String json) throws ApiException {
        try {
            return mapper.readValue(json, SearchApiResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new MalformedRequestBodyException(INVALID_JSON_PAYLOAD_MSG + e.getMessage(),
                SERVER_UNABLE_ERROR_MSG + TRY_AGAIN_LATER_MSG, e);
        }
    }

    private void cacheResponse(SearchApiResponseDto responseDto) throws CacheException {
        Map<String, String> batch = new HashMap<>();
        for (SearchFoodItemDto itemDto : responseDto.getFoods()) {
            String id = itemDto.getFdcId();
            String gtinUpc = itemDto.getGtinUpc();
            if (gtinUpc != null && id != null) {
                batch.put(gtinUpc, id);
            }
        }
        fileCacheService.addToIndex(batch);
    }
}