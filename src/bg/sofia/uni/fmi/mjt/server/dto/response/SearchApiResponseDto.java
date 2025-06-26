package bg.sofia.uni.fmi.mjt.server.dto.response;

import bg.sofia.uni.fmi.mjt.server.dto.model.SearchFoodItemDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * A Data Transfer Object (DTO) that encapsulates the response from the USDA FoodData Central API
 * when executing the {@code get-food <food name>} command.
 * <p>
 * Contains a list of {@link SearchFoodItemDto} objects representing matching food items
 * based on the search query.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SearchApiResponseDto {
    List<SearchFoodItemDto> foods;

    public List<SearchFoodItemDto> getFoods() {
        return foods;
    }

    public SearchApiResponseDto(List<SearchFoodItemDto> foods) {
        this.foods = foods;
    }

    public SearchApiResponseDto() {
        foods = new ArrayList<>();
    }

    public void setFoods(List<SearchFoodItemDto> foods) {
        this.foods = foods;
    }
}