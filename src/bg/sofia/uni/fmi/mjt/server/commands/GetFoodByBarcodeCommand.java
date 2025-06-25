package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.BarcodeDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.BarcodeReaderException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import bg.sofia.uni.fmi.mjt.server.utility.BarcodeReaderUtil;

import java.io.File;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.CHECK_AND_TRY_LATER_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.IMAGE_NOT_FOUND_MSG;

public final class GetFoodByBarcodeCommand implements Command {
    private BarcodeDto params;
    private final FoodService foodService;

    public GetFoodByBarcodeCommand(BarcodeDto params, FoodService foodService) {
        this.params = params;
        this.foodService = foodService;
    }

    @Override
    public List<FoodItemDto> execute() throws ApiException, BarcodeReaderException {
        String code = params.code();

        if (code == null) {
            File imageFile = parseImage(params.imagePath());
            code = BarcodeReaderUtil.readBarcodeFromFile(imageFile);
        }
        ReportFoodItemDto foodItem = foodService.getFoodByBarcode(code);

        return List.of(foodItem);
    }

    private File parseImage(String imagePath) throws BarcodeReaderException {
        File imageFile = new File(imagePath);

        if (!imageFile.exists() || !imageFile.isFile()) {
            throw new BarcodeReaderException(IMAGE_NOT_FOUND_MSG + CHECK_AND_TRY_LATER_MSG);
        }

        return imageFile;
    }
}