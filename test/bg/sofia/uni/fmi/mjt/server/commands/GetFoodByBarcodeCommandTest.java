package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.BarcodeDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.BarcodeReaderException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import bg.sofia.uni.fmi.mjt.server.utility.BarcodeReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.server.utility.BarcodeReaderUtil.readBarcodeFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetFoodByBarcodeCommandTest {

    private FoodService foodServiceMock;

    @BeforeEach
    void setup() {
        foodServiceMock = Mockito.mock(FoodService.class);
    }

    @Test
    void testExecuteWithProvidedBarcode() throws ApiException, BarcodeReaderException {
        BarcodeDto params = Mockito.mock(BarcodeDto.class);
        when(params.code()).thenReturn("12345");

        ReportFoodItemDto mockedFoodItem = Mockito.mock(ReportFoodItemDto.class);
        when(foodServiceMock.getFoodByBarcode("12345")).thenReturn(mockedFoodItem);

        try (MockedStatic<BarcodeReaderUtil> mockedStatic = Mockito.mockStatic(BarcodeReaderUtil.class)) {

            GetFoodByBarcodeCommand command = new GetFoodByBarcodeCommand(params, foodServiceMock);
            List<ReportFoodItemDto> result = command.execute();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertSame(mockedFoodItem, result.get(0));
            verify(foodServiceMock, times(1)).getFoodByBarcode("12345");
            mockedStatic.verify(() -> BarcodeReaderUtil.readBarcodeFromFile(any(File.class)), never());
        }
    }

    @Test
    void testExecuteWithNullBarcode() throws ApiException, BarcodeReaderException {
        BarcodeDto params = Mockito.mock(BarcodeDto.class);
        when(params.code()).thenReturn(null);
        when(params.imagePath()).thenReturn(
            "C:\\Users\\Katina\\Desktop\\Java\\Project\\FoodAnalyzerServer\\test\\resources\\barcode.gif");

        ReportFoodItemDto mockedFoodItem = Mockito.mock(ReportFoodItemDto.class);
        when(foodServiceMock.getFoodByBarcode("barcodeFromImage")).thenReturn(mockedFoodItem);

        try (MockedStatic<BarcodeReaderUtil> mockedStatic = mockStatic(BarcodeReaderUtil.class)) {
            mockedStatic
                .when(() -> readBarcodeFromFile(any(File.class)))
                .thenReturn("barcodeFromImage");
            GetFoodByBarcodeCommand command = new GetFoodByBarcodeCommand(params, foodServiceMock);

            List<ReportFoodItemDto> result = command.execute();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertSame(mockedFoodItem, result.getFirst());
            verify(foodServiceMock).getFoodByBarcode("barcodeFromImage");
            mockedStatic.verify(() -> BarcodeReaderUtil.readBarcodeFromFile(any(File.class)), times(1));
        }
    }

    @Test
    void testExecuteWithBarcodeReaderException() {
        BarcodeDto params = Mockito.mock(BarcodeDto.class);
        when(params.code()).thenReturn(null);
        when(params.imagePath()).thenReturn("non/existing/path.png");

        GetFoodByBarcodeCommand command = new GetFoodByBarcodeCommand(params, foodServiceMock);

        BarcodeReaderException thrown = assertThrows(BarcodeReaderException.class, command::execute);
        assertTrue(thrown.getMessage().contains("image not found"));
    }
}