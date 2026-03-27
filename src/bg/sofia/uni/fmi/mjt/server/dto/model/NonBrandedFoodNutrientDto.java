package bg.sofia.uni.fmi.mjt.server.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a single nutrient entry from the foodNutrients array.
 * Used for non-branded foods that have lab-analyzed nutrient data.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NonBrandedFoodNutrientDto {
    private String type;
    private NutrientInfo nutrient;
    private Double amount;

    public NonBrandedFoodNutrientDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NutrientInfo getNutrient() {
        return nutrient;
    }

    public void setNutrient(NutrientInfo nutrient) {
        this.nutrient = nutrient;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Nested class representing nutrient metadata.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NutrientInfo {
        private Integer id;
        private String number;
        private String name;
        private Integer rank;
        private String unitName;

        public NutrientInfo() {
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }
    }
}