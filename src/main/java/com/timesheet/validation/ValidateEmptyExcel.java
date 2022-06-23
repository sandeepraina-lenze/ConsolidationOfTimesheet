// 
// Decompiled by Procyon v0.6.0
// 

package com.timesheet.validation;

import org.apache.poi.ss.usermodel.Row;
import java.util.Iterator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ValidateEmptyExcel
{
    private static XSSFWorkbook workbook;
    private static int noOfOSheets;
    private static boolean isExcelEmpty;
    
    public static boolean validateExcel(final String path) {
        ValidateEmptyExcel.isExcelEmpty = true;
        try {
            final File f = new File(path);
            final FileInputStream ios = new FileInputStream(f);
            ValidateEmptyExcel.workbook = new XSSFWorkbook((InputStream)ios);
            ValidateEmptyExcel.noOfOSheets = ValidateEmptyExcel.workbook.getNumberOfSheets();
            while (ValidateEmptyExcel.noOfOSheets > 0) {
                --ValidateEmptyExcel.noOfOSheets;
                final XSSFSheet sheet = ValidateEmptyExcel.workbook.getSheetAt(ValidateEmptyExcel.noOfOSheets);
                final Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    rowIterator.next();
                    ValidateEmptyExcel.isExcelEmpty = false;
                }
            }
            ios.close();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        return ValidateEmptyExcel.isExcelEmpty;
    }
}
