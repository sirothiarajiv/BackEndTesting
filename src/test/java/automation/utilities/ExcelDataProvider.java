package automation.utilities;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelDataProvider {

    private static XSSFWorkbook book;
    private static XSSFRow row;
    private static XSSFCell cell;

    public static Object[][] getEnabledExcelTests(String excelFileName, String sheetName, String executionFlagName) {
        XSSFSheet sheet = readExcelSheet(excelFileName,sheetName);
        List<Map<String, Object>> dataList = getDataAsList(sheet);

        int i=0;
        Object[][] testData = null;
        ArrayList enabledTests = new ArrayList();
        while ( i < dataList.size() ){
            if(dataList.get(i).get(executionFlagName)!=null && dataList.get(i).get(executionFlagName).toString().equalsIgnoreCase("Y")){
                enabledTests.add(dataList.get(i));
            }
            testData = new Object[enabledTests.size()][1];
            i++;
        }
        for(i=0; i< enabledTests.size();i++) {
            testData[i][0]=enabledTests.get(i);
        }
        if(testData == null)
            return new Object[0][0];
        else
            return testData;
    }
    public static XSSFSheet readExcelSheet(String file, String sheetName){
        try{
            FileInputStream excelFile = new FileInputStream(new File(file));
            book = new XSSFWorkbook(excelFile);
        }catch (FileNotFoundException fnfe){
            System.out.println(fnfe.getMessage());
        }catch (IOException ioe){
            ioe.getMessage();
        }
        return book.getSheet(sheetName);
    }
    public static List<Map<String, Object>> getDataAsList(XSSFSheet sheet) {
        List<Map<String, Object>> sheetData = new ArrayList<>();
        int rowCount = sheet.getLastRowNum();
        if (rowCount > 0) {
            ArrayList header = new ArrayList();
            row = sheet.getRow(0);
            int colCount = row.getPhysicalNumberOfCells();
            for (int i = 0; i < colCount; i++) {
                cell = row.getCell(i);
                if (cell.getCellType() == CellType.STRING) {
                    header.add(cell.getStringCellValue());
                }
            }

            DataFormatter fmt = new DataFormatter();
            for (int i = 1; i <= rowCount; i++) {
                Map map = new HashMap<>();
                row = sheet.getRow(i);
                if (row.getCell(1) != null) {
                    for (int j = 0; j < header.size(); j++) {
                        cell = row.getCell(j);
                        if (cell == null) {
                            map.put(header.get(j), null);
                        } else {
                            if (cell.getCellType() == CellType._NONE) {
                                map.put(header.get(j), null);
                            }
                            if (cell.getCellType() == CellType.STRING) {
                                map.put(header.get(j), cell.getStringCellValue());
                            }
                            if (cell.getCellType() == CellType.NUMERIC) {
                                map.put(header.get(j), fmt.formatCellValue(cell));
                            }
                            if (cell.getCellType() == CellType.BLANK) {
                                map.put(header.get(j), "");
                            }
                        }
                    }
                    sheetData.add(map);
                }
            }
        }
        return sheetData;
    }
}
