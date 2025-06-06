package bg.sofia.uni.fmi.mjt.server.dto.response;

import bg.sofia.uni.fmi.mjt.server.dto.model.SearchFoodItemDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchApiResponseDto {
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