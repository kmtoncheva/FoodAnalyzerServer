package bg.sofia.uni.fmi.mjt.server;

import bg.sofia.uni.fmi.mjt.server.commands.Command;
import bg.sofia.uni.fmi.mjt.server.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiServiceUnavailableException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.MalformedRequestBodyException;
import bg.sofia.uni.fmi.mjt.server.dto.response.ServerResponseDto;
import bg.sofia.uni.fmi.mjt.server.utility.LoggerConfig;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import static bg.sofia.uni.fmi.mjt.server.constants.FoodDataContsants.DATA_TYPE_TITLE;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerConstants.BUFFER_SIZE;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerConstants.HOST;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerConstants.PORT;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.CLIENT_DISCONNECTED_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.ERROR_CLOSING_CONNECTION_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.ERROR_PROCESSING_CLIENT_REQ_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.SEE_LOGS_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.SERVER_ERROR_LOGS_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.SERVER_FAILED_TO_START_ERROR_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.SERVER_FAILED_TO_RECOGNIZE_CMD_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.SERVER_STARTED_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.UNKNOWN_CMD_MSG;

public class FoodAnalyzerServer {
    private boolean isServerWorking;
    private Selector selector;

    private static class ClientContext {
        ByteBuffer readBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        ByteBuffer writeBuffer = null;
    }

    private static final Logger LOGGER = LoggerConfig.createLogger(FoodAnalyzerServer.class.getName());
    private final CommandFactory commandFactory;

    public FoodAnalyzerServer(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            configureServer(serverSocketChannel);

            while (isServerWorking) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
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
        ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
        SocketChannel acceptChannel = socketChannel.accept();
        acceptChannel.configureBlocking(false);

        ClientContext context = new ClientContext();
        acceptChannel.register(selector, SelectionKey.OP_READ, context);

        /*
          ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
    SocketChannel clientChannel = serverChannel.accept();
    clientChannel.configureBlocking(false);

    ClientContext context = new ClientContext();
    clientChannel.register(selector, SelectionKey.OP_READ, context);
         */
    }

    private String getClientInput(SocketChannel clientChannel, ByteBuffer byteBuffer) throws IOException {
        byteBuffer.clear();
        int readBytes = clientChannel.read(byteBuffer);
        if (readBytes < 0) {  // means the client has disconnected
            clientChannel.close();

            return null; // Return null to signal to the server that this client is no longer active
        }

        byteBuffer.flip();
        byte[] clientInputBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(clientInputBytes);


        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    /**
     * {"command":"get-food","args":["raffaello"]}
     * {"command":"get-food-by-barcode","args":{"code":"23e","imagePath":"C:\\Users\\Katina\\Pictures\\Screenshot.jpg"}}
     * {"command":"get-food-report","args":"123"}
     * {"command":"quit"}
     */
    private void processInput(String clientInput, SelectionKey key, SocketChannel channel) throws IOException {
        Command command = null;

        try {
            command = commandFactory.create(clientInput);

            if (command.isTerminatingCommand()) {
                System.out.println(CLIENT_DISCONNECTED_MSG);
                try {
                    channel.close();

                    return;
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, ERROR_CLOSING_CONNECTION_MSG, e);
                }
            }

           // String requestResponse = null;
            ServerResponseDto serverResponseDto = null;

            try {
                List<FoodItemDto> apiFoods = command.execute();
                serverResponseDto = ServerResponseDto.ok(apiFoods);
            } catch (ApiServiceUnavailableException | MalformedRequestBodyException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                System.out.println("Something went wrong while processing current request. " +
                    "Please see logs for details.");

                serverResponseDto = ServerResponseDto.error(e.getClientMessage());
            } catch (FoodItemNotFoundException e) {
                serverResponseDto = ServerResponseDto.notFound(e.getClientMessage());
            } catch (ApiException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                serverResponseDto = ServerResponseDto.error(e.getClientMessage());
            }

            Gson gson = new Gson();
            String responseMessage = gson.toJson(serverResponseDto) + "\n"; // or use "\0"

            sendResponseToClient(channel, responseMessage, key);

        } catch (InvalidCommandException | IOException e) {
            LOGGER.warning(UNKNOWN_CMD_MSG + e.getMessage());
            //ServerResponseDto.error(SERVER_FAILED_TO_RECOGNIZE_CMD_MSG + e.getMessage());

            sendResponseToClient(channel, SERVER_FAILED_TO_RECOGNIZE_CMD_MSG + e.getMessage(), key);
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "NB", e);

            LOGGER.warning("Unexpected error while parsing command: " + e.getMessage());
            //ServerResponseDto.error("Invalid request format or unexpected internal error. Please check request and try again.");

            sendResponseToClient(channel, "Invalid request format or unexpected internal error. Please check request and try again." +  "\n", key);
        }
    }

    private void sendResponseToClient(SocketChannel socketChannel, String response, SelectionKey key)
        throws IOException {
        ClientContext context = (ClientContext) key.attachment();
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        context.writeBuffer = ByteBuffer.wrap(responseBytes);

        writePendingData(socketChannel, key); // try to write immediately

//        buffer.clear();
//        buffer.put(response.getBytes());
//        buffer.flip();
//
//        socketChannel.write(buffer);
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


    private void stop() {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }
}