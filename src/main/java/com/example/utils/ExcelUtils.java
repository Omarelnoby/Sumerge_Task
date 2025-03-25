package com.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {
    private static Workbook workbook;
    private static Sheet sheet;

    // Load Excel file
    public static void loadExcelFile(String filePath, String sheetName) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new IOException("Excel file not found at: " + filePath);
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(fileInputStream);
            sheet = workbook.getSheet(sheetName);
    
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' does not exist in the Excel file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Excel file: " + e.getMessage());
        }
    }
    

    // Get cell data as String
    public static String getCellData(int rowNumber, int cellNumber) {
        if (sheet == null) {
            throw new IllegalStateException("Excel file not loaded. Call loadExcelFile first.");
        }
        Row row = sheet.getRow(rowNumber);
        if (row != null) {
            Cell cell = row.getCell(cellNumber);
            if (cell == null) {
                return "";
            }
    
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        return String.valueOf((int) cell.getNumericCellValue()); // Convert 2.0 -> 2
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                case BLANK:
                    return "";
                default:
                    return cell.toString();
            }
        }
        return "";
    }

    // Get total number of rows
    public static int getRowCount() {
        if (sheet == null) {
            throw new IllegalStateException("Excel file not loaded. Call loadExcelFile first.");
        }
        return sheet.getPhysicalNumberOfRows();
    }

    // Get total number of columns in a row
    public static int getColumnCount(int rowNumber) {
        if (sheet == null) {
            throw new IllegalStateException("Excel file not loaded. Call loadExcelFile first.");
        }
        Row row = sheet.getRow(rowNumber);
        return row != null ? row.getPhysicalNumberOfCells() : 0;
    }

    // Close the workbook
    public static void closeWorkbook() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
