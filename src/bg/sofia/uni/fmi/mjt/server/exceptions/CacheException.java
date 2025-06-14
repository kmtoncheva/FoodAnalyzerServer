package bg.sofia.uni.fmi.mjt.server.exceptions;

/**
 * Checked exception indicating that a cache directory cannot be initialized.
 */
public class CacheException extends Exception {
    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}