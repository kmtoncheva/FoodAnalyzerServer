package bg.sofia.uni.fmi.mjt.server.service.cache;

import bg.sofia.uni.fmi.mjt.server.exceptions.CacheException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.DEFAULT_SUBDIR_NAME;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.FAILED_TO_CREATE_CACHE_DIR_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.FAILED_TO_SAVE_DATA_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.FIRST_IND;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.FOURTH_INDEX;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.INDEX_DIR_NAME;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.INDEX_FILE_NAME;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.JSON_FILE_EXTENSION;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.ONE_SMB_LEN;
import static bg.sofia.uni.fmi.mjt.server.constants.CacheConstants.PATH_CANNOT_BE_RESOLVED_MSG;
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
    private final Path indexDir;
    private final Path indexFile;

    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> gtinToIdIndex = new ConcurrentHashMap<>();
    private final ReentrantLock indexLock = new ReentrantLock();

    private final Gson gson = new Gson();

    /**
     * Constructs a new {@code FileCacheServiceImpl} with the specified base directory for storing cached data.
     * <p>
     * This constructor initializes the main cache directory, a dedicated subdirectory for the cache index,
     * and an index file used to map GTIN/UPC codes to food item IDs. If the specified directory does not exist,
     * it will be created along with the index directory. The method also attempts to load any existing index
     * mappings from the index file during initialization.
     * </p>
     *
     * @param baseCacheDirName the name of the base directory used to store files for the food reports and index data.
     * @throws CacheException if the base or index directory cannot be created, or if the path is invalid or
     *                        there is an error loading the index file
     */
    public FileCacheServiceImpl(String baseCacheDirName) throws CacheException {
        try {
            this.baseCacheDir = Paths.get(baseCacheDirName);
            this.indexDir = baseCacheDir.resolve(INDEX_DIR_NAME);
            this.indexFile = indexDir.resolve(INDEX_FILE_NAME + JSON_FILE_EXTENSION);
        } catch (InvalidPathException e) {
            throw new CacheException(FAILED_TO_CREATE_CACHE_DIR_MSG + PATH_CANNOT_BE_RESOLVED_MSG, e);
        }

        try {
            Files.createDirectories(baseCacheDir);
            Files.createDirectories(indexDir);
            loadIndexFromFile();
        } catch (IOException e) {
            throw new CacheException(FAILED_TO_CREATE_CACHE_DIR_MSG, e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation stores the data on the file system in a structured directory layout.
     * It uses a lock to ensure thread-safe access per key and caches the result to avoid redundant fetches.
     */
    @Override
    public String getReportById(String key, FetchFunction fetcher) throws IOException, ApiException {
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

    /**
     * {@inheritDoc}
     * <p>
     * Storing the gtinUpc values and their corresponding IDs in a {@link ConcurrentHashMap} to be easily retrieved.
     */
    public void persistGtinToIdIndex(Map<String, String> gtinToIdMap) throws CacheException {
        if(gtinToIdMap.isEmpty()) {
            return;
        }

        gtinToIdIndex.putAll(gtinToIdMap);
        saveIndexToFile();
    }

    /**
     * {@inheritDoc}
     */
    public String getIdFromIndex(String gtinUpc) {
        if (gtinUpc != null && gtinToIdIndex.containsKey(gtinUpc)) {
            return gtinToIdIndex.get(gtinUpc);
        }
        return null;
    }

    private void loadIndexFromFile() throws IOException {
        if (Files.exists(indexFile)) {
            indexLock.lock();
            try (Reader reader = Files.newBufferedReader(indexFile, StandardCharsets.UTF_8)) {
                Type mapType = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> map = gson.fromJson(reader, mapType);
                if (map != null) {
                    gtinToIdIndex.putAll(map);
                }
            } finally {
                indexLock.unlock();
            }
        }
    }

    private void saveIndexToFile() throws CacheException {
        indexLock.lock();
        try (Writer writer = Files.newBufferedWriter(indexFile, StandardCharsets.UTF_8,
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            gson.toJson(gtinToIdIndex, writer);
        } catch (IOException e) {
            throw new CacheException(FAILED_TO_SAVE_DATA_MSG, e);
        } finally {
            indexLock.unlock();
        }
    }

    Path getPathForKey(String key) {
        String safeKey = key.replaceAll(REGEX_TO_REPLACE, REGEX_REPLACEMENT);
        String hash = Integer.toHexString(safeKey.hashCode());
        String subDir1 = hash.length() > ONE_SMB_LEN ? hash.substring(FIRST_IND, SECOND_IND) : DEFAULT_SUBDIR_NAME;
        String subDir2 = hash.length() > THREE_SMB_LEN ? hash.substring(SECOND_IND, FOURTH_INDEX) : DEFAULT_SUBDIR_NAME;

        return baseCacheDir.resolve(Paths.get(subDir1, subDir2, safeKey + JSON_FILE_EXTENSION));
    }
}