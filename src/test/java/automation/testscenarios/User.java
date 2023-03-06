package automation.testscenarios;

import automation.utilities.ExcelDataProvider;
import automation.utilities.UserUtilities;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Map;
import static automation.utilities.Constants.excelSheetPath;

/**
 * author @ Rajiv Sirothia
 */
public class User {

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
         UserUtilities.getUserByUsernameEndpoint(testData.get("getUserStatusCode"),testData.get("getUserEndPoint"),
                testData.get("getUserJsonSchema"),testData.get("username"),testData);
    }

    /**
     * shouldBeAbleToPostUserDeleteUserGetUser()
     * @param testData
     */
    @Test(dataProvider="getDataProviderUser")
    public void shouldBeAbleToPostUserDeleteUserGetUser(Map<String, String> testData){
     UserUtilities.postUserEndpoint(testData.get("postUserStatusCode"),testData.get("postUserEndPoint"),
                testData.get("postUserBody"), testData.get("postUserJsonSchema"), testData);
     UserUtilities.deleteUserEndpoint(testData.get("deleteUserStatusCode"),testData.get("deleteUserEndPoint"),
                testData.get("deleteUserJsonSchema"),testData);
     UserUtilities.getUserByUsernameEndpoint(testData.get("getDeletedUserStatusCode"),testData.get("getUserEndPoint"),
                testData.get("getDeletedUserJsonSchema"),testData.get("username"),testData);
    }
}
