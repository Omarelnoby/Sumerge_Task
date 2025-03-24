package com.example.data;

import org.testng.annotations.DataProvider;

import com.example.utils.ExcelUtils;

import java.io.IOException;

public class ExcelDataProvider {
    @DataProvider(name = "ExcelData")
    public static Object[][] getData() throws IOException {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata.xlsx";
        String sheetName = "Sheet1";  // Set your sheet name
        
        ExcelUtils.loadExcelFile(filePath, sheetName);

        int rows = ExcelUtils.getRowCount();
        int cols = ExcelUtils.getColumnCount(0);

        Object[][] data = new Object[rows - 1][cols]; // Exclude header row

        for (int i = 1; i < rows; i++) { // Start from row 1 to skip header
            for (int j = 0; j < cols; j++) {
                data[i - 1][j] = ExcelUtils.getCellData(i, j);
            }
        }
        return data;
    }
}
