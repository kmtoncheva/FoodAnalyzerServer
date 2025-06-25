package bg.sofia.uni.fmi.mjt.server.service.http;

import bg.sofia.uni.fmi.mjt.server.exceptions.ConfigurationException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiServiceUnavailableException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.InvalidApiRequestException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.BAD_RQST_TO_API_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.CHECK_AND_TRY_LATER_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.HTTP_RQST_EXCEOTION_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.INTERRUPTED_RQST_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.INVALID_RQST_PARAMETERS_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.MISCONFIGURED_FOOD_SERVER_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.MISCONFIGURED_HTTP_SERVICE_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.TRY_AGAIN_LATER_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.UNABLE_TO_CONNECT_TO_API_MSG;

/**
 * Implementation of {@link HttpService} that performs HTTP GET requests
 * to an external API using Java's {@link HttpClient}.
 * <p>
 * This service handles response status codes and maps them to appropriate custom exceptions.
 */
public final class HttpServiceImpl implements HttpService {
    private final HttpClient httpClient;

    /**
     * Constructs a new {@code HttpServiceImpl} with the provided {@link HttpClient} instance.
     *
     * @param httpClient the {@link HttpClient} to be used for sending HTTP requests.
     * @throws ConfigurationException if the provided {@code httpClient} is {@code null}.
     */
    public HttpServiceImpl(HttpClient httpClient) {
        if (httpClient == null) {
            throw new ConfigurationException(MISCONFIGURED_HTTP_SERVICE_MSG);
        }
        this.httpClient = httpClient;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Additionally handles specific HTTP response codes:
     * <ul>
     *     <li>400 - throws {@link InvalidApiRequestException}</li>
     *     <li>404 - throws {@link FoodItemNotFoundException}</li>
     *     <li>IO and interruption errors - throw {@link ApiServiceUnavailableException}</li>
     * </ul>
     */
    @Override
    public String get(String url) throws ApiException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

        try {
            HttpResponse<String> response = executeRequest(request);

            if (response.statusCode() == 400) {
                throw new InvalidApiRequestException(BAD_RQST_TO_API_MSG + response.body(),
                    INVALID_RQST_PARAMETERS_MSG + CHECK_AND_TRY_LATER_MSG);
            }
            if (response.statusCode() == 404) {
                throw new FoodItemNotFoundException(null, null); // nothing to log if food is not present
            }

            return response.body();
        } catch (IOException e) {
            throw new ApiServiceUnavailableException(HTTP_RQST_EXCEOTION_MSG + e.getMessage(),
                UNABLE_TO_CONNECT_TO_API_MSG + TRY_AGAIN_LATER_MSG, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupted flag
            throw new ApiServiceUnavailableException(INTERRUPTED_RQST_MSG + e.getMessage(),
                UNABLE_TO_CONNECT_TO_API_MSG + TRY_AGAIN_LATER_MSG, e);
        }
    }

    /**
     * Executes the provided HTTP request and returns the response.
     *
     * @param request the {@link HttpRequest} to be executed.
     * @return the {@link HttpResponse} from the API.
     * @throws IOException          if an I/O error occurs when sending or receiving.
     * @throws InterruptedException if the operation is interrupted.
     */
    private HttpResponse<String> executeRequest(HttpRequest request) throws IOException, InterruptedException {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}