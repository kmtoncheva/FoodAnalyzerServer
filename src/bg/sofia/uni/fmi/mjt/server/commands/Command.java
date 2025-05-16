package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;

public interface Command {
    String execute() throws ApiException;

    default boolean isTerminatingCommand() {
        return false;
    }
}
