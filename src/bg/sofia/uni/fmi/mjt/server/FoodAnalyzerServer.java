package bg.sofia.uni.fmi.mjt.server;

import bg.sofia.uni.fmi.mjt.server.commands.Command;
import bg.sofia.uni.fmi.mjt.server.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.BarcodeReaderException;
import bg.sofia.uni.fmi.mjt.server.exceptions.ConfigurationException;
import bg.sofia.uni.fmi.mjt.server.exceptions.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.dto.response.ServerResponseDto;
import bg.sofia.uni.fmi.mjt.server.utility.LoggerUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static bg.sofia.uni.fmi.mjt.server.constants.CommandConstants.LINE_BREAK;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerConstants.BUFFER_SIZE;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerConstants.HOST;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerConstants.EMPTY;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerConstants.PORT;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.CHECK_AND_TRY_LATER_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.CLIENT_DISCONNECTED_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.DISAPPROVED_REQUEST;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.ERROR_CLOSING_CONNECTION_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.ERROR_PARSING_CMD;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.ERROR_PROCESSING_CLIENT_REQ_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.MISCONFIGURED_FOOD_SERVER_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.SEE_LOGS_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.SERVER_ERROR_LOGS_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.SERVER_FAILED_TO_START_ERROR_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.SERVER_FAILED_TO_RECOGNIZE_CMD_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.SERVER_STARTED_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.TRY_AGAIN_LATER_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.UNKNOWN_CMD_MSG;

/**
 * The {@code FoodAnalyzerServer} class represents a TCP server that handles client requests
 * related to food information analysis. It uses Java NIO for non-blocking I/O operations
 * and processes JSON-based commands from clients using a {@link CommandFactory}.
 */
public class FoodAnalyzerServer {
    private boolean isServerWorking;
    private Selector selector;
    private final Set<SocketChannel> connectedClients = ConcurrentHashMap.newKeySet();
    private static final Logger LOGGER = LoggerUtil.createLogger(FoodAnalyzerServer.class.getName());
    private final CommandFactory commandFactory;

    /**
     * Maintains the read and write buffers per connected client.
     */
    private static class ClientContext {
        ByteBuffer readBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE); // the input that comes from the client
        // will not exceed Buffer Size
        ByteBuffer writeBuffer = null; // the output size can not be predicted
    }

    public FoodAnalyzerServer(CommandFactory commandFactory) {
        if (commandFactory == null) {
            throw new ConfigurationException(MISCONFIGURED_FOOD_SERVER_MSG);
        }

        this.commandFactory = commandFactory;
    }

    /**
     * Opens a {@link ServerSocketChannel} and binds it to a configured port. The server then enters
     * an event loop using a {@link Selector} to listen for incoming client connections and handle I/O operations.
     * It supports non-blocking I/O with multiple concurrent clients using the NIO selector pattern.
     */
    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            configureServer(serverSocketChannel);

            while (isServerWorking) {
                int readyChannels = selector.select();
                if (readyChannels == EMPTY) {
                    continue;
                }

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    try {
                        processRequest(keyIterator);
                    } catch (IOException e) {
                        LOGGER.log(Level.WARNING, ERROR_PROCESSING_CLIENT_REQ_MSG, e);
                        System.out.println(ERROR_PROCESSING_CLIENT_REQ_MSG + SEE_LOGS_MSG);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, SERVER_ERROR_LOGS_MSG, e);
            System.out.println(SERVER_FAILED_TO_START_ERROR_MSG + SEE_LOGS_MSG);
        }

    }

    /**
     * Gracefully stops the server.
     * This method is intended to be called during application shutdown, such as within a JVM shutdown hook,
     * to ensure that the server and its client connections are properly cleaned up.
     */
    public void stop() {
        this.isServerWorking = false;
        if (selector != null && selector.isOpen()) {
            selector.wakeup();
        }

        for (SocketChannel client : connectedClients) {
            closeChannelSilently(client);
        }
    }

    private void configureServer(ServerSocketChannel serverSocketChannel) throws IOException {
        selector = Selector.open();
        configureServerSocketChannel(serverSocketChannel, selector);
        isServerWorking = true;

        LOGGER.info(SERVER_STARTED_MSG + PORT);
        System.out.println(SERVER_STARTED_MSG + PORT);
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(HOST, PORT));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void processRequest(Iterator<SelectionKey> keyIterator) throws IOException {
        SelectionKey key = keyIterator.next();

        if (key.isReadable()) {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            ClientContext context = (ClientContext) key.attachment();

            String clientInput = getClientInput(clientChannel, context.readBuffer);
            if (clientInput == null) {
                return;
            }

            processInput(clientInput, key, clientChannel);
        } else if (key.isWritable()) {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            writePendingData(clientChannel, key);
        } else if (key.isAcceptable()) {
            accept(selector, key);
        }

        keyIterator.remove();
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);

        ClientContext clientContext = new ClientContext();
        clientChannel.register(selector, SelectionKey.OP_READ, clientContext);
        connectedClients.add(clientChannel);
    }

    private String getClientInput(SocketChannel clientChannel, ByteBuffer byteBuffer) throws IOException {
        byteBuffer.clear();
        int readBytes = clientChannel.read(byteBuffer);
        if (readBytes < EMPTY) {  // means the client has disconnected
            clientChannel.close();

            return null; // Return null to signal to the server that this client is no longer active
        }

        byteBuffer.flip();
        byte[] clientInputBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(clientInputBytes);


        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void processInput(String clientInput, SelectionKey key, SocketChannel channel) throws IOException {
        try {
            Command command = commandFactory.create(clientInput);

            if (command.isTerminatingCommand()) {
                LOGGER.info(CLIENT_DISCONNECTED_MSG);
                closeChannelSilently(channel);
                return;
            }

            ServerResponseDto responseDto = executeCommand(command);
            sendResponse(channel, key, responseDto);

        } catch (InvalidCommandException e) {
            LOGGER.warning(UNKNOWN_CMD_MSG + e.getMessage());
            sendErrorResponse(channel, key, SERVER_FAILED_TO_RECOGNIZE_CMD_MSG + e.getMessage());

        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, ERROR_PARSING_CMD, e);
            sendErrorResponse(channel, key, DISAPPROVED_REQUEST + TRY_AGAIN_LATER_MSG);
        }
    }

    private ServerResponseDto executeCommand(Command<FoodItemDto> command) {
        try {
            List<FoodItemDto> apiFoods = command.execute();

            return ServerResponseDto.ok(apiFoods);
        } catch (FoodItemNotFoundException e) {
            return ServerResponseDto.notFound(e.getClientMessage());
        } catch (ApiException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);

            return ServerResponseDto.error(e.getClientMessage());
        } catch (BarcodeReaderException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);

            return ServerResponseDto.error(e.getMessage() + CHECK_AND_TRY_LATER_MSG);
        }
    }

    private void sendResponse(SocketChannel channel, SelectionKey key, ServerResponseDto dto) throws IOException {
        String json = new Gson().toJson(dto) + LINE_BREAK;
        sendResponseToClient(channel, json, key);
    }

    private void sendErrorResponse(SocketChannel channel, SelectionKey key, String message) throws IOException {
        ServerResponseDto dto = ServerResponseDto.error(message);
        sendResponse(channel, key, dto);
    }

    private void closeChannelSilently(SocketChannel channel) {
        try {
            channel.close();
            connectedClients.remove(channel);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, ERROR_CLOSING_CONNECTION_MSG, e);
        }
    }

    private void sendResponseToClient(SocketChannel socketChannel, String response, SelectionKey key)
        throws IOException {
        ClientContext context = (ClientContext) key.attachment();
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        context.writeBuffer = ByteBuffer.wrap(responseBytes);

        writePendingData(socketChannel, key); // try to write immediately
    }

    private void writePendingData(SocketChannel socketChannel, SelectionKey key) throws IOException {
        ClientContext context = (ClientContext) key.attachment();
        ByteBuffer buffer = context.writeBuffer;

        if (buffer == null) return;

        socketChannel.write(buffer);

        if (buffer.hasRemaining()) {
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE); // still data to write
        } else {
            context.writeBuffer = null;
            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE); // writing done
        }
    }
}