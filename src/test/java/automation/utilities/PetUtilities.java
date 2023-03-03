package automation.utilities;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import java.util.Map;
import static automation.utilities.Constants.baseURL;
import static io.restassured.RestAssured.given;

/**
 * author @ Rajiv Sirothia
 */
public class PetUtilities {

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
        response
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema)).log().all();

        System.out.println("Response time for Post order for Pet API is  "+response.getTime() + " ms");

        if(testData.get("postorderStatus").equals("200")){
            Assert.assertEquals(response.jsonPath().getString("petId"), testData.get("postOrderpetId"));
            Assert.assertEquals(response.jsonPath().getString("quantity"), testData.get("postOrderQuantity"));
            Assert.assertEquals(response.jsonPath().getString("status"), testData.get("postorderStatus"));
            Assert.assertEquals(response.jsonPath().getString("complete"), true);
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
        response
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema)).log().all();

        System.out.println("Response time for Get Order By Id API is  "+response.getTime() + " ms");

        if(testData.get("getOrderStatusCode").equals("200")){
            JSONObject postResponseObject = new JSONObject(postResponse);
            Assert.assertEquals(response.jsonPath().getInt("petId"), postResponseObject.get("petId"));
            Assert.assertEquals(response.jsonPath().getInt("quantity"), postResponseObject.get("quantity"));
            Assert.assertEquals(response.jsonPath().getString("status"), postResponseObject.get("status"));
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
        response
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema)).log().all();

        System.out.println("Response time for delete order API is "+response.getTime() + " ms");

        if(testData.get("deleteOrderStatusCode").equals("200")){
            Assert.assertEquals(response.jsonPath().getString("code"), "200");
            Assert.assertEquals(response.jsonPath().getString("type"), "unknown");
        }

        return response;
    }
}
