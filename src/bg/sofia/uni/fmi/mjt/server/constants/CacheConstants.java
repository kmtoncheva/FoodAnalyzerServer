package bg.sofia.uni.fmi.mjt.server.constants;

public final class CacheConstants {
    public static final String FAILED_TO_CREATE_CACHE_DIR_MSG = "Failed to create cache directory.";
    public static final String FAILED_TO_CREATE_CACHE_WARNING =
        "WARNING: Cache initialization failed. Continuing without caching.";

    public static final String REGEX_TO_REPLACE = "[^a-zA-Z0-9-_]";
    public static final String REGEX_REPLACEMENT = "_";
    public static final String DEFAULT_SUBDIR_NAME = "00";
    public static final String JSON_FILE_EXTENSION = ".json";

    public static final int ONE_SMB_LEN = 1;
    public static final int THREE_SMB_LEN = 3;
    public static final int FIRST_IND = 0;
    public static final int SECOND_IND = 2;
    public static final int FOURTH_INDEX = 4;
    private CacheConstants() {}
}
