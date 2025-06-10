import bg.sofia.uni.fmi.mjt.server.FoodAnalyzerServer;
import bg.sofia.uni.fmi.mjt.server.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.http.HttpService;
import bg.sofia.uni.fmi.mjt.server.http.HttpServiceImpl;
import bg.sofia.uni.fmi.mjt.server.cache.FileCacheService;
import bg.sofia.uni.fmi.mjt.server.cache.FileCacheServiceImpl;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import bg.sofia.uni.fmi.mjt.server.service.FoodServiceImpl;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws ApiException {
        HttpService httpService = new HttpServiceImpl();
        FileCacheService cacheService = new FileCacheServiceImpl("cache");
        FoodService foodService = new FoodServiceImpl(httpService, cacheService);
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