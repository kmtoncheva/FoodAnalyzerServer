package bg.sofia.uni.fmi.mjt.server.utility;

import bg.sofia.uni.fmi.mjt.server.exceptions.BarcodeReaderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class BarcodeReaderUtilTest {
    @Test
    void testReadBarcodeFromFileWithValidFile() throws BarcodeReaderException {
        File imageFile =
            new File("C:\\Users\\Katina\\Desktop\\Java\\Project\\FoodAnalyzerServer\\test\\resources\\barcode.gif");

        String result = BarcodeReaderUtil.readBarcodeFromFile(imageFile);
        assertEquals("009800146130", result);
    }

    @Test
    void testReadBarcodeFromFileWithNoBarcodeFile() {
        File imageFile =
            new File("C:\\Users\\Katina\\Desktop\\Java\\Project\\FoodAnalyzerServer\\test\\resources\\Black.png");

        Assertions.assertThrows(BarcodeReaderException.class, () -> {
            BarcodeReaderUtil.readBarcodeFromFile(imageFile);
        }, "Should throw an exception when there is no barcode in the image.");
    }

    @Test
    void testReadBarcodeFromFileWithInvalidFile() {
        File imageFile =
            new File("C:\\Users\\Katina\\Desktop\\Java\\Project\\FoodAnalyzerServer\\test\\resources\\text.txt");

        Assertions.assertThrows(BarcodeReaderException.class, () -> {
            BarcodeReaderUtil.readBarcodeFromFile(imageFile);
        }, "Should throw an exception when it is a non-image file.");
    }
    @Test
    void testReadBarcodeFromFileWithMissingFile() {
        File imageFile =
            new File("");

        Assertions.assertThrows(BarcodeReaderException.class, () -> {
            BarcodeReaderUtil.readBarcodeFromFile(imageFile);
        }, "Should throw an exception when file is missing or path is invalid.");
    }
}