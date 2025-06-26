package bg.sofia.uni.fmi.mjt.server.exceptions.api;

/**
 * Exception indicating that the body of the client's request is malformed or invalid.
 * <p>
 * This exception wraps both a log message for backend diagnostics and a client-facing message to be returned.
 */
public class MalformedRequestBodyException extends ApiException{
    public MalformedRequestBodyException(String logMessage, String clientMessage) {
        super(logMessage, clientMessage);
    }

    public MalformedRequestBodyException(String logMessage, String clientMessage, Throwable cause) {
        super(logMessage, clientMessage, cause);
    }
}