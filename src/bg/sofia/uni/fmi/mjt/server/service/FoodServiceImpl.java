package bg.sofia.uni.fmi.mjt.server.service;

import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.http.HttpService;
import bg.sofia.uni.fmi.mjt.server.http.HttpServiceImpl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.API_KEY_NAME;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.API_KEY_PARAM;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.BASE_URL;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.DELIMITER;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.QUERY_PARAM;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.REPLACEMENT_DELIMITER;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.SEARCH_ENDPOINT;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.TARGET_DELIMITER;

public class FoodServiceImpl implements FoodService {
    private final HttpService httpService;

    public FoodServiceImpl() {
        this.httpService = new HttpServiceImpl();
    }

    // for get-food <search-keywords> command
    @Override
    public String searchFood(String[] keywords) throws ApiException {
        String queryParams = encodeTokens(keywords);
        String apiKey = getApiKey();

        String url = BASE_URL + SEARCH_ENDPOINT + API_KEY_PARAM + apiKey + QUERY_PARAM + queryParams;

        return httpService.get(url);
    }

    @Override
    public String getFoodReport(String fdcId) {
        return "";
    }

    private static String encodeTokens(String[] keywords) {
        String joined = String.join(DELIMITER, keywords);
        return URLEncoder.encode(joined, StandardCharsets.UTF_8).replace(TARGET_DELIMITER, REPLACEMENT_DELIMITER);
    }

    private static String getApiKey() {
        return System.getenv(API_KEY_NAME);
    }
}