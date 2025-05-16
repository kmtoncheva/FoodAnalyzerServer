package bg.sofia.uni.fmi.mjt.server.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FoodItem(String description, String fdcId, String gtinUpc) {
}