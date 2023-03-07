package automation.testscenarios;

import automation.utilities.ExcelDataProvider;
import automation.utilities.PetUtilities;
import automation.utilities.UserUtilities;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Map;
import static automation.utilities.Constants.excelSheetPath;

/**
 * author @Rajiv Sirothia
 */
public class Pet {

    private static Logger logger = LoggerFactory.getLogger(Pet.class);
    /**
     * getDataProvider()
     * @return
     */
    @DataProvider
    public Object[][] getDataProviderForPet( ){
        return ExcelDataProvider.getEnabledExcelTests(excelSheetPath
                ,"post-order","SmokeTestFlag");
    }

    /**
     * shouldBeAbleToPostOrderGetOrderDeleteOrder()
     * @param testData
     */
    @Test(dataProvider="getDataProviderForPet")
    public void shouldBeAbleToPostOrderGetOrderDeleteOrder(Map<String, String> testData) {
        Response response = PetUtilities.postPetOrderEndpoint(testData.get("postOrderStatusCode"),testData.get("postOrderEndPoint"),
                testData.get("postOrderBody"), testData.get("postOrderJsonSchema"), testData.get("postOrderPetId"),
                testData.get("postOrderQuantity"),testData.get("postOrderStatus"), testData);
        logger.info("Order posted successfully.");
        String postResponse = response.getBody().asString();
        String id = response.jsonPath().getString("id");
        PetUtilities.getOrderByIdEndpoint(testData.get("getOrderStatusCode"),testData.get("getOrderEndPoint"),
                testData.get("getOrderJsonSchema"),testData, postResponse, id);
        logger.info("Get order by id performed successfully.");
        PetUtilities.deleteOrderEndpoint(testData.get("deleteOrderStatusCode"),testData.get("deleteOrderEndPoint"),
                testData.get("deleteOrderJsonSchema"),testData, postResponse, id);
        logger.info("Order deleted successfully.");
    }

    /**
     * shouldBeAbleToPostOrderDeleteOrderGetOrder()
     * @param testData
     */
    @Test(dataProvider="getDataProviderForPet")
    public void shouldBeAbleToPostOrderDeleteOrderGetOrder(Map<String, String> testData) {
        Response response = PetUtilities.postPetOrderEndpoint(testData.get("postOrderStatusCode"),testData.get("postOrderEndPoint"),
                testData.get("postOrderBody"), testData.get("postOrderJsonSchema"), testData.get("postOrderPetId"),
                testData.get("postOrderQuantity"),testData.get("postOrderStatus"), testData);
        logger.info("Order posted successfully.");
        String postResponse = response.getBody().asString();
        String id = response.jsonPath().getString("id");
        PetUtilities.deleteOrderEndpoint(testData.get("deleteOrderStatusCode"),testData.get("deleteOrderEndPoint"),
                testData.get("deleteOrderJsonSchema"),testData, postResponse, id);
        logger.info("Order deleted successfully.");
        PetUtilities.getOrderByIdEndpoint(testData.get("getOrderStatusCodeAfterDeletion"),testData.get("getOrderEndPoint"),
                testData.get("getOrderJsonSchemaAfterDeletion"),testData, postResponse, id);
        logger.info("Get order by id executed successfully for negative scenario.");
    }
}
