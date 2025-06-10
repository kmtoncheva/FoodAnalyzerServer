package bg.sofia.uni.fmi.mjt.server.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.server.constants.FoodDataContsants.REPORT_FOOD_ITEM;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportFoodItemDto implements FoodItemDto{
    private String description;
    private String ingredients;
    private LabelNutrientsDto labelNutrients;

    public ReportFoodItemDto() {
    }

    public ReportFoodItemDto(String description, String ingredients, LabelNutrientsDto labelNutrients) {
        this.description = description;
        this.ingredients = ingredients;
        this.labelNutrients = labelNutrients;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public LabelNutrientsDto getLabelNutrients() {
        return labelNutrients;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setLabelNutrients(LabelNutrientsDto labelNutrients) {
        this.labelNutrients = labelNutrients;
    }

    @Override
    public String getFoodItem() {
        return REPORT_FOOD_ITEM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportFoodItemDto)) return false;
        ReportFoodItemDto that = (ReportFoodItemDto) o;
        return Objects.equals(description, that.description) &&
            Objects.equals(ingredients, that.ingredients) &&
            Objects.equals(labelNutrients, that.labelNutrients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, ingredients, labelNutrients);
    }
}