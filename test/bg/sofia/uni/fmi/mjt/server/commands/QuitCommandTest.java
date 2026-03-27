package bg.sofia.uni.fmi.mjt.server.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuitCommandTest {
    @Test
    void testQuitCommandIsTerminating() {
        QuitCommand command = new QuitCommand();
        assertTrue(command.isTerminatingCommand());
    }

    @Test
    void testQuitCommandExecuteReturnsEmptyList() {
        QuitCommand command = new QuitCommand();
        List<?> result = command.execute();
        Assertions.assertNull(result);
    }
}
