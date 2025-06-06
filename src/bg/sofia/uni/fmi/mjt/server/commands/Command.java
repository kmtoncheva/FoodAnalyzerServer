package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;

import java.util.List;

/**
 * Represents a command that can be executed on the server side, typically in response to a client request.
 * Each implementation of this interface should encapsulate the logic for handling a specific type of operation,
 * such as retrieving a report or performing a search.
 * <p>
 * This interface is generic and can be used with any type that extends {@link FoodItemDto}.
 *
 * @param <T> the type of {@link FoodItemDto} returned in the result list;
 *            for example, a single item from a report retrieval or multiple items from a search operation
 */
public interface Command<T extends FoodItemDto> {
    /**
     * Executes the command and returns a list of results.
     * Implementations may interact with external APIs or internal services to retrieve the required data.
     *
     * @return a list of {@code T} representing the result of the command execution;
     * may return {@code null} for terminating commands like "quit"
     * @throws ApiException if an error occurs during execution, such as an external API failure,
     *                      invalid input, or no matching items found (to enable uniform error handling)
     */
    List<T> execute() throws ApiException;

    /**
     * Indicates whether the command should terminate the client-server session.
     *
     * @return {@code true} if the command is a terminating command (e.g., "quit"); {@code false} otherwise
     */
    default boolean isTerminatingCommand() {
        return false;
    }
}