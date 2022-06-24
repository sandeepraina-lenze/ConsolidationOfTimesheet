// 
// Decompiled by Procyon v0.6.0
// 

package com.odc.readexcel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class ReadExcelFile
{
    public static ArrayList<String> extractExcelContentByColumnIndex(final String filePath, final int columnIndex) {
        ArrayList<String> columndata = null;
        final ArrayList<String> returnColumnData = new ArrayList<String>();
        try {
//            final File f = new File(filePath);
//            final FileInputStream ios = new FileInputStream(f);
            Workbook workbook = null;

            if (filePath.toString().endsWith(".xls")) {
                POIFSFileSystem fs = new POIFSFileSystem(new File(filePath));
                workbook = new HSSFWorkbook(fs.getRoot(), true);
                fs.close();
            } else {
                // XSSFWorkbook, File
                OPCPackage pkg = OPCPackage.open(new File(filePath));
                workbook = new XSSFWorkbook(pkg);
                pkg.close();
            }

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
//                ios.close();
                returnColumnData.addAll(columndata);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return returnColumnData;
    }
}
