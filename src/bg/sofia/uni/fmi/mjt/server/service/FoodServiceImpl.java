package bg.sofia.uni.fmi.mjt.server.service;

import bg.sofia.uni.fmi.mjt.server.cache.CacheService;
import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.response.SearchApiResponseDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiServiceUnavailableException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.MalformedRequestBodyException;
import bg.sofia.uni.fmi.mjt.server.http.HttpService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.API_KEY_PARAM;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.QUERY_PARAM;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.REPORT_ENDPOINT;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.SEARCH_ENDPOINT;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.INVALID_JSON_PAYLOAD_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.NOT_WELL_DOCUMENTED_MSG;
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
 */
public final class FoodServiceImpl implements FoodService {
    private final HttpService httpService;
    private final CacheService fileCacheService;

    private final ObjectMapper mapper = new ObjectMapper();

    public FoodServiceImpl(HttpService httpService, CacheService fileCacheService) {
        this.httpService = httpService;
        this.fileCacheService = fileCacheService;
    }

    @Override
    public SearchApiResponseDto searchFood(String[] keywords) throws ApiException {
        String bodyResponse = fetchSearchResultsFromApi(keywords);

        return parseAndValidateSearchResult(bodyResponse);
    }

    @Override
    public ReportFoodItemDto getFoodReport(String fdcId) throws ApiException {
        String bodyResponse = getReport(fdcId);

        return parseAndValidateReport(bodyResponse);
    }

    private String getReport(String fdcId) throws ApiException {
        try {
            return fileCacheService.get(fdcId, () -> fetchReportFromApi(fdcId));
        } catch (IOException e) {
            throw new ApiServiceUnavailableException(SERVER_UNABLE_ERROR_MSG, TRY_AGAIN_LATER_MSG, e);
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
}