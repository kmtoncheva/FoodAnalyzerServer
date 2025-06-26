package bg.sofia.uni.fmi.mjt.server.exceptions;

/**
 * Exception thrown to indicate that a received command is invalid or improperly formatted.
 * <p>
 * This may occur when the command name is unrecognized, required arguments are missing,
 * or the arguments are of the wrong type or format.
 */
public class InvalidCommandException extends Exception{
    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}