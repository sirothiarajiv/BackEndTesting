package automation.utilities;

import io.restassured.http.Headers;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import javax.mail.Header;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static automation.utilities.Constants.baseURL;
import static io.restassured.RestAssured.given;

/**
 * author @ Rajiv Sirothia
 */
public class PetUtilities {

    private static Logger logger = LoggerFactory.getLogger(PetUtilities.class);
    /**
     *
     * @param statusCode
     * @param endpoint
     * @param payload
     * @param jsonSchema
     * @param petId
     * @param quantity
     * @param status
     * @param testData
     * @return
     */
    public static Response postPetOrderEndpoint(String statusCode, String endpoint, String payload,
                                             String jsonSchema , String petId, String quantity,
                                                String status,Map<String,String> testData ) {

        Response response = given()
                .headers("Content-type","application/json")
                .and()
                .body(payload)
                .when()
                .post(baseURL+endpoint)
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

            Assert.assertEquals(response.getHeader("Transfer-Encoding"),"chunked");
            Assert.assertEquals(response.getHeader("Connection"),"keep-alive");
            Assert.assertEquals(response.getHeader("Access-Control-Allow-Methods"),"GET, POST, DELETE, PUT");
            Assert.assertEquals(response.getStatusLine(),"HTTP/1.1 200 OK");
            logger.info("Response Headers Validated.");

            Assert.assertEquals(response.jsonPath().getString("petId"), testData.get("postOrderpetId"));
            Assert.assertEquals(response.jsonPath().getString("quantity"), testData.get("postOrderQuantity"));
            Assert.assertEquals(response.jsonPath().getString("status"), testData.get("postorderStatus"));
            Assert.assertEquals(response.jsonPath().getBoolean("complete"), true);
            logger.info("Response Body Validated.");
        }
        return response;
    }

    /**
     *
     * @param statusCode
     * @param endpoint
     * @param jsonSchema
     * @param testData
     * @param postResponse
     * @param id
     * @return
     */
    public static Response getOrderByIdEndpoint(String statusCode, String endpoint,
                                                String jsonSchema,
                                                Map<String,String> testData, String postResponse, String id) {

        Response response = given()
                .headers("Content-type","application/json")
                .and()
                .when()
                .get(baseURL+endpoint+id)
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

            Assert.assertEquals(response.getHeader("Transfer-Encoding"),"chunked");
            Assert.assertEquals(response.getHeader("Connection"),"keep-alive");
            Assert.assertEquals(response.getHeader("Access-Control-Allow-Methods"),"GET, POST, DELETE, PUT");
            Assert.assertEquals(response.getStatusLine(),"HTTP/1.1 200 OK");
            logger.info("Response Headers Validated.");

            JSONObject postResponseObject = new JSONObject(postResponse);
            Assert.assertEquals(response.jsonPath().getInt("petId"), postResponseObject.getInt("petId"));
            Assert.assertEquals(response.jsonPath().getInt("quantity"), postResponseObject.getInt("quantity"));
            Assert.assertEquals(response.jsonPath().getString("status"), postResponseObject.getString("status"));
            logger.info("Response Body Validated.");
        } else if (response.getStatusCode() == 404){
            logger.info("Not Found Scenario Status Validated.");

            Assert.assertEquals(response.jsonPath().getString("type"),"error");
            Assert.assertEquals(response.jsonPath().getString("message"),"Order not found");
            logger.info("Response Body Validated For Negative Scenario.");
        }
        return response;
    }

    /**
     *
     * @param statusCode
     * @param endpoint
     * @param jsonSchema
     * @param testData
     * @param postResponse
     * @param id
     * @return
     */
    public static  Response deleteOrderEndpoint(String statusCode, String endpoint,String jsonSchema,
                                                Map<String,String> testData,String postResponse, String id) {

        Response response = given()
                .headers("Content-type","application/json")
                .and()
                .when()
                .delete(baseURL+endpoint+id)
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

            Assert.assertEquals(response.getHeader("Transfer-Encoding"),"chunked");
            Assert.assertEquals(response.getHeader("Connection"),"keep-alive");
            Assert.assertEquals(response.getHeader("Access-Control-Allow-Methods"),"GET, POST, DELETE, PUT");
            Assert.assertEquals(response.getStatusLine(),"HTTP/1.1 200 OK");
            logger.info("Response Headers Validated.");

            Assert.assertEquals(response.jsonPath().getString("code"), "200");
            Assert.assertEquals(response.jsonPath().getString("type"), "unknown");
            logger.info("Response Body Validated.");
        }
        return response;
    }
}
