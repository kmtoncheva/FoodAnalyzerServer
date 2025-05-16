package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.model.dto.BarcodeRequestDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import bg.sofia.uni.fmi.mjt.server.service.FoodServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.ARGS_TITLE;
import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.COMMAND_TITLE;
import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.GET_FOOD_BY_BARCODE_CMD;
import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.GET_FOOD_CMD;
import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.GET_FOOD_REPORT_CMD;
import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.QUIT_CMD;

public final class CommandFactory {
    private static final Gson gson = new Gson();
    private static final FoodService foodService = new FoodServiceImpl();

    public static Command create(String json) throws InvalidCommandException {
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
                BarcodeRequestDto params = gson.fromJson(obj.get(ARGS_TITLE), BarcodeRequestDto.class);
                return new GetFoodByBarcodeCommand(params);
            case QUIT_CMD:
                return new QuitCommand();
            default:
                throw new InvalidCommandException(commandName);
        }
    }

    private CommandFactory() {
    }
}