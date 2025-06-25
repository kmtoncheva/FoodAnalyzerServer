package bg.sofia.uni.fmi.mjt.server.service.cache;

import bg.sofia.uni.fmi.mjt.server.exceptions.CacheException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;

import java.io.IOException;
import java.util.Map;

/**
 * A service interface for caching data based on unique food item IDs.
 * Implementations of this interface manage the storage and retrieval of data using a caching mechanism.
 * If the data associated with a given key is not found in the cache, it is fetched using the provided
 * {@link FetchFunction}, stored in the cache, and then returned.
 * Some implementations may also maintain an index to map {@code gtinUpc} values to their corresponding food item IDs.
 * This index enables efficient lookup of items by their barcode and is persisted in the file system for future access.
 * <p>
 * When caching is unavailable or fails to initialize (e.g., due to file system issues), a fallback no-op
 * implementation such as {@link FetchCacheServiceImpl} can be used, which bypasses all caching and indexing logic.
 */
public interface CacheService {
    /**
     * Returns cached data (food report) for the key if available.
     * If the data exists in the cache, it is returned immediately.
     * Otherwise, the provided {@code fetcher} is used to obtain the data,
     * which is then stored in the cache and returned.
     *
     * @param key     the unique identifier for the cached data
     * @param fetcher a functional interface used to fetch data if it is not already cached
     * @return the cached or newly fetched data as a {@code String}
     * @throws IOException  IOException if an I/O error occurs during retrieval or caching
     * @throws ApiException if a domain-specific error occurs during data fetching from the external API
     */
    String getReportById(String key, FetchFunction fetcher) throws IOException, ApiException;

    /**
     * Caches the latest food items.
     * After the requests to the external API, all retrieved data with associated {@code gtinUpc} values
     * are added to the index file that maps each {@code gtinUpc} to its corresponding food item ID.
     * <p>
     * The default implementation (as in {@link FetchCacheServiceImpl}) is a no-op, used when caching is unavailable.
     *
     * @param gtinToIdMap a map containing {@code gtinUpc} values as keys and corresponding food item IDs as values.
     * @throws CacheException if an error occurs while saving the index data to the cache
     */
    default void persistGtinToIdIndex(Map<String, String> gtinToIdMap) throws CacheException {
        return;
    }

    /**
     * Retrieves a food item ID from the index based on its {@code gtinUpc} value.
     * This is used to resolve branded food items that are identified by their {@code gtinUpc}.
     * If the ID is not found in the cache, {@code null} is returned.
     * <p>
     * The default implementation (as in {@link FetchCacheServiceImpl}) always returns {@code null},
     * indicating that no cache index is available.
     *
     * @param gtinUpc the parameter the food is searched with from the client.
     * @return the corresponding food item ID if present, or {@code null} otherwise
     */
    default String getIdFromIndex(String gtinUpc) {
        return null;
    }

    /**
     * A functional interface representing a strategy for fetching data when it is not found in the cache.
     */
    @FunctionalInterface
    interface FetchFunction {
        /**
         * Fetches the data associated with a cache key.
         *
         * @return the fetched data as a {@code String}
         * @throws IOException  if an I/O error occurs while fetching the data
         * @throws ApiException if a domain-specific error occurs during data fetching (e.g., invalid response)
         */
        String fetch() throws IOException, ApiException;
    }
}