package bg.sofia.uni.fmi.mjt.server.cache;

import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;

import java.io.IOException;

public interface FileCacheService {
    /**
     * Returns cached data for the key if available.
     * Otherwise, uses fetcher to get it, caches it, and returns the result.
     */
    String get(String key, FetchFunction fetcher) throws IOException;

    @FunctionalInterface
    interface FetchFunction {
        String fetch() throws IOException, ApiException;
    }
}