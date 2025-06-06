package bg.sofia.uni.fmi.mjt.client;

import bg.sofia.uni.fmi.mjt.client.commands.Command;
import bg.sofia.uni.fmi.mjt.client.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.client.dto.request.ClientRequestDto;
import bg.sofia.uni.fmi.mjt.client.exceptions.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.client.utils.LoggerConfigUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static bg.sofia.uni.fmi.mjt.client.constants.ClientAttrConstants.BUFFER_SIZE;
import static bg.sofia.uni.fmi.mjt.client.constants.ClientAttrConstants.SERVER_HOST;
import static bg.sofia.uni.fmi.mjt.client.constants.ClientAttrConstants.SERVER_PORT;
import static bg.sofia.uni.fmi.mjt.client.constants.ClientMessagesConstants.GREETING_MSG;
import static bg.sofia.uni.fmi.mjt.client.constants.ClientMessagesConstants.HELP_MSG;
import static bg.sofia.uni.fmi.mjt.client.constants.ClientMessagesConstants.NETWORK_CONNECTION_PROBLEM_MSG;
import static bg.sofia.uni.fmi.mjt.client.constants.ClientMessagesConstants.SEE_LOGS_MSG;
import static bg.sofia.uni.fmi.mjt.client.constants.LoggerConstants.SERVER_CONNECTION_ERROR_MSG;

public class FoodAnalyzerClient {
    private final ByteBuffer buffer;

    private Command command;
    private static final Logger LOGGER = LoggerConfigUtils.createLogger(FoodAnalyzerClient.class.getName());

    public FoodAnalyzerClient() {
        buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    }

    public void start() {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println(GREETING_MSG + HELP_MSG);

            while(true) {
                String input = scanner.nextLine();
                String response = null;

                try {
                    command = parseCommand(input);
                }
                catch (InvalidCommandException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                if (sendRequest(socketChannel, command) && !command.isTerminatingCommand()) {
                    response = getResponse(socketChannel);
                }

                command.printOutputToClient(response);

                if(command.isTerminatingCommand()) {
                    break;
                }
            }
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, SERVER_CONNECTION_ERROR_MSG, e);
            System.out.println(NETWORK_CONNECTION_PROBLEM_MSG + SEE_LOGS_MSG);
        }
    }

    private Command parseCommand(String input) throws InvalidCommandException {
        return CommandFactory.create(input); // can return one of the 5 cmds or throw an exception
    }


    private boolean sendRequest(SocketChannel socketChannel, Command command) throws IOException {
        ClientRequestDto request = command.getRequest();
        if (request == null) {
            return false; // Nothing to send, e.g., help command
        }

        String json = request.toJson();

        buffer.clear();
        buffer.put(json.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        socketChannel.write(buffer);

        return true;
    }

    private String getResponse(SocketChannel socketChannel) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        buffer.clear();

        while (true) {
            int bytesRead = socketChannel.read(buffer);
            if (bytesRead == -1) break; // EOF

            if (bytesRead == 0) {
                // If non-blocking, you'd wait on a selector
                break;
            }

            buffer.flip();
            byte[] chunk = new byte[buffer.remaining()];
            buffer.get(chunk);
            out.write(chunk);
            buffer.clear();

            // Check if we've reached the end marker (e.g. newline)
            String dataSoFar = out.toString(StandardCharsets.UTF_8);
            if (dataSoFar.endsWith("\n")) break;
        }

        return out.toString(StandardCharsets.UTF_8);
    }
}