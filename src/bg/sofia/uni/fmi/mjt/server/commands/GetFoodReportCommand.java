package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;

import java.util.List;

public class GetFoodReportCommand implements Command{
    private String fcdId;

    private final FoodService foodService;

    public GetFoodReportCommand(String fcdId, FoodService foodService) {
        this.fcdId = fcdId;
        this.foodService = foodService;
    }

    // if there is no food items with the provided id - an exception will be thrown
    // at least *description fields will be present !!!
    @Override
    public List<FoodItemDto> execute() throws ApiException {
        ReportFoodItemDto foodItem = foodService.getFoodReport(fcdId);

        return List.of(foodItem);
    }
}

/*
JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        String commandName = obj.get(COMMAND_TITLE).getAsString();

        switch (commandName) {
            case GET_FOOD_CMD:
                String[] foodArgs = gson.fromJson(obj.get(ARGS_TITLE), String[].class);
                return new GetFoodCommand(foodArgs, foodService);
            case GET_FOOD_REPORT_CMD:
                String reportArg = gson.fromJson(obj.get(ARGS_TITLE), String.class);
                return new GetFoodReportCommand(reportArg);
            case GET_FOOD_BY_BARCODE_CMD:
                BarcodeDto params = gson.fromJson(obj.get(ARGS_TITLE), BarcodeDto.class);
                return new GetFoodByBarcodeCommand(params);
            case QUIT_CMD:
                return new QuitCommand();
            default:
                throw new InvalidCommandException(commandName);
        }
 */


/*
SearchApiResponseDto apiResponseDto = foodService.searchFood(tokens);

        if (apiResponseDto.getFoods().isEmpty()) {
            throw new FoodItemNotFoundException(null,
                NO_MATCHING_FOODS_MSG + String.join(DELIMITER, tokens));
        }

        return apiResponseDto.getFoods();
 */