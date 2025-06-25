package bg.sofia.uni.fmi.mjt.server.service.http;

import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;

/**
 * A service interface for making HTTP GET requests to external APIs.
 * <p>
 * Implementations of this interface should handle communication with external services
 * and manage potential errors such as invalid requests, unavailable services, or missing data.
 */
public interface HttpService {
    /**
     * Sends an HTTP GET request to the specified URL and returns the response body as a string.
     *
     * @param url the URL to send the GET request to.
     * @return the body of the HTTP response as a {@code String}.
     * @throws ApiException if an error occurs during the request or if the response indicates a failure.
     */
    String get(String url) throws ApiException;
}