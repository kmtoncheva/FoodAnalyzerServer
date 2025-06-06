package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.SearchFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.response.SearchApiResponseDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.DELIMITER;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.NO_MATCHING_FOODS_MSG;

/**
 * A command implementation that retrieves food items based on search tokens using a {@link FoodService}.
 */
public class GetFoodCommand implements Command<SearchFoodItemDto> {
    private final String[] tokens;

    private final FoodService foodService;

    /**
     * Constructs a {@code GetFoodCommand} with the given search tokens and food service.
     *
     * @param tokens      the array of keywords from the client
     * @param foodService the service responsible for making API calls to retrieve food information
     */
    public GetFoodCommand(String[] tokens, FoodService foodService) {
        this.tokens = tokens;
        this.foodService = foodService;
    }

    /**
     * Executes the command to search for food items based on the provided tokens.
     * <p>
     * This method delegates the search request to the {@link FoodService}, which
     * returns a parsed {@link SearchApiResponseDto}. If no matching food items
     * are found, a {@link FoodItemNotFoundException} is thrown.
     *
     * @return a list of {@link SearchFoodItemDto} representing the found food items
     * @throws ApiException              if there is an error during the search operation,
     *                                   including communication errors or API issues
     * @throws FoodItemNotFoundException if no matching food items are found for the given tokens
     */
    @Override
    public List<SearchFoodItemDto> execute() throws ApiException {
        SearchApiResponseDto apiResponseDto = foodService.searchFood(tokens);

        if (apiResponseDto.getFoods().isEmpty()) {
            throw new FoodItemNotFoundException(null,
                NO_MATCHING_FOODS_MSG + String.join(DELIMITER, tokens));
        }

        return apiResponseDto.getFoods();
    }
}