package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.model.dto.BarcodeRequestDto;

public class GetFoodByBarcodeCommand implements Command{
    private BarcodeRequestDto params;

    public GetFoodByBarcodeCommand(BarcodeRequestDto params) {
        this.params = params;
    }

    @Override
    public String execute() {
        return "";
    }
}
