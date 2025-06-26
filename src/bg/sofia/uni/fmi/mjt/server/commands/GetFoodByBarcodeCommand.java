package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.BarcodeDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.BarcodeReaderException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import bg.sofia.uni.fmi.mjt.server.utility.BarcodeReaderUtil;

import java.io.File;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.CHECK_AND_TRY_LATER_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.IMAGE_NOT_FOUND_MSG;

/**
 * An implementation of {@link Command} interface that retrieves food report based on a product barcode.
 * Note: This command does not interact directly with the remote REST API for barcode lookup; it relies on
 * the server's internal cache.
 */
public final class GetFoodByBarcodeCommand implements Command<ReportFoodItemDto> {
    private final BarcodeDto params;
    private final FoodService foodService;

    /**
     * Constructs a {@code GetFoodByBarcodeCommand} with the given parameters from the client and food service.
     *
     * @param params product code or/and a valid path to an image file representing the barcode image.
     * @param foodService the service responsible for business logic for making API calls to retrieve food information
     */
    public GetFoodByBarcodeCommand(BarcodeDto params, FoodService foodService) {
        this.params = params;
        this.foodService = foodService;
    }

    /**
     * {@inheritDoc}
     * <p>
     * If code parameter is given from the client the image path is ignored.
     * Otherwise, code is extracted from the image via {@link BarcodeReaderUtil}.
     * This method delegates the search request to the {@link FoodService}.
     */
    @Override
    public List<ReportFoodItemDto> execute() throws ApiException, BarcodeReaderException {
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