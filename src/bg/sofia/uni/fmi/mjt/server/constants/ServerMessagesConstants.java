package bg.sofia.uni.fmi.mjt.server.constants;

import com.beust.ah.A;

public final class ServerMessagesConstants {
    // common messages
    public static final String SERVER_STARTED_MSG = "Server started and listening on port ";
    public static final String SEE_LOGS_MSG = " See logs for details.";
    public static final String CLIENT_DISCONNECTED_MSG = "Client has disconnected.";
    public static final String TRY_AGAIN_LATER_MSG = "Please try again later.";
    public static final String CHECK_AND_TRY_LATER_MSG = "Please, check and try again later.";

    // server error messages
    public static final String ERROR_PROCESSING_CLIENT_REQ_MSG = "Error while processing client request.";
    public static final String ERROR_PARSING_CMD = "Unexpected error while parsing command";
    public static final String SERVER_ERROR_LOGS_MSG = "Fatal server error during startup.";
    public static final String SERVER_FAILED_TO_START_ERROR_MSG = "Server failed to start.";
    public static final String SERVER_UNABLE_ERROR_MSG = "Server unable to process your request. ";
    public static final String ERROR_CLOSING_CONNECTION_MSG = "Error closing client connection.";

    // commands error messages
    public static final String UNKNOWN_CMD_MSG = "Received unknown command from client: ";
    public static final String SERVER_FAILED_TO_RECOGNIZE_CMD_MSG = "Server failed to recognize the command. ";
    public static final String INVALID_CMD_FORMAT =  "Invalid command format: ";
    public static final String INVALID_ARG_TYPE = "Invalid argument type: ";

    // client requests error messages
    public static final String NO_MATCHING_FOODS_MSG = "No food items matched your search query: ";
    public static final String NO_INFO_IN_THE_CACHE_MSG = "There is no information about this food in the cache.";
    public static final String INVALID_JSON_PAYLOAD_MSG = "Invalid JSON payload: ";
    public static final String NOT_WELL_DOCUMENTED_MSG =
        "The provided ID corresponds to a food that is not properly documented. We apologize for the inconvenience.";
    public static final String DISAPPROVED_REQUEST = "Invalid request format or unexpected internal error. ";
    public static final String IMAGE_NOT_FOUND_MSG = "Provided image not found.";
    public static final String NO_BARCODE_FOUND_ERROR_MSG = "No barcode found in the image.";
    public static final String DECODING_IMAGE_ERROR_MSG =
        "Unexpected error while reading or decoding the barcode from image: ";

    // http requests error messages
    public static final String FAILED_TO_RETRIEVE_REPORT_MSG =
        "Failed to retrieve report due to I/O error while accessing API.";
    public static final String BAD_RQST_TO_API_MSG = "Bad request sent to external API: ";
    public static final String INVALID_RQST_PARAMETERS_MSG =  "Your request parameters are invalid. ";
    public static final String HTTP_RQST_EXCEOTION_MSG = "IOException occurred during HTTP request: ";
    public static final String UNABLE_TO_CONNECT_TO_API_MSG = "Unable to connect to FoodData Central API. ";
    public static final String INTERRUPTED_RQST_MSG = "Request was interrupted: ";

    // configuration error messages
    public static final String MISCONFIGURED_COMMAND_FACTORY_MSG = "FoodService must not be null.";
    public static final String MISCONFIGURED_FOOD_SERVICE_MSG = "Http Service and Cache Service must not be null.";
    public static final String MISCONFIGURED_FOOD_SERVER_MSG = "Command Factory must not be null.";
    public static final String MISCONFIGURED_HTTP_SERVICE_MSG = "Http Client must not be null.";
    public static final String CACHE_FAILED_WARNING_MSG =
        "Warning: Cache service failed. Some operations may not work optimally.";

    private ServerMessagesConstants() {}
}
