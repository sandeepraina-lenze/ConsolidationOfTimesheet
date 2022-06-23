// 
// Decompiled by Procyon v0.6.0
// 

package com.odc.readexcel;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class ReadExcelFile
{
    public static ArrayList<String> extractExcelContentByColumnIndex(final String filePath, final int columnIndex) {
        ArrayList<String> columndata = null;
        final ArrayList<String> returnColumnData = new ArrayList<String>();
        try {
            final File f = new File(filePath);
            final FileInputStream ios = new FileInputStream(f);
            final Workbook workbook = WorkbookFactory.create((InputStream)ios);
            for (int i = 0; i < workbook.getNumberOfSheets(); ++i) {
                final Sheet sheet = workbook.getSheetAt(i);
                final Iterator<Row> rowIterator = sheet.iterator();
                columndata = new ArrayList<String>();
                while (rowIterator.hasNext()) {
                    final Row row = rowIterator.next();
                    final Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        final Cell cell = cellIterator.next();
                        if (row.getRowNum() > 0 && cell.getColumnIndex() == columnIndex) {
                            switch (cell.getCellType()) {
                                default: {
                                    continue;
                                }
                                case NUMERIC: {
                                    columndata.add(new StringBuilder(String.valueOf(cell.getNumericCellValue())).toString());
                                    continue;
                                }
                                case STRING: {
                                    columndata.add(cell.getStringCellValue());
                                    continue;
                                }
                            }
                        }
                    }
                }
                ios.close();
                returnColumnData.addAll(columndata);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return returnColumnData;
    }
}
