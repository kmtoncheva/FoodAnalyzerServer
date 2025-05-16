package bg.sofia.uni.fmi.mjt.server.model.dto;

import bg.sofia.uni.fmi.mjt.server.model.enums.ResponseStatusType;
import com.google.gson.Gson;

public final class ServerResponseDto<T> {
    private final ResponseStatusType status; // "ok" or "error" or "not_found"
    private final T message;

        private static final Gson gson = new Gson();

    public String toJson() {
        return gson.toJson(this);
    }

    public static <T> ServerResponseDto ok(T foods) {
        return new ServerResponseDto(ResponseStatusType.OK, foods);
    }

    public static ServerResponseDto error(String message) {
        return new ServerResponseDto(ResponseStatusType.ERROR, message);
    }
    public static ServerResponseDto notFound(String message) {
        return new ServerResponseDto(ResponseStatusType.NOT_FOUND, message);
    }

    private ServerResponseDto(ResponseStatusType status, T message) {
        this.status = status;
        this.message = message;
    }
}