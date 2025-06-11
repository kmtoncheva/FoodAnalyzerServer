package bg.sofia.uni.fmi.mjt.server.exceptions;

/**
 * Checked exception indicating that a cache directory cannot be initialized.
 */
public class CacheInitializationException extends Exception {
    public CacheInitializationException(String message) {
        super(message);
    }

    public CacheInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}