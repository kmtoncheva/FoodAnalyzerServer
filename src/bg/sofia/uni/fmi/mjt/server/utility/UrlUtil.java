package bg.sofia.uni.fmi.mjt.server.utility;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.API_KEY_NAME;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.BASE_URL;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.DELIMITER;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.REPLACEMENT_DELIMITER;
import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.TARGET_DELIMITER;

/**
 * Utility class for building and encoding URLs used in HTTP requests.
 */
public final class UrlUtil {
    /**
     * Constructs a complete URL string by appending the given path parts.
     *
     * @param parts The individual path segments to be appended to the base URL.
     * @return The complete URL string.
     */
    public static String buildUrl(String... parts) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        for (String part : parts) {
            urlBuilder.append(part);
        }
        return urlBuilder.toString();
    }

    /**
     * Encodes an array of keyword tokens into a single URL-safe string based on API specification.
     *
     * @param keywords An array of keyword strings to be encoded.
     * @return A single encoded string that is used as a URL query parameter.
     */
    public static String encodeTokens(String[] keywords) {
        String joined = String.join(DELIMITER, keywords);
        return URLEncoder.encode(joined, StandardCharsets.UTF_8).replace(TARGET_DELIMITER, REPLACEMENT_DELIMITER);
    }

    /**
     * Retrieves the API key from environment variables.
     *
     * @return The API key as a string, or {@code null} if not set in the environment.
     */
    public static String getApiKey() {
        return System.getenv(API_KEY_NAME);
    }

    private UrlUtil() {
    }
}