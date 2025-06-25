package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.SearchFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.response.SearchApiResponseDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.BarcodeReaderException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiServiceUnavailableException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import bg.sofia.uni.fmi.mjt.server.service.FoodServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetFoodCommandTest {
    String[] tokens = {"raffaello", "treat"};
    FoodService foodService = mock(FoodServiceImpl.class);

    Command command = new GetFoodCommand(tokens, foodService);

    @Test
    void testExecuteWithListOfFoods() throws ApiException, BarcodeReaderException {
        SearchFoodItemDto item1 = new SearchFoodItemDto("abc", "description1", "123");
        SearchFoodItemDto item2 = new SearchFoodItemDto("def", "description2", "456");
        SearchApiResponseDto responseDto = new SearchApiResponseDto(List.of(item1, item2));

        when(foodService.searchFoodByKeywords(tokens)).thenReturn(responseDto);

        Assertions.assertEquals(command.execute().get(0), responseDto.getFoods().get(0),
            "Should return the same items returned from the service.");
        Assertions.assertEquals(command.execute().get(1), responseDto.getFoods().get(1),
            "Should return the same items returned from the service.");
    }

    @Test
    void testExecuteWithEmptyList() throws ApiException {
        SearchApiResponseDto responseDto = new SearchApiResponseDto(List.of());

        when(foodService.searchFoodByKeywords(tokens)).thenReturn(responseDto);

        Assertions.assertThrows(FoodItemNotFoundException.class, () -> {
            command.execute();
        }, "Should throw an exception when no foods match the tokens from the client.");
    }

    @Test
    void testExecuteWithApiException() throws ApiException {
        when(foodService.searchFoodByKeywords(tokens)).thenThrow(
            new ApiServiceUnavailableException("API failure", "API failure"));

        ApiException exception = assertThrows(ApiException.class, () -> {
            command.execute();
        });
        assertEquals("API failure", exception.getMessage());
    }
}