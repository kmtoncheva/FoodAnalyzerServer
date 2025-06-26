package bg.sofia.uni.fmi.mjt.server.constants;

/**
 *  Defines constant values related to command parsing and handling.
 *  Used to standardize the structure and recognized commands in the system.
 */
public final class CommandConstants {
    public static final String COMMAND_TITLE = "command";
    public static final String ARGS_TITLE = "args";
    public static final String GET_FOOD_CMD = "get-food";
    public static final String GET_FOOD_REPORT_CMD = "get-food-report";
    public static final String GET_FOOD_BY_BARCODE_CMD = "get-food-by-barcode";
    public static final String QUIT_CMD = "quit";

    public static final String LINE_BREAK = "\n";

    private CommandConstants() {}
}