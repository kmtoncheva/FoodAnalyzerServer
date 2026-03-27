import bg.sofia.uni.fmi.mjt.server.FoodAnalyzerServer;
import bg.sofia.uni.fmi.mjt.server.service.cache.FetchCacheServiceImpl;
import bg.sofia.uni.fmi.mjt.server.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.server.exceptions.CacheException;
import bg.sofia.uni.fmi.mjt.server.service.http.HttpService;
import bg.sofia.uni.fmi.mjt.server.service.http.HttpServiceImpl;
import bg.sofia.uni.fmi.mjt.server.service.cache.CacheService;
import bg.sofia.uni.fmi.mjt.server.service.cache.FileCacheServiceImpl;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import bg.sofia.uni.fmi.mjt.server.service.FoodServiceImpl;

import java.net.http.HttpClient;

import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.FAILED_TO_CREATE_CACHE_WARNING;

/**
 * Entry point for the Food Analyzer server application.
 *
 * <p>Responsible for initializing all core components of the system,
 * including HTTP communication, caching mechanisms, business services, and command handling.
 * It then starts the {@link FoodAnalyzerServer} to listen for client requests.</p>
 *
 * <p>The application attempts to use a file-based cache for improved performance.
 * If the cache initialization fails, it falls back to a dynamic rela-time fetch-based "cache".</p>
 *
 * <p>A shutdown hook is registered to ensure the server is stopped gracefully
 * when the application is terminated.</p>
 */
public class Main {
    private static final String CACHE_NAME = "cache";

    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpService httpService = new HttpServiceImpl(httpClient);
        CacheService cacheService;

        try {
            cacheService = new FileCacheServiceImpl(CACHE_NAME);
        } catch (CacheException e) {
            System.err.println(FAILED_TO_CREATE_CACHE_WARNING);
            cacheService = new FetchCacheServiceImpl();
        }

        FoodService foodService = new FoodServiceImpl(httpService, cacheService);
        CommandFactory factory = new CommandFactory(foodService);
        FoodAnalyzerServer server = new FoodAnalyzerServer(factory);

        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        server.start();
    }
}