package bg.sofia.uni.fmi.mjt.server.utility;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.API_KEY_NAME;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.BASE_URL;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.DELIMITER;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.REPLACEMENT_DELIMITER;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.TARGET_DELIMITER;

public class UrlUtil {
    public static String buildUrl(String... parts) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        for (String part : parts) {
            urlBuilder.append(part);
        }
        return urlBuilder.toString();
    }

    public static String encodeTokens(String[] keywords) {
        String joined = String.join(DELIMITER, keywords);
        return URLEncoder.encode(joined, StandardCharsets.UTF_8).replace(TARGET_DELIMITER, REPLACEMENT_DELIMITER);
    }

    public static String getApiKey() {
        return System.getenv(API_KEY_NAME);
    }
}
