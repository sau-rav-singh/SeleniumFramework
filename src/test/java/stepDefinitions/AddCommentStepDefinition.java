package stepDefinitions;

import apiUtils.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import java.util.List;
import java.util.Map;

public class AddCommentStepDefinition {
    private final CreatePayload createPayload;
    private final JsonPaths jsonpaths;
    private final ExcelSheetReader excelSheetReader;
    private String updatedJsonPayload;
    private String postResponse;
    private Response addCommentResponse;

    public AddCommentStepDefinition() {
        createPayload = new CreatePayload();
        excelSheetReader = ExcelSheetManager.getExcelSheetReader();
        jsonpaths = new JsonPaths();
    }

    @Then("Read comment as{string} to create a valid addCommentToBug payload as{string}")
    public void createAddCommentPayload(String comment, String payloadName) {
        comment = excelSheetReader.readCell(comment);
        try {
            updatedJsonPayload = (payloadName.equals("addCommentToBug"))
                    ? createPayload.addCommentPayload(comment)
                    : createPayload.invalidAddCommentPayload(comment);
        } catch (Exception e) {
            System.out.println("Error creating payload: " + e.getMessage());
        }
    }

    @Then("Read comment as{string} to create an invalid addCommentToBug payload as{string}")
    public void createInvalidAddCommentPayload(String comment, String payloadName) {
        createAddCommentPayload(comment, payloadName);
    }

    @When("The {string} request is sent with the {string} HTTP method on IssueID as {string}")
    public void request_is_sent_with_http_method_on_issueid(String resource, String httpMethod, String issueID) {
        issueID = excelSheetReader.readCell(issueID);
        if (httpMethod.equalsIgnoreCase("POST")) {
            addCommentResponse = SpecBuilders.SendPostAndReturnResponse(updatedJsonPayload, issueID, resource);
        }
    }

    @When("The {string} request is sent with the {string} HTTP method on a non-existing IssueID as {string}")
    public void the_request_is_sent_with_the_http_method_on_a_non_existing_issue_id_as(String resource,
                                                                                       String httpMethod, String issueID) {
        request_is_sent_with_http_method_on_issueid(resource, httpMethod, issueID);
    }

    @Then("Validate that the response status code is {string}")
    public void the_response_status_code_should_be_something(String responseCode) {
        postResponse = SpecBuilders.responseSpecification(addCommentResponse).statusCode(Integer.parseInt(responseCode))
                .extract().response().body().asString();
    }

    @Then("Validate the following fields from the response:")
    public void validate_the_following_fields_from_the_response(DataTable dataTable) {
        JsonPath js = new JsonPath(postResponse);
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String field = row.get("Field");
            String expectedValue = excelSheetReader.readCell(row.get("Value"));
            String actualValue = js.get(jsonpaths.getJsonPathForField(field));
            Assert.assertEquals(expectedValue, actualValue);
        }
    }
}