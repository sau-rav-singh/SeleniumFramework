package apiUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONException;
import org.json.JSONObject;

public class CreatePayload {

	public String addCommentPayload(String comment) throws JSONException, IOException {

		JSONObject jsonObject = new JSONObject(
				new String(Files.readAllBytes(Paths.get(TestConstants.ADD_COMMENT_JSON_PAYLOAD_PATH))));

		jsonObject.getJSONObject("body").getJSONArray("content").getJSONObject(0).getJSONArray("content")
				.getJSONObject(0).put("text", comment);

		return jsonObject.toString();
	}

	public String invalidAddCommentPayload(String comment) throws JSONException, IOException {

		JSONObject jsonObject = new JSONObject(
				new String(Files.readAllBytes(Paths.get(TestConstants.INVALID_ADD_COMMENT_JSON_PAYLOAD_PATH))));

		jsonObject.getJSONObject("body").getJSONArray("content").getJSONObject(0).getJSONArray("content")
				.getJSONObject(0).put("text", comment);

		return jsonObject.toString();
	}
}
