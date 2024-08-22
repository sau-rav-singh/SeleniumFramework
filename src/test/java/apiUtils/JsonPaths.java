package apiUtils;

import java.util.HashMap;
import java.util.Map;

public class JsonPaths {
    private Map<String, String> fieldToJsonPathMap;

    public JsonPaths() {
        fieldToJsonPathMap = new HashMap<>();
        fieldToJsonPathMap.put("Display Name", "author.displayName");
        fieldToJsonPathMap.put("Comment", "body.content[0].content[0].text");
        fieldToJsonPathMap.put("Update Author Email Address", "updateAuthor.emailAddress");
    }

    public String getJsonPathForField(String field) {
        String jsonPath = fieldToJsonPathMap.get(field);
        if (jsonPath == null) {
            throw new IllegalArgumentException("Invalid field name: " + field);
        }
        return jsonPath;
    }
}
