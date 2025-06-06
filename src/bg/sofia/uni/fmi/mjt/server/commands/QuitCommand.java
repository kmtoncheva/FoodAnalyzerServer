package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;

import java.util.List;

/**
 * Command implementation that represents the quit operation.
 * <p>
 * This command is used to indicate that the application should terminate.
 * When executed, it performs no operation and returns {@code null}.
 */
public class QuitCommand implements Command {
    /**
     * Executes the quit command.
     * <p>
     * Since quitting does not require any action or result, this method returns {@code null}.
     *
     * @return {@code null}, as no operation is performed.
     */
    @Override
    public List<FoodItemDto> execute() {
        return null;
    }

    /**
     * @return {@code true}, indicating the command terminates the application.
     */
    @Override
    public boolean isTerminatingCommand() {
        return true;
    }
}