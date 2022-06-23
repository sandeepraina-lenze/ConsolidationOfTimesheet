// 
// Decompiled by Procyon v0.6.0
// 

package com.odc.readexcel;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import java.io.OutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class Reader_XLS
{
    public String path;
    private Workbook workbook;
    private Sheet sheet;
    private Row row;
    private Cell cell;
    public FileInputStream FIS;
    public FileOutputStream FOS;
    private boolean isExcelEmpty;
    
    public Reader_XLS(final String path) {
        this.path = null;
        this.workbook = null;
        this.sheet = null;
        this.row = null;
        this.cell = null;
        this.FIS = null;
        this.FOS = null;
        this.path = path;
        try {
            this.FIS = new FileInputStream(path);
            this.workbook = WorkbookFactory.create((InputStream)this.FIS);
            this.sheet = this.workbook.getSheetAt(0);
            this.FIS.close();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public String[] getSheetNames() {
        final String[] sheetNumbers = new String[this.workbook.getNumberOfSheets()];
        for (int i = 0; i < sheetNumbers.length; ++i) {
            sheetNumbers[i] = this.workbook.getSheetName(i);
        }
        return sheetNumbers;
    }
    
    public int getRowCount(final String sheetName) {
        final int index = this.workbook.getSheetIndex(sheetName);
        if (index == -1) {
            return 0;
        }
        this.sheet = this.workbook.getSheetAt(index);
        return this.sheet.getLastRowNum() + 1;
    }
    
    public int getColumnCount(final String SheetName) {
        if (this.isSheetExist(SheetName)) {
            this.sheet = this.workbook.getSheet(SheetName);
            this.row = this.sheet.getRow(0);
            return this.row.getLastCellNum();
        }
        return 0;
    }
    
    public boolean isSheetExist(final String SheetName) {
        final int index = this.workbook.getSheetIndex(SheetName);
        return index != -1;
    }
    
    public Object getCellData(final String SheetName, final int RowNum, final int ColNum) {
        try {
            if (RowNum <= 0 || ColNum <= 0) {
                return "RowNumber or ColNum is invalid";
            }
            final int index = this.workbook.getSheetIndex(SheetName);
            if (index == -1) {
                return "Sheet doesnt exist";
            }
            this.sheet = this.workbook.getSheetAt(index);
            this.row = this.sheet.getRow(RowNum - 1);
            if (this.row == null) {
                return null;
            }
            if (this.row.getCell(ColNum - 1) == null) {
                return null;
            }
            if (this.row.getCell(ColNum - 1).getCellType() == CellType.STRING) {
                return this.row.getCell(ColNum - 1).getStringCellValue();
            }
            if (this.row.getCell(ColNum - 1).getCellType() == CellType.NUMERIC) {
                return this.row.getCell(ColNum - 1).getDateCellValue();
            }
            return this.row.getCell(ColNum - 1).getDateCellValue();
        }
        catch (final Exception e) {
            e.printStackTrace();
            return "The cell number and col number doesnt exist";
        }
    }
    
    public boolean isRowExist(final String SheetName, final int RowNum) {
        final int index = this.workbook.getSheetIndex(SheetName);
        if (index == -1) {
            return false;
        }
        this.sheet = this.workbook.getSheetAt(index);
        this.row = this.sheet.getRow(RowNum - 1);
        return this.row != null;
    }
    
    public boolean setCellValue(final String SheetName, final int ColNum, final int RowNum, final String Data) {
        try {
            if (RowNum <= 0) {
                return false;
            }
            if (ColNum <= 0) {
                return false;
            }
            this.FIS = new FileInputStream(this.path);
            this.workbook = (Workbook)new XSSFWorkbook((InputStream)this.FIS);
            if (this.isSheetExist(SheetName)) {
                this.sheet = this.workbook.getSheet(SheetName);
                this.row = this.sheet.getRow(RowNum - 1);
                if (this.row == null) {
                    this.row = this.sheet.createRow(RowNum - 1);
                }
                this.cell = this.row.getCell(ColNum - 1);
                if (this.cell == null) {
                    this.cell = this.row.createCell(ColNum - 1);
                }
                this.cell.setCellValue(Data);
                this.FOS = new FileOutputStream(this.path);
                this.workbook.write((OutputStream)this.FOS);
                this.FOS.close();
                return true;
            }
            return false;
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    public boolean setCellValue(final String SheetName, final int ColNum, final int RowNum, final double Data) {
        try {
            if (RowNum <= 0) {
                return false;
            }
            if (ColNum <= 0) {
                return false;
            }
            this.FIS = new FileInputStream(this.path);
            this.workbook = (Workbook)new XSSFWorkbook((InputStream)this.FIS);
            if (this.isSheetExist(SheetName)) {
                this.sheet = this.workbook.getSheet(SheetName);
                this.row = this.sheet.getRow(RowNum - 1);
                if (this.row == null) {
                    this.row = this.sheet.createRow(RowNum - 1);
                }
                this.cell = this.row.getCell(ColNum - 1);
                if (this.cell == null) {
                    this.cell = this.row.createCell(ColNum - 1);
                }
                this.cell.setCellValue(Data);
                this.FOS = new FileOutputStream(this.path);
                this.workbook.write((OutputStream)this.FOS);
                this.FOS.close();
                return true;
            }
            return false;
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    public Object getIntegerCellData(final String SheetName, final int RowNum, final int ColNum) {
        try {
            if (RowNum <= 0 || ColNum <= 0) {
                return "RowNumber or ColNum is invalid";
            }
            final int index = this.workbook.getSheetIndex(SheetName);
            if (index == -1) {
                return "Sheet doesnt exist";
            }
            this.sheet = this.workbook.getSheetAt(index);
            this.row = this.sheet.getRow(RowNum - 1);
            this.cell = this.row.getCell(ColNum - 1);
            if (this.cell == null) {
                return 0.0;
            }
            if (this.cell.getCellType() == CellType.NUMERIC || this.cell.getCellType() == CellType.FORMULA) {
                return this.cell.getNumericCellValue();
            }
            return 0.0;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return "The cell number and col number doesnt exist";
        }
    }
    
    public void Clear() {
        try {
            int i = 0;
            this.FIS = new FileInputStream(this.path);
            this.workbook = (Workbook)new XSSFWorkbook((InputStream)this.FIS);
            for (XSSFSheet sheet = (XSSFSheet)this.workbook.getSheetAt(0); this.getRowCount(sheet.getSheetName()) > i; ++i) {
                if (sheet.getRow(i) != null) {
                    sheet.removeRow((Row)sheet.getRow(i));
                }
            }
            this.FOS = new FileOutputStream(this.path);
            this.workbook.write((OutputStream)this.FOS);
            this.FOS.close();
        }
        catch (final Exception ex) {}
    }
    
    public boolean validateExcel(final String path) {
        this.isExcelEmpty = true;
        try {
            int noOfOSheets = this.workbook.getNumberOfSheets();
            while (noOfOSheets > 0) {
                --noOfOSheets;
                this.sheet = this.workbook.getSheetAt(noOfOSheets);
                final Iterator<Row> rowIterator = this.sheet.iterator();
                while (rowIterator.hasNext()) {
                    rowIterator.next();
                    this.isExcelEmpty = false;
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return this.isExcelEmpty;
    }
}
