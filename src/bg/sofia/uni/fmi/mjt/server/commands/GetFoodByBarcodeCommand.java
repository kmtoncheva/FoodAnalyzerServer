package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.dto.model.BarcodeDto;
import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;

import java.util.List;

public class GetFoodByBarcodeCommand implements Command{
    private BarcodeDto params;

    public GetFoodByBarcodeCommand(BarcodeDto params) {
        this.params = params;
    }

    @Override
    public List<FoodItemDto> execute() {
        return null;
    }
}
