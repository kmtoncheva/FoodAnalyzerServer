package bg.sofia.uni.fmi.mjt.server.dto.response;

import bg.sofia.uni.fmi.mjt.server.enums.ResponseStatusType;
import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;

import java.util.List;

/**
 * A Data Transfer Object (DTO) representing the response returned by the server
 * after executing a client command.
 * <p>
 * The response may contain a list of food items, a status indicating the result of the operation,
 * and an optional message in case of an error or a "not found" condition.
 * <p>
 * This object is immutable and should be constructed only via the provided factory methods.
 */
public final class ServerResponseDto {
    private final ResponseStatusType status;
    private final String message;
    private final List<FoodItemDto> foods;

    /**
     * Factory method for creating a successful server response, indicating the operation
     * has found the searched items.
     * The status is set to {@code OK} and no message is included.
     *
     * @param foods the list of food items successfully retrieved from the API
     * @return a response containing the food items
     */
    public static ServerResponseDto ok(List<FoodItemDto> foods) {
        return new ServerResponseDto(ResponseStatusType.OK, null, foods);
    }

    /**
     * Factory method for creating an error server response, indicating the operation
     * was interrupted due to a client-server error.
     * The explanation of the error is sent to the client via the message.
     *
     * @param message an explanation of the error that caused the failure
     * @return a response containing the error message
     */
    public static ServerResponseDto error(String message) {
        return new ServerResponseDto(ResponseStatusType.ERROR, message, null);
    }

    /**
     * Factory method for creating a server response when no food items
     * match the search criteria.
     *
     * @param message a detailed explanation why the items were not found
     * @return a response containing the explanation message
     */
    public static ServerResponseDto notFound(String message) {
        return new ServerResponseDto(ResponseStatusType.NOT_FOUND, message, null);
    }

    private ServerResponseDto(ResponseStatusType status, String message, List<FoodItemDto> foods) {
        this.status = status;
        this.message = message;
        this.foods = foods;
    }
}