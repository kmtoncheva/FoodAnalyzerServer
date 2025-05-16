package bg.sofia.uni.fmi.mjt.server.exceptions.api;

/*
 * логика: „инфраструктурен“ проблем → не можем да достигнем услугата
 */
public class ApiServiceUnavailableException extends ApiException {
    public ApiServiceUnavailableException(String logMessage, String clientMessage) {
        super(logMessage, clientMessage);
    }

    public ApiServiceUnavailableException(String logMessage, String clientMessage, Throwable cause) {
        super(logMessage, clientMessage, cause);
    }
}