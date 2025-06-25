package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.LabelNutrientsDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.NutrientDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.BarcodeReaderException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import bg.sofia.uni.fmi.mjt.server.service.FoodServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetFoodReportCommandTest {
    String fcdId = "id";
    FoodService foodService = mock(FoodServiceImpl.class);

    Command command = new GetFoodReportCommand(fcdId, foodService);

    @Test
    void testExecuteWithFood() throws ApiException, BarcodeReaderException {
        NutrientDto calories = new NutrientDto();
        calories.setValue(250.0f);
        NutrientDto protein = new NutrientDto();
        protein.setValue(10.0f);
        NutrientDto fiber = new NutrientDto();
        fiber.setValue(5.0f);

        LabelNutrientsDto labelNutrients = new LabelNutrientsDto();
        labelNutrients.setCalories(calories);
        labelNutrients.setProtein(protein);
        labelNutrients.setFiber(fiber);

        ReportFoodItemDto reportFoodItem = new ReportFoodItemDto();
        reportFoodItem.setDescription("Grilled Chicken Sandwich");
        reportFoodItem.setIngredients("Chicken breast, bread, lettuce, tomato, mayo");
        reportFoodItem.setLabelNutrients(labelNutrients);

        when(foodService.getFoodReport(fcdId)).thenReturn(reportFoodItem);

        Assertions.assertEquals(command.execute().size(), 1, "Only one item should be added to the list.");
        Assertions.assertEquals(command.execute().get(0), reportFoodItem,
            "Should return the same items returned from the service.");
    }

    @Test
    void testExecuteWithApiException() throws ApiException {
        when(foodService.getFoodReport(fcdId)).thenThrow(
            new FoodItemNotFoundException("No food item", "No food item"));

        ApiException exception = assertThrows(ApiException.class, () -> {
            command.execute();
        });
        assertEquals("No food item", exception.getMessage());
    }
}