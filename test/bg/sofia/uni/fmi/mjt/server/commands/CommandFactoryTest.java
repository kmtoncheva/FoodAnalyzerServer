package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.exceptions.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandFactoryTest {
    private FoodService foodServiceMock = mock(FoodService.class);;
    private CommandFactory factory = new CommandFactory(foodServiceMock);;

    @Test
    void testCreateWithGetFoodCommand() throws Exception {
        String json = "{\"command\":\"get-food\", \"args\":[\"apple\"]}";
        Command<?> command = factory.create(json);

        assertTrue(command instanceof GetFoodCommand, "Should create a get-food command instance on valid input.");
    }

    @Test
    void testCreateWithGetFoodReportCommand() throws Exception {
        String json = "{\"command\":\"get-food-report\", \"args\":\"12345\"}";
        Command<?> command = factory.create(json);

        assertTrue(command instanceof GetFoodReportCommand, "Should create a get-food-report command instance on valid input.");
    }
    @Test
    void testCreateGetFoodByBarcodeCommand() throws Exception {
        String json = "{\"command\":\"get-food-by-barcode\", \"args\":{\"code\":\"xyz\",\"imagePath\":\"path/to/image\"}}";
        Command<?> command = factory.create(json);

        assertTrue(command instanceof GetFoodByBarcodeCommand, "Should create a get-food-by-barcode command instance on valid input.");
    }

    @Test
    void testCreateWithQuitCommand() throws Exception {
        String json = "{\"command\":\"quit\", \"args\":null}";
        Command<?> command = factory.create(json);

        assertTrue(command instanceof QuitCommand, "Should create a quit command.");
    }

    @Test
    void testCreateWithUnknownCommand() {
        String json = "{\"command\":\"unknown\", \"args\":null}";

        assertThrows(InvalidCommandException.class, () -> factory.create(json), "Should throw an InvalidCommandException when the command is none of the specified.");
    }

    @Test
    void testCreateWithInvalidJsonSyntax() {
        String invalidJson = "{not valid json}";

        assertThrows(RuntimeException.class, () -> factory.create(invalidJson), "Should throw a Runtime exception with invalid JSON syntax.");
    }

    @Test
    void testCreateWithMissingCommandField() {
        String jsonMissingCommand = "{\"args\":[]}";

        assertThrows(RuntimeException.class, () -> factory.create(jsonMissingCommand), "Should throw a Runtime exception ");
    }

}