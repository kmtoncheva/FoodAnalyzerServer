package bg.sofia.uni.fmi.mjt.server.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

/**
 * Represents a food item returned as a result of the {@code get-food <food name>} command.
 * <p>
 * Contains a unique FDC ID, a description, and a GTIN/UPC code if available (present only for
 * branded food items). All data is extracted and parsed from the USDA FoodData Central API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class SearchFoodItemDto implements FoodItemDto {
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
        if (!(o instanceof SearchFoodItemDto that)) return false;
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
}