package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;

import java.util.List;

/**
 * A command implementation that retrieves a report for a single food based on a unique ID using a {@link FoodService}.
 */
public final class GetFoodReportCommand implements Command {
    private String fcdId;
    private final FoodService foodService;

    /**
     * Constructs a {@code GetFoodReportCommand} with the given fcdId and food service.
     *
     * @param fcdId       a unique ID
     * @param foodService the service responsible for making API calls to retrieve food information
     */
    public GetFoodReportCommand(String fcdId, FoodService foodService) {
        this.fcdId = fcdId;
        this.foodService = foodService;
    }

    /**
     * Executes the command to search for the food item based on the provided id.
     * This method delegates the search request to the {@link FoodService}, which
     * returns a parsed {@link ReportFoodItemDto}.
     *
     * If no matching item is found a {@link FoodItemNotFoundException} will be thrown on a service layer.
     *
     * @return the {@link ReportFoodItemDto} object wrapped in a List
     * @throws ApiException from the service layer if no matching food is found or some other problem occurs
     */
    @Override
    public List<FoodItemDto> execute() throws ApiException {
        ReportFoodItemDto foodItem = foodService.getFoodReport(fcdId);

        return List.of(foodItem);
    }
}