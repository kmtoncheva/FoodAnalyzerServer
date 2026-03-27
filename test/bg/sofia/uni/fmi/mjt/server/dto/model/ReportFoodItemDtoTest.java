package bg.sofia.uni.fmi.mjt.server.dto.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ReportFoodItemDtoTest {
    @Test
    void testReportFoodItemDtoEqualsWithNull() {
        ReportFoodItemDto dto = new ReportFoodItemDto("desc", null, null);
        assertNotEquals(null, dto);
    }

    @Test
    void testReportFoodItemDtoHashCodeConsistency() {
        ReportFoodItemDto dto1 = new ReportFoodItemDto("desc", "ing", null);
        ReportFoodItemDto dto2 = new ReportFoodItemDto("desc", "ing", null);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}
