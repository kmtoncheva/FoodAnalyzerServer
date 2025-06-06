package bg.sofia.uni.fmi.mjt.server.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents a collection of core nutrient values typically found on a nutrition labels of Branded Foods returned
 * by the external API.
 * Each nutrient is represented as a {@link NutrientDto} containing a numeric value.
 */
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore extra fields
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabelNutrientsDto {
    private NutrientDto calories;
    private NutrientDto protein;
    private NutrientDto fat;
    private NutrientDto carbohydrates;
    private NutrientDto fiber;

    /**
     * Default no-args constructor for JSON deserialization.
     */
    public LabelNutrientsDto() {
    }

    /**
     * Full constructor to initialize all nutrient values.
     *
     * @param calories       the calorie content
     * @param protein        the protein content
     * @param fat            the fat content
     * @param carbohydrates  the carbohydrate content
     * @param fiber          the fiber content
     */
    public LabelNutrientsDto(NutrientDto calories, NutrientDto protein, NutrientDto fat, NutrientDto carbohydrates,
                             NutrientDto fiber) {
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
    }

    public NutrientDto getCalories() {
        return calories;
    }

    public NutrientDto getProtein() {
        return protein;
    }

    public NutrientDto getFat() {
        return fat;
    }

    public NutrientDto getCarbohydrates() {
        return carbohydrates;
    }

    public NutrientDto getFiber() {
        return fiber;
    }

    public void setCalories(NutrientDto calories) {
        this.calories = calories;
    }

    public void setProtein(NutrientDto protein) {
        this.protein = protein;
    }

    public void setFat(NutrientDto fat) {
        this.fat = fat;
    }

    public void setCarbohydrates(NutrientDto carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setFiber(NutrientDto fiber) {
        this.fiber = fiber;
    }
}