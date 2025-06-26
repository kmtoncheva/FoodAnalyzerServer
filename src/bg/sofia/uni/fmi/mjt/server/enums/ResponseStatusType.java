package bg.sofia.uni.fmi.mjt.server.enums;

/**
 * Enum representing the types of responses the server can send to the client.
 * <p>
 * Each type indicates the result of a client command execution:
 * <ul>
 *     <li>{@link #OK} – The request was successful and may include one or more food items.<li/>
 *     <li>{@link #ERROR} – The request failed due to a client or server-side issue.
 *     The {@code message} will contain an explanation of the failure.</li>
 *     <li>{@link #NOT_FOUND} – The request was valid, but the searched item (by name or ID) was not found.<li/>
 * </ul>
 */

public enum ResponseStatusType {
    OK,
    ERROR,
    NOT_FOUND
}