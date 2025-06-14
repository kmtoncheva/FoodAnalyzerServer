package bg.sofia.uni.fmi.mjt.server.service.cache;

import bg.sofia.uni.fmi.mjt.server.exceptions.CacheException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;

import java.io.IOException;
import java.util.Map;

/**
 * A service interface for caching data based on food IDs.
 * Implementations of this interface manage storage and retrieval of cached data.
 * If the data for a given key is not present in the cache, it will be fetched
 * using the provided {@link FetchFunction}, stored in the cache, and then returned and saved in the local file system.
 */
public interface CacheService {
    /**
     * Returns cached data for the key if available.
     * If the data exists in the cache, it is returned immediately.
     * Otherwise, the provided {@code fetcher} is used to obtain the data,
     * which is then stored in the cache and returned.
     *
     * @param key     the unique identifier for the cached data
     * @param fetcher a functional interface used to fetch data if it is not already cached
     * @return the cached or newly fetched data as a {@code String}
     * @throws IOException IOException if an I/O error occurs during retrieval or caching
     * @throws ApiException if a domain-specific error occurs during data fetching from the external API
     */
    String getById(String key, FetchFunction fetcher) throws IOException, ApiException;

    default void addToIndex(Map<String, String> gtinToIdMap) throws CacheException {return;}

    default String getIdFromIndex(String gtinUpc) {return null;}

    /**
     * A functional interface representing a strategy for fetching data when it is not found in the cache.
     * This allows lazy loading of data from an external source (e.g., an HTTP API).
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