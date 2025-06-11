import bg.sofia.uni.fmi.mjt.server.FoodAnalyzerServer;
import bg.sofia.uni.fmi.mjt.server.cache.FetchCacheServiceImpl;
import bg.sofia.uni.fmi.mjt.server.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.server.exceptions.CacheInitializationException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.http.HttpService;
import bg.sofia.uni.fmi.mjt.server.http.HttpServiceImpl;
import bg.sofia.uni.fmi.mjt.server.cache.CacheService;
import bg.sofia.uni.fmi.mjt.server.cache.FileCacheServiceImpl;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import bg.sofia.uni.fmi.mjt.server.service.FoodServiceImpl;

import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.FAILED_TO_CREATE_CACHE_WARNING;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws ApiException {
        HttpService httpService = new HttpServiceImpl();
        CacheService cacheService;

        try {
            cacheService = new FileCacheServiceImpl("cache");
        } catch (CacheInitializationException e) {
            System.err.println(FAILED_TO_CREATE_CACHE_WARNING);
            e.printStackTrace();
            cacheService = new FetchCacheServiceImpl();
        }

        FoodService foodService = new FoodServiceImpl(httpService, cacheService);
        CommandFactory factory = new CommandFactory(foodService);
        FoodAnalyzerServer server = new FoodAnalyzerServer(factory);
        server.start();
    }
}