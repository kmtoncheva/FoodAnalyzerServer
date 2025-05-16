package bg.sofia.uni.fmi.mjt.server.commands;

import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.CLIENT_DISCONNECTED_MSG;

public class QuitCommand implements Command{
    @Override
    public String execute() {
        return null;
    }

    @Override
    public boolean isTerminatingCommand() {
        return true;
    }
}
