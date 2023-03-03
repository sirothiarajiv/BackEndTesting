package automation.testscenarios;

import automation.utilities.ExcelDataProvider;
import automation.utilities.PetUtilities;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Map;
import static automation.utilities.Constants.excelSheetPath;

/**
 * author @Rajiv Sirothia
 */
public class Pet {

    /**
     * getDataProvider()
     * @return
     */
    @DataProvider
    public Object[][] getDataProvider( ){
        return ExcelDataProvider.getEnabledExcelTests(excelSheetPath
                ,"post-order","SmokeTestFlag");
    }

    /**
     * shouldBeAbleToPostOrderGetOrderDeleteOrderSuccessfully()
     * @param testData
     */
    @Test(dataProvider="getDataProvider")
    public void shouldBeAbleToPostOrderGetOrderDeleteOrderSuccessfully(Map<String, String> testData) {
        Response response = PetUtilities.postPetOrderEndpoint(testData.get("postOrderStatusCode"),testData.get("postOrderEndPoint"),
                testData.get("postOrderBody"), testData.get("postOrderJsonSchema"), testData.get("postOrderPetId"),
                testData.get("postOrderQuantity"),testData.get("postOrderStatus"), testData);
        String postResponse = response.getBody().asString();
        String id = response.jsonPath().getString("id");
        PetUtilities.getOrderByIdEndpoint(testData.get("getOrderStatusCode"),testData.get("getOrderEndPoint"),
                testData.get("getOrderJsonSchema"),testData, postResponse, id);
        PetUtilities.deleteOrderEndpoint(testData.get("deleteOrderStatusCode"),testData.get("deleteOrderEndPoint"),
                testData.get("deleteOrderJsonSchema"),testData, postResponse, id);
    }

    @Test(dataProvider="getDataProvider")
    public void shouldBeAbleToPostOrderDeleteOrderGetOrderSuccessfully(Map<String, String> testData) {
        Response response = PetUtilities.postPetOrderEndpoint(testData.get("postOrderStatusCode"),testData.get("postOrderEndPoint"),
                testData.get("postOrderBody"), testData.get("postOrderJsonSchema"), testData.get("postOrderPetId"),
                testData.get("postOrderQuantity"),testData.get("postOrderStatus"), testData);
        String postResponse = response.getBody().asString();
        String id = response.jsonPath().getString("id");
        PetUtilities.deleteOrderEndpoint(testData.get("deleteOrderStatusCode"),testData.get("deleteOrderEndPoint"),
                testData.get("deleteOrderJsonSchema"),testData, postResponse, id);
    }
}
