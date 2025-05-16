package bg.sofia.uni.fmi.mjt.server.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GetFoodDto(List<FoodItemDto> foods) {
}