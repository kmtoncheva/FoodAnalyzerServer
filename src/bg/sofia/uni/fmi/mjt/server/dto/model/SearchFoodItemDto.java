package bg.sofia.uni.fmi.mjt.server.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.server.constants.FoodDataContsants.SEARCH_FOOD_ITEM;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchFoodItemDto implements FoodItemDto {
    private String fdcId;
    private String description;
    private String gtinUpc;

    public SearchFoodItemDto(String fdcId, String description, String gtinUpc) {
        this.fdcId = fdcId;
        this.description = description;
        this.gtinUpc = gtinUpc;
    }

    public SearchFoodItemDto() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchFoodItemDto)) return false;
        SearchFoodItemDto that = (SearchFoodItemDto) o;
        return Objects.equals(fdcId, that.fdcId);
    }

    public String getFdcId() {
        return fdcId;
    }

    public String getDescription() {
        return description;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fdcId);
    }

    @Override
    public String getFoodItem() {
        return SEARCH_FOOD_ITEM;
    }
}