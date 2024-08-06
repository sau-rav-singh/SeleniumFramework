package apiUtils;

public final class TestConstants {

    public static final String DATA_FILE_PATH = "src/test/resources/data/";

    public static final String GLOBAL_PROPERTIES_PATH = "src/test/resources/global.properties";

    public static final String ADD_COMMENT_JSON_PAYLOAD_PATH = "src/test/resources/data/json/addComment.json";

    public static final String INVALID_ADD_COMMENT_JSON_PAYLOAD_PATH = "src/test/resources/data/json/invalidAddComment.json";

    private TestConstants() {
        throw new AssertionError("Cannot instantiate TestConstants");
    }
}
