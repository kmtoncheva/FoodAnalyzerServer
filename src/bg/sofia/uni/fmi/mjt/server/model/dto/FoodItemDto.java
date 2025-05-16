package bg.sofia.uni.fmi.mjt.server.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FoodItemDto(String description, String fdcId, String gtinUpc) {
}