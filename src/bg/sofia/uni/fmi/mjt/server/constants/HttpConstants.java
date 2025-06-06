package bg.sofia.uni.fmi.mjt.server.constants;

public final class HttpConstants {
    public static final String BASE_URL = "https://api.nal.usda.gov/fdc/v1";
    public static final String SEARCH_ENDPOINT = "/foods/search";
    public static final String REPORT_ENDPOINT = "/food/";
    public static final String QUERY_PARAM = "&query=";
    public static final String API_KEY_NAME = "USDA_API_KEY";
    public static final String API_KEY_PARAM = "?api_key=";

    public static final String DELIMITER = " ";
    public static final String TARGET_DELIMITER = "+";
    public static final String REPLACEMENT_DELIMITER = "%20";

    private HttpConstants() {}
}