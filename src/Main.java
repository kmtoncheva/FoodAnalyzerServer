import bg.sofia.uni.fmi.mjt.server.FoodAnalyzerServer;
import bg.sofia.uni.fmi.mjt.server.commands.Command;
import bg.sofia.uni.fmi.mjt.server.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.server.commands.GetFoodReportCommand;
import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.http.HttpService;
import bg.sofia.uni.fmi.mjt.server.http.HttpServiceImpl;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import bg.sofia.uni.fmi.mjt.server.service.FoodServiceImpl;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.API_KEY_NAME;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws ApiException {
        HttpService httpService = new HttpServiceImpl();
        FoodService foodService = new FoodServiceImpl(httpService);
        CommandFactory factory = new CommandFactory(foodService);

        FoodAnalyzerServer server = new FoodAnalyzerServer(factory);
        server.start();

//        HttpService httpService = new HttpServiceImpl();
//        FoodService foodService = new FoodServiceImpl(httpService);
//        GetFoodReportCommand cmd = new GetFoodReportCommand("2381454", foodService);
//        List<FoodItemDto> food = cmd.execute();
//
//        System.out.println(food.get(0));
    }
}