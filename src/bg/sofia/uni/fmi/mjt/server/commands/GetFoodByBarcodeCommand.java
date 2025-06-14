package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.BarcodeDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.ReportFoodItemDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.BarcodeReaderException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import bg.sofia.uni.fmi.mjt.server.utility.BarcodeReaderUtil;

import java.util.List;

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
            code = BarcodeReaderUtil.readImage(params.imagePath());
        }
        ReportFoodItemDto foodItem = foodService.getFoodByBarcode(code);

        return List.of(foodItem);
    }
}