package bg.sofia.uni.fmi.mjt.server.exceptions.api;

/**
 * Exception indicating that the requested food item could not be found in the USDA FoodData Central API.
 * <p>
 * This may occur in the following scenarios:
 * <ul>
 *     <li>{@code GET /v1/food/{fdcId}} returns a 404 Not Found status.</li>
 *     <li>{@code POST /v1/foods/search} returns a 200 OK response with an empty {@code "foods": []} list.</li>
 * </ul>
 * Contains both a log message for internal diagnostics and a client message for user-facing feedback.
 */
public class FoodItemNotFoundException extends ApiException{

    public FoodItemNotFoundException(String logMessage, String clientMessage) {
        super(logMessage, clientMessage);
    }

    public FoodItemNotFoundException(String logMessage, String clientMessage, Throwable cause) {
        super(logMessage, clientMessage, cause);
    }
}