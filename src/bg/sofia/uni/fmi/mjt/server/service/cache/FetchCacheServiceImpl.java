package bg.sofia.uni.fmi.mjt.server.service.cache;

import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;

import java.io.IOException;

/**
 * A no-op (non-caching) implementation of the {@link CacheService} interface.
 * <p>
 * It is intended for fallback scenarios when the
 * caching system is unavailable or cannot be initialized (e.g., due to directory creation failure),
 * allowing the program to continue running with live data only.
 */
public final class FetchCacheServiceImpl implements CacheService {
    /**
     * {@inheritDoc}
     * <p>
     * This method always calls the provided {@link FetchFunction} directly, bypassing any caching mechanism.
     */
    @Override
    public String getReportById(String key, FetchFunction fetcher) throws IOException, ApiException {
        return fetcher.fetch();
    }
}