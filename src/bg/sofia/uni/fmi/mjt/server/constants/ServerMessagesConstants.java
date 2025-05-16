package bg.sofia.uni.fmi.mjt.server.constants;

public final class ServerMessagesConstants {
    public static final String SERVER_STARTED_MSG = "Server started and listening on port ";
    public static final String SEE_LOGS_MSG = " See logs for details.";
    public static final String CLIENT_DISCONNECTED_MSG = "Client has disconnected.";

    public static final String ERROR_PROCESSING_CLIENT_REQ_MSG = "Error while processing client request.";
    public static final String SERVER_ERROR_LOGS_MSG = "Fatal server error during startup.";
    public static final String SERVER_ERROR_MSG = "Server failed to start.";
    public static final String ERROR_CLOSING_CONNECTION_MSG = "Error closing client connection.";
    public static final String UNKNOWN_CMD_MSG = "Received unknown command from client: ";
    public static final String SERVER_FAILED_TO_RECOGNIZE_CMD_MSG = "Server failed to recognize the command: ";

    private ServerMessagesConstants() {}
}
