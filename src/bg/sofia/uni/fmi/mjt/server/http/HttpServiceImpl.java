package bg.sofia.uni.fmi.mjt.server.http;

import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiServiceUnavailableException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.InvalidApiRequestException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class HttpServiceImpl implements HttpService{
    /*
    status codes for the two types of get request :
    /v1/food/{fdcId} :                      /v1/foods/search
     1. 200 - one food result               1. 200 - list of foods that matched searched terms, 0-no results found
     2. 400 - bad input parameter           2. 400 - bad input parameter
     3. 404 - no results found
     */
    @Override
    public String get(String url) throws ApiException{
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

        try {
            HttpResponse<String> response = executeRequest(request);

            if(response.statusCode() == 400) {
                throw new InvalidApiRequestException("Bad request sent to external API: " + response.body(),
                    "Your request parameters are invalid. Please check the documentation.");
            }
            if (response.statusCode() == 404) {
                throw new FoodItemNotFoundException(null, null); // nothing to log if food is not present
                // custom messages in the commands
            }

            return response.body();
        }
        catch (IOException e) {
            throw new ApiServiceUnavailableException("IOException occurred during HTTP request: " + e.getMessage(),
                "Unable to connect to external food data service. Please try again later", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupted flag
            throw new ApiServiceUnavailableException("Request was interrupted: " + e.getMessage(),
                "Unable to connect to external food data service. Please try again later", e);
        }
    }

    private static  HttpResponse<String> executeRequest(HttpRequest request) throws IOException, InterruptedException {
        try (HttpClient client = HttpClient.newBuilder().build()) {
           return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
    }
}