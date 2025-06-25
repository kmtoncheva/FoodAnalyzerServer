package bg.sofia.uni.fmi.mjt.server.service.cache;

import bg.sofia.uni.fmi.mjt.server.exceptions.ConfigurationException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FetchCacheServiceImplTest {
    @Test
    void testGetReportByIdReturnsFetcherResult() throws IOException, ApiException {
        CacheService.FetchFunction fetcher = mock(CacheService.FetchFunction.class);
        when(fetcher.fetch()).thenReturn("{\"data\":\"live\"}");

        CacheService service = new FetchCacheServiceImpl();
        String result = service.getReportById("someKey", fetcher);

        assertEquals("{\"data\":\"live\"}", result);
        verify(fetcher, times(1)).fetch();
    }

    @Test
    void testGetReportByIdPropagatesIOException() throws ApiException, IOException {
        CacheService.FetchFunction fetcher = mock(CacheService.FetchFunction.class);
        when(fetcher.fetch()).thenThrow(new IOException("IO failure"));

        CacheService service = new FetchCacheServiceImpl();

        assertThrows(IOException.class, () -> service.getReportById("key", fetcher));
    }

    @Test
    void testGetReportByIdPropagatesApiException() throws ApiException, IOException {
        CacheService.FetchFunction fetcher = mock(CacheService.FetchFunction.class);
        when(fetcher.fetch()).thenThrow(new ConfigurationException(null, null));

        CacheService service = new FetchCacheServiceImpl();

        assertThrows(ConfigurationException.class, () -> service.getReportById("key", fetcher));
    }
}
