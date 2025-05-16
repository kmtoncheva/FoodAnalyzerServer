package bg.sofia.uni.fmi.mjt.server.exceptions.api;

public abstract class ApiException extends Exception {
    private final String clientMessage;

    public ApiException(String logMessage, String clientMessage) {
        super(logMessage);
        this.clientMessage = clientMessage;
    }

    public ApiException(String logMessage, String clientMessage, Throwable cause) {
        super(logMessage, cause);
        this.clientMessage = clientMessage;
    }

    public String getClientMessage() {
        return clientMessage;
    }
}