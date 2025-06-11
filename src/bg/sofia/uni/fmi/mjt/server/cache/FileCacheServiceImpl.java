package bg.sofia.uni.fmi.mjt.server.cache;

import bg.sofia.uni.fmi.mjt.server.exceptions.CacheInitializationException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.DEFAULT_SUBDIR_NAME;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.FAILED_TO_CREATE_CACHE_DIR_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.FIRST_IND;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.FOURTH_INDEX;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.JSON_FILE_EXTENSION;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.ONE_SMB_LEN;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.REGEX_REPLACEMENT;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.REGEX_TO_REPLACE;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.SECOND_IND;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.THREE_SMB_LEN;

/**
 * Implementation of {@link CacheService} that uses the local file system to persist and retrieve cached data.
 * This service stores API responses in a structured directory hierarchy based on hashed keys to avoid repeated
 * network calls for identical requests. It ensures thread-safe access using per-key {@link ReentrantLock}s.
 */
public final class FileCacheServiceImpl implements CacheService {
    private final Path baseCacheDir;
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public FileCacheServiceImpl(String baseCacheDirName) throws CacheInitializationException {
        this.baseCacheDir = Paths.get(baseCacheDirName);
        try {
            Files.createDirectories(baseCacheDir);
        } catch (IOException e) {
            throw new CacheInitializationException(FAILED_TO_CREATE_CACHE_DIR_MSG, e);
        }
    }

    @Override
    public String get(String key, FetchFunction fetcher) throws IOException, ApiException {
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
        } finally {
            lock.unlock();
        }
    }

    private Path getPathForKey(String key) {
        String safeKey = key.replaceAll(REGEX_TO_REPLACE, REGEX_REPLACEMENT);
        String hash = Integer.toHexString(safeKey.hashCode());
        String subDir1 = hash.length() > ONE_SMB_LEN ? hash.substring(FIRST_IND, SECOND_IND) : DEFAULT_SUBDIR_NAME;
        String subDir2 = hash.length() > THREE_SMB_LEN ? hash.substring(SECOND_IND, FOURTH_INDEX) : DEFAULT_SUBDIR_NAME;

        return baseCacheDir.resolve(Paths.get(subDir1, subDir2, safeKey + JSON_FILE_EXTENSION));
    }
}