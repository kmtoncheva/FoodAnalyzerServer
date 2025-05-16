package bg.sofia.uni.fmi.mjt.server.exceptions.api;

/*
/v1/food/{fdcId} → 404

/v1/foods/search → 200 с "foods": []
 */
public class FoodItemNotFoundException extends ApiException{

    public FoodItemNotFoundException(String logMessage, String clientMessage) {
        super(logMessage, clientMessage);
    }

    public FoodItemNotFoundException(String logMessage, String clientMessage, Throwable cause) {
        super(logMessage, clientMessage, cause);
    }
}