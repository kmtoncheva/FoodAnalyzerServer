package bg.sofia.uni.fmi.mjt.server.cache;

import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class FileCacheServiceImpl implements FileCacheService {
    private final Path baseCacheDir;
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public FileCacheServiceImpl(String baseCacheDirName) {
        this.baseCacheDir = Paths.get(baseCacheDirName);
        try {
            Files.createDirectories(baseCacheDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create cache directory", e);
        }
    }

    @Override
    public String get(String key, FetchFunction fetcher) throws IOException {
        Path filePath = getPathForKey(key);
        ReentrantLock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());

        lock.lock();
        try {
            if (Files.exists(filePath)) {
                return Files.readString(filePath, StandardCharsets.UTF_8);
            }

            String result = fetcher.fetch();

            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, result, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            return result;
        } catch (ApiException e) {
            throw new RuntimeException("SHOULD NOT BE RUNTIME - TEMP");
        } finally {
            lock.unlock();
        }
    }

    private Path getPathForKey(String key) {
        // Optional: sanitize or hash if keys could be unsafe
        String safeKey = key.replaceAll("[^a-zA-Z0-9-_]", "_");
        String hash = Integer.toHexString(safeKey.hashCode());
        String subDir1 = hash.length() > 1 ? hash.substring(0, 2) : "00";
        String subDir2 = hash.length() > 3 ? hash.substring(2, 4) : "00";

        return baseCacheDir.resolve(Paths.get(subDir1, subDir2, safeKey + ".json"));
    }
}
