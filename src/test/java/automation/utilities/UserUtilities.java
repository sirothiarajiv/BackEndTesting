package automation.utilities;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import java.util.Map;

import static automation.utilities.Constants.baseURL;
import static io.restassured.RestAssured.given;

public class UserUtilities {
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
        response
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema)).log().all();

        System.out.println("Response time for Post user API is  "+response.getTime() + " ms");

            if (testData.get("postUserStatusCode").equals("200")) {
                Assert.assertEquals(response.jsonPath().getString("code"), "200");
                Assert.assertEquals(response.jsonPath().getString("type"),"unknown");
            }

        return response;
    }

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
        response
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema)).log().all();

        System.out.println("Response time for Get Order By Id API is  "+response.getTime() + " ms");

        if(testData.get("getUserStatusCode").equals("200")){
            Assert.assertEquals(response.jsonPath().getString("username"), testData.get("username"));
            Assert.assertEquals(response.jsonPath().getString("firstName"), testData.get("firstName"));
            Assert.assertEquals(response.jsonPath().getString("lastName"), testData.get("lastName"));
            Assert.assertEquals(response.jsonPath().getString("email"), testData.get("email"));
            Assert.assertEquals(response.jsonPath().getString("phone"), testData.get("phone"));
        }
        return response;
    }

    public static  Response deleteUserEndpoint(String statusCode, String endpoint,String jsonSchema,
                                                Map<String,String> testData) {

        System.out.println(baseURL+endpoint+testData.get("username"));
        Response response = given()
                .headers("Content-type","application/json")
                .and()
                .when()
                .delete(baseURL+endpoint+testData.get("username"))
                .then()
                .extract()
                .response();
        response
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema)).log().all();

        System.out.println("Response time for delete order API is "+response.getTime() + " ms");

        if(testData.get("deleteUserStatusCode").equals("200")){
            Assert.assertEquals(response.jsonPath().getString("code"),"200");
            Assert.assertEquals(response.jsonPath().getString("message"), testData.get("username"));
            Assert.assertEquals(response.jsonPath().getString("type"), "unknown");
        }

        return response;
    }
}
