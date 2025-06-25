package bg.sofia.uni.fmi.mjt.server.service.cache;

import bg.sofia.uni.fmi.mjt.server.exceptions.CacheException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileCacheServiceImpltest {
    @TempDir
    Path tempDir;

    private FileCacheServiceImpl createService() throws CacheException {
        return new FileCacheServiceImpl(tempDir.toString());
    }

    private void writeFile(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

    @Test
    void testConstructorCreatesDirectories() {
        Path baseDir = tempDir.resolve("cache");

        Assertions.assertDoesNotThrow(() -> new FileCacheServiceImpl(baseDir.toString()));
        Assertions.assertTrue(Files.exists(baseDir));
        Assertions.assertTrue(Files.exists(baseDir.resolve("index")));
    }

    @Test
    void testConstructorThrowsCacheException() {
        String invalidPath = "invalid/Dir";

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(Paths.get(invalidPath)))
                .thenThrow(new IOException("Simulated failure"));

            Assertions.assertThrows(CacheException.class, () -> new FileCacheServiceImpl(invalidPath));
        }
    }

    @Test
    void testConstructorLoadsIndexFileCorrectly() throws Exception {
        Path indexDir = tempDir.resolve("index");
        Files.createDirectories(indexDir);

        String jsonContent = """
            {
                "123456789012": "fdcId123",
                "987654321098": "fdcId987"
            }
        """;

        Files.writeString(indexDir.resolve("gtin_to_id.json"), jsonContent, StandardCharsets.UTF_8);

        FileCacheServiceImpl service = createService();

        Assertions.assertEquals("fdcId123", service.getIdFromIndex("123456789012"));
        Assertions.assertEquals("fdcId987", service.getIdFromIndex("987654321098"));
        Assertions.assertNull(service.getIdFromIndex("000000000000"));
    }

    @Test
    void testGetReportByIdReturnsExistingFile() throws Exception {
        String key = "009800146130";
        String expectedContent = "{\"some\":\"json\"}";

        FileCacheServiceImpl service = createService();
        Path filePath = service.getPathForKey(key);

        writeFile(filePath, expectedContent);

        CacheService.FetchFunction fetcher = mock();

        String result = service.getReportById(key, fetcher);

        Assertions.assertEquals(expectedContent, result);
        verify(fetcher, never()).fetch();
    }

    @Test
    void testGetReportByIdCallsFetcherWhenFileMissing() throws Exception {
        String key = "109800146130";
        String expectedContent = "{\"some\":\"fetcher\"}";

        FileCacheServiceImpl service = createService();
        Path filePath = service.getPathForKey(key);

        CacheService.FetchFunction fetcher = mock();
        when(fetcher.fetch()).thenReturn(expectedContent);

        Assertions.assertFalse(Files.exists(filePath));

        String result = service.getReportById(key, fetcher);

        Assertions.assertEquals(expectedContent, result);
        Assertions.assertTrue(Files.exists(filePath));
        Assertions.assertEquals(expectedContent, Files.readString(filePath));
        verify(fetcher, times(1)).fetch();
    }

    @Test
    void testGetReportByIdThreadSafety() throws Exception {
        String key = "209800146130";
        String expectedContent = "{\"some\":\"json\"}";

        FileCacheServiceImpl service = createService();
        Path filePath = service.getPathForKey(key);

        CacheService.FetchFunction fetcher = mock();
        when(fetcher.fetch()).thenReturn(expectedContent);

        int threadCount = 4;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                startLatch.await();
                try {
                    return service.getReportById(key, fetcher);
                } finally {
                    doneLatch.countDown();
                }
            }));
        }

        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();

        for (Future<String> future : futures) {
            Assertions.assertEquals(expectedContent, future.get());
        }

        verify(fetcher, times(1)).fetch();
        Assertions.assertTrue(Files.exists(filePath));
        Assertions.assertEquals(expectedContent, Files.readString(filePath));
    }

    @Test
    void testGetReportByIdWithExcepyion() throws IOException, ApiException {
        CacheService.FetchFunction fetcher = mock();
        when(fetcher.fetch()).thenThrow(FoodItemNotFoundException.class);

        Assertions.assertThrows(ApiException.class, () -> createService().getReportById("key", fetcher));
    }

    @Test
    void testGetReportByIdWithIOException() throws Exception {
        FileCacheServiceImpl service = createService();
        String key = "testKey";
        Path filePath = service.getPathForKey(key);

        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, "some-data");

        CacheService.FetchFunction fetcher = mock();

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(filePath)).thenReturn(true);
            filesMock.when(() -> Files.readString(filePath, StandardCharsets.UTF_8))
                .thenThrow(new IOException("Simulated read error"));

            Assertions.assertThrows(IOException.class, () -> service.getReportById(key, fetcher));
        }
    }

    @Test
    void testPersistGtinToIdIndexWithData() throws CacheException, IOException {
        Map<String, String> gtinToIdMap = new ConcurrentHashMap<>();
        gtinToIdMap.put("gtin1", "id1");
        gtinToIdMap.put("gtin2", "id2");

        FileCacheServiceImpl service = createService();
        Assertions.assertNull(service.getIdFromIndex("gtin1"), "Should not have such data in advance.");
        Assertions.assertNull(service.getIdFromIndex("gtin2"), "Should not have such data in advance.");

        service.persistGtinToIdIndex(gtinToIdMap);
        Assertions.assertEquals("id1", service.getIdFromIndex("gtin1"), "Should have saved the data.");
        Assertions.assertEquals("id2", service.getIdFromIndex("gtin2"), "Should have saved the data.");

        String jsonContent = "{\"gtin2\":\"id2\",\"gtin1\":\"id1\"}";

        Path indexDir = tempDir.resolve("index");
        Assertions.assertEquals(jsonContent, Files.readString(indexDir.resolve("gtin_to_id.json")));
    }

    @Test
    void testGetIdFromIndexWithExistingKeys() throws Exception {
        String jsonContent = """
    {
        "gtin1": "id1",
        "gtin2": "id2"
    }
    """;

        Path indexDir = tempDir.resolve("index");
        Files.createDirectories(indexDir);
        Path indexFile = indexDir.resolve("gtin_to_id.json");
        Files.writeString(indexFile, jsonContent, StandardCharsets.UTF_8);

        FileCacheServiceImpl service = new FileCacheServiceImpl(tempDir.toString());

        Assertions.assertEquals("id1", service.getIdFromIndex("gtin1"));
        Assertions.assertEquals("id2", service.getIdFromIndex("gtin2"));
    }

    @Test
    void testGetIdFromIndexWithNotExistingKeys() throws Exception {
        String jsonContent = """
    {
        "gtin1": "id1"
    }
    """;

        Path indexDir = tempDir.resolve("index");
        Files.createDirectories(indexDir);
        Path indexFile = indexDir.resolve("gtin_to_id.json");
        Files.writeString(indexFile, jsonContent, StandardCharsets.UTF_8);

        FileCacheServiceImpl service = new FileCacheServiceImpl(tempDir.toString());

        Assertions.assertNull(service.getIdFromIndex("nonexistent"));
    }

    @Test
    void testGetIdFromIndexWithNull() throws Exception {
        FileCacheServiceImpl service = new FileCacheServiceImpl(tempDir.toString());
        Assertions.assertNull(service.getIdFromIndex(null));
    }
}