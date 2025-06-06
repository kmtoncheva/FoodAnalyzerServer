package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.BarcodeDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.ARGS_TITLE;
import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.COMMAND_TITLE;
import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.GET_FOOD_BY_BARCODE_CMD;
import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.GET_FOOD_CMD;
import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.GET_FOOD_REPORT_CMD;
import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.QUIT_CMD;

/**
 * A factory class responsible for creating {@link Command} instances
 * based on a JSON-encoded client request.
 * The input JSON must contain a {@code "command"} field and an {@code "args"} field,
 * where the format and type of {@code "args"} depends on the specific command.
 * <p>
 * Supported commands include:
 * {@code "get-food"} - expects {@code args} as a JSON array of strings
 * {@code "get-food-report"} - expects {@code args} as a single string
 * {@code "get-food-by-barcode"} - expects {@code args} as a {@link BarcodeDto} JSON object
 * {@code "quit"} - no arguments required
 * If an unrecognized command is provided, an {@link InvalidCommandException} is thrown.
 */
public final class CommandFactory {
    private static final Gson gson = new Gson();
    private static FoodService foodService;

    /**
     * Constructs a new {@code CommandFactory} with the specified {@link FoodService} instance.
     * This constructor allows for dependency injection of the service used by commands that
     * require access to external food-related APIs or some other business logic.
     *
     * @param foodService the {@link FoodService} implementation to be used by commands created by this factory;
     *                    must not be {@code null}
     */
    public CommandFactory(FoodService foodService) {
        this.foodService = foodService;
    }

    /**
     * Parses a JSON-formatted string to create a corresponding {@link Command} instance.
     *
     * @param json the input JSON string containing the command type and arguments
     * @return a concrete {@link Command} instance corresponding to the parsed input
     * @throws InvalidCommandException if the command type is not recognized or improperly formatted
     * @throws JsonSyntaxException     if the JSON is invalid
     * @throws IllegalStateException   or {@link NullPointerException} if expected fields are missing
     */
    public Command create(String json) throws InvalidCommandException {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        String commandName = obj.get(COMMAND_TITLE).getAsString();

        switch (commandName) {
            case GET_FOOD_CMD:
                String[] foodArgs = gson.fromJson(obj.get(ARGS_TITLE), String[].class);
                return new GetFoodCommand(foodArgs, foodService);
            case GET_FOOD_REPORT_CMD:
                String reportArg = gson.fromJson(obj.get(ARGS_TITLE), String.class);
                return new GetFoodReportCommand(reportArg, foodService);
            case GET_FOOD_BY_BARCODE_CMD:
                BarcodeDto params = gson.fromJson(obj.get(ARGS_TITLE), BarcodeDto.class);
                return new GetFoodByBarcodeCommand(params);
            case QUIT_CMD:
                return new QuitCommand();
            default:
                throw new InvalidCommandException(commandName);
        }
    }
}