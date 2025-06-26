package bg.sofia.uni.fmi.mjt.server.exceptions.api;

/**
 * Exception indicating that the request sent to the USDA FoodData Central API was invalid and rejected with a 400 Bad Request.
 * <p>
 * This typically occurs when the client request contains malformed or unsupported parameters.
 * <p>
 * This exception wraps both a log message for backend diagnostics and a client-facing message to be returned.
 */
public class InvalidApiRequestException extends ApiException {

    public InvalidApiRequestException(String logMessage, String clientMessage) {
        super(logMessage, clientMessage);
    }

    public InvalidApiRequestException(String logMessage, String clientMessage, Throwable cause) {
        super(logMessage, clientMessage, cause);
    }
}