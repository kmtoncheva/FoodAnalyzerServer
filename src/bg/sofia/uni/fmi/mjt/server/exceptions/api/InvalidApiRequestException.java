package bg.sofia.uni.fmi.mjt.server.exceptions.api;

/*
 * използва се, когато и /v1/food/{fdcId} и /v1/foods/search върнат 400
 */
public class InvalidApiRequestException extends ApiException {

    public InvalidApiRequestException(String logMessage, String clientMessage) {
        super(logMessage, clientMessage);
    }

    public InvalidApiRequestException(String logMessage, String clientMessage, Throwable cause) {
        super(logMessage, clientMessage, cause);
    }
}