package bg.sofia.uni.fmi.mjt.server.model.dto;

import bg.sofia.uni.fmi.mjt.server.model.enums.ResponseStatusType;
import com.google.gson.Gson;

public final class ServerResponseDto {
    private final ResponseStatusType status; // "ok" or "error" or "not_found"
    private final String message;

    private static final Gson gson = new Gson();

    public String toJson() {
        return gson.toJson(this);
    }

    public static ServerResponseDto ok(String message) {
        return new ServerResponseDto(ResponseStatusType.OK, message);
    }

    public static ServerResponseDto error(String message) {
        return new ServerResponseDto(ResponseStatusType.ERROR, message);
    }
    public static ServerResponseDto notFound(String message) {
        return new ServerResponseDto(ResponseStatusType.NOT_FOUND, message);
    }

    private ServerResponseDto(ResponseStatusType status, String message) {
        this.status = status;
        this.message = message;
    }
}