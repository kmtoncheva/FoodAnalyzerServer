package bg.sofia.uni.fmi.mjt.server.exceptions.api;

public class MalformedRequestBodyException extends ApiException{
    public MalformedRequestBodyException(String logMessage, String clientMessage) {
        super(logMessage, clientMessage);
    }

    public MalformedRequestBodyException(String logMessage, String clientMessage, Throwable cause) {
        super(logMessage, clientMessage, cause);
    }
}
