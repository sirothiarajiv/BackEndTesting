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
    public Object[][] getDataProviderUser(){
        return ExcelDataProvider.getEnabledExcelTests(excelSheetPath
                ,"user","SmokeTestFlag");
    }

    /**
     * shouldBeAbleToPostUserGetUserSuccessfully()
     * @param testData
     */
    @Test(dataProvider="getDataProviderUser")
    public void shouldBeAbleToPostUserGetUserSuccessfully(Map<String, String> testData) {
         UserUtilities.postUserEndpoint(testData.get("postUserStatusCode"),testData.get("postUserEndPoint"),
                testData.get("postUserBody"), testData.get("postUserJsonSchema"), testData);
         UserUtilities.getUserByUsernameEndpoint(testData.get("getUserStatusCode"),testData.get("getUserEndPoint"),
                testData.get("getUserJsonSchema"),testData.get("username"),testData);
    }

    /**
     * shouldBeAbleToPostUserDeleteUserGetUserSuccessfully()
     * @param testData
     */
    @Test(dataProvider="getDataProviderUser")
    public void shouldBeAbleToPostUserDeleteUserGetUserSuccessfully(Map<String, String> testData){
     UserUtilities.postUserEndpoint(testData.get("postUserStatusCode"),testData.get("postUserEndPoint"),
                testData.get("postUserBody"), testData.get("postUserJsonSchema"), testData);
     UserUtilities.deleteUserEndpoint(testData.get("deleteUserStatusCode"),testData.get("deleteUserEndPoint"),
                testData.get("deleteUserJsonSchema"),testData);
//     UserUtilities.getUserByUsernameEndpoint(testData.get("getUserStatusCodeAfterDeletion"),testData.get("getUserEndPoint"),
//                testData.get("deleteUserJsonSchema"),testData.get("username"),testData);
    }
}
