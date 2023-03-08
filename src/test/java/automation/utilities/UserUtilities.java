package automation.utilities;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import java.util.Map;
import static automation.utilities.Constants.baseURL;
import static io.restassured.RestAssured.given;

public class UserUtilities {

    private static Logger logger = LoggerFactory.getLogger(UserUtilities.class);

    /**
     *
     * @param statusCode
     * @param endpoint
     * @param payload
     * @param jsonSchema
     * @param testData
     * @return
     */
    public static Response postUserEndpoint(String statusCode, String endpoint, String payload,
                                                String jsonSchema , Map<String,String> testData ) {

        Response response = given()
                .headers("Content-type","application/json")
                .and()
                .body(payload)
                .when()
                .post(baseURL+endpoint)
                .then()
                .extract()
                .response();
            if (response.getStatusCode() == 200) {
                logger.info("Status Validated.");

                response
                        .then()
                        .assertThat()
                        .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
                logger.info("JSON Schema Validated.");

                Assert.assertEquals(response.jsonPath().getString("code"), "200");
                Assert.assertEquals(response.jsonPath().getString("type"),"unknown");
                logger.info("Response Body Validated.");
            }
        return response;
    }

    /**
     *
     * @param statusCode
     * @param endpoint
     * @param jsonSchema
     * @param username
     * @param testData
     * @return
     */
    public static Response getUserByUsernameEndpoint(String statusCode, String endpoint,
                                                String jsonSchema, String username,
                                                Map<String,String> testData) {
        Response response = given()
                .headers("Content-type","application/json")
                .and()
                .when()
                .get(baseURL+endpoint+username)
                .then()
                .extract()
                .response();
        if(response.getStatusCode() == 200){
            logger.info("Status Validated.");

            response
                    .then()
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
            logger.info("JSON Schema Validated.");

            Assert.assertEquals(response.jsonPath().getString("username"), testData.get("username"));
            Assert.assertEquals(response.jsonPath().getString("firstName"), testData.get("firstName"));
            Assert.assertEquals(response.jsonPath().getString("lastName"), testData.get("lastName"));
            Assert.assertEquals(response.jsonPath().getString("email"), testData.get("email"));
            Assert.assertEquals(response.jsonPath().getString("phone"), testData.get("phone"));
            logger.info("Response Body Validated.");
        } else if(response.getStatusCode() == 404){
            Assert.assertEquals(response.jsonPath().getString("type"), "error");
            Assert.assertEquals(response.jsonPath().getString("message"),"User not found");
            logger.info("Response Body Validated for negative test case.");
        }
        return response;
    }

    /**
     *
     * @param statusCode
     * @param endpoint
     * @param username
     * @param jsonSchema
     * @param testData
     * @return
     */
    public static  Response deleteUserEndpoint(String statusCode, String endpoint, String username, String jsonSchema,
                                                Map<String,String> testData) {
        Response response = given()
                .headers("Content-type","application/json")
                .and()
                .when()
                .delete(baseURL+endpoint+username)
                .then()
                .extract()
                .response();
        if(response.getStatusCode() == 200){
            logger.info("Status Validated.");
            response
                    .then()
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
            logger.info("JSON Schema Validated.");

            Assert.assertEquals(response.jsonPath().getString("code"),"200");
            Assert.assertEquals(response.jsonPath().getString("message"), username);
            Assert.assertEquals(response.jsonPath().getString("type"), "unknown");
            logger.info("Response Body Validated.");
        }
        return response;
    }
}
