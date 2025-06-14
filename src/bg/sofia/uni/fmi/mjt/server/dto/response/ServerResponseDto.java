package bg.sofia.uni.fmi.mjt.server.dto.response;

import bg.sofia.uni.fmi.mjt.server.enums.ResponseStatusType;
import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.server.constants.FoodDataContsants.FIRST_ITEM_IND;

public final class ServerResponseDto<T> {
    private final ResponseStatusType status; // "ok" or "error" or "not_found"
    private final String message;
    private final List<FoodItemDto> foods;

    public static ServerResponseDto ok(List<FoodItemDto> foods) {
        return new ServerResponseDto(ResponseStatusType.OK, null, foods);
    }

    public static ServerResponseDto error(String message) {
        return new ServerResponseDto(ResponseStatusType.ERROR, message, null);
    }

    public static ServerResponseDto notFound(String message) {
        return new ServerResponseDto(ResponseStatusType.NOT_FOUND, message, null);
    }

    public String getResponseType() {
        return foods.get(FIRST_ITEM_IND).getFoodItem();
    }

    private ServerResponseDto(ResponseStatusType status, String message, List<FoodItemDto> foods) {
        this.status = status;
        this.message = message;
        this.foods = foods;
    }
}