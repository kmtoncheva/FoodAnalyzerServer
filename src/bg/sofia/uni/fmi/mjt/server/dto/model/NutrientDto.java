package bg.sofia.uni.fmi.mjt.server.dto.model;

import java.util.Objects;

/**
 * Represents a single nutrient value as part of a nutrition label.
 * This DTO is commonly used for serialization and deserialization of nutrient data in JSON format.
 */
public class NutrientDto {
    private Float value;
    public NutrientDto() {}

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NutrientDto that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}