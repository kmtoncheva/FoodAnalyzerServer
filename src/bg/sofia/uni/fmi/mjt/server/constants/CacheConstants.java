package bg.sofia.uni.fmi.mjt.server.constants;

/**
 * Holds constant values related to cache operations. This class is not meant to be instantiated.
 */
public final class CacheConstants {
    // Message shown when cache directory creation fails.
    public static final String FAILED_TO_CREATE_CACHE_DIR_MSG = "Failed to create cache directory.";
    public static final String PATH_CANNOT_BE_RESOLVED_MSG = " Paths can not be resolved.";
    public static final String FAILED_TO_SAVE_DATA_MSG = "Failed to save data to cache.";
    public static final String FAILED_TO_CREATE_CACHE_WARNING =
        "WARNING: Cache initialization failed. Continuing without caching.";

    // Regex to replace unsupported characters in filenames.
    public static final String REGEX_TO_REPLACE = "[^a-zA-Z0-9-_]";
    public static final String REGEX_REPLACEMENT = "_";

    // Constants related to file naming
    public static final String JSON_FILE_EXTENSION = ".json";
    public static final String INDEX_DIR_NAME = "index";
    public static final String INDEX_FILE_NAME = "gtin_to_id";
    public static final String DEFAULT_SUBDIR_NAME = "00";

    // Indexing constants.
    public static final int ONE_SMB_LEN = 1;
    public static final int THREE_SMB_LEN = 3;
    public static final int FIRST_IND = 0;
    public static final int SECOND_IND = 2;
    public static final int FOURTH_INDEX = 4;

    private CacheConstants() {
    }
}