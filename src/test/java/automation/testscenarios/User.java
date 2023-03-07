package automation.testscenarios;

import automation.utilities.ExcelDataProvider;
import automation.utilities.UserUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Map;
import static automation.utilities.Constants.excelSheetPath;

/**
 * author @ Rajiv Sirothia
 */
public class User {

    private static Logger logger = LoggerFactory.getLogger(User.class);

    /**
     * getDataProviderUser()
     * @return
     */
    @DataProvider
    public Object[][] getDataProviderForUser(){
        return ExcelDataProvider.getEnabledExcelTests(excelSheetPath
                ,"user","SmokeTestFlag");
    }

    /**
     * shouldBeAbleToPostUserGetUser()
     * @param testData
     */
    @Test(dataProvider="getDataProviderForUser")
    public void shouldBeAbleToPostUserGetUser(Map<String, String> testData) {
         UserUtilities.postUserEndpoint(testData.get("postUserStatusCode"),testData.get("postUserEndPoint"),
                testData.get("postUserBody"), testData.get("postUserJsonSchema"), testData);
         logger.info("User posted successfully.");
         UserUtilities.getUserByUsernameEndpoint(testData.get("getUserStatusCode"),testData.get("getUserEndPoint"),
                testData.get("getUserJsonSchema"),testData.get("username"),testData);
         logger.info("Get User by username executed successfully.");
    }

    /**
     * shouldBeAbleToPostUserDeleteUserGetUser()
     * @param testData
     */
    @Test(dataProvider="getDataProviderForUser")
    public void shouldBeAbleToPostUserDeleteUserGetUser(Map<String, String> testData){
        String userName = UserUtilities.postUserEndpoint(testData.get("postUserStatusCode"),testData.get("postUserEndPoint"),
                testData.get("postUserBody"), testData.get("postUserJsonSchema"), testData).jsonPath().getString("username");
        logger.info("User posted successfully.");
        UserUtilities.deleteUserEndpoint(testData.get("deleteUserStatusCode"),testData.get("deleteUserEndPoint"),
             userName, testData.get("deleteUserJsonSchema"),testData);
        logger.info("User deleted successfully");
        UserUtilities.getUserByUsernameEndpoint(testData.get("getDeletedUserStatusCode"),testData.get("getUserEndPoint"),
                testData.get("getDeletedUserJsonSchema"), userName,testData);
        logger.info("Get User by username executed successfully for negative scenario.");
    }
}
