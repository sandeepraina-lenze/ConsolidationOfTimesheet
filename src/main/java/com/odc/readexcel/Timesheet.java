// 
// Decompiled by Procyon v0.6.0
// 

package com.odc.readexcel;

import com.timesheet.panels.DatePanel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Timesheet
{
    Reader_XLS TS;
    String[] sheets;
    String Path;
    public FileInputStream FIS;
    public FileOutputStream FOS;
    
    public Timesheet(final String Path) {
        this.TS = null;
        this.sheets = null;
        this.FIS = null;
        this.FOS = null;
        this.Path = Path;
        this.TS = new Reader_XLS(Path);
        this.sheets = this.TS.getSheetNames();
    }
    
    public String getResourceName() {
        String ResourceName = null;
        ResourceName = (String)this.TS.getCellData(this.sheets[0], 2, 2);
        return ResourceName;
    }
    
    public HashSet<String> GetResourceName() {
        final HashSet<String> Names = new HashSet<String>();
        for (int Column = 2; this.TS.getCellData(this.sheets[0], 2, Column) != null; Column += 2) {
            Names.add((String)this.TS.getCellData(this.sheets[0], 2, Column));
        }
        return Names;
    }
    
    public static HashMap<String, String> GetResourceNamesAndFilePaths(final String FolderPath) {
        final HashMap<String, String> HashMap = new HashMap<String, String>();
        final File folder = new File(FolderPath);
        final File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; ++i) {
            if (listOfFiles[i].isFile() && (listOfFiles[i].getName().endsWith(".xlsx") || listOfFiles[i].getName().endsWith(".xls"))) {
                final String TimeSheetPath = String.valueOf(FolderPath) + "\\" + listOfFiles[i].getName();
                final Timesheet timesheet = new Timesheet(TimeSheetPath);
                if (timesheet.getResourceName() == null) {
                    return null;
                }
                HashMap.put(timesheet.getResourceName(), TimeSheetPath);
            }
        }
        return HashMap;
    }
    
    public Date getStartDate() throws ParseException {
        final String[] sheets = this.TS.getSheetNames();
        Date StartDate = null;
        StartDate = (Date)this.TS.getCellData(sheets[0], 3, 2);
        return StartDate;
    }
    
    public Date getLastDate() {
        final String[] sheets = this.TS.getSheetNames();
        Date LastDate = null;
        LastDate = (Date)this.TS.getCellData(sheets[sheets.length - 1], 6, 9);
        return LastDate;
    }
    
    public HashSet<String> getSOWNames(final HashSet<String> SOWs) {
        final int RowCountOfSOWs = 1;
        for (int i = 0; i < this.sheets.length; ++i) {
            for (int RowCount = this.TS.getRowCount(this.sheets[i]), j = 8; j <= RowCount; ++j) {
                if (this.TS.getCellData(this.sheets[i], j, 1) != null && this.TS.getCellData(this.sheets[i], j, 1).equals("SOW") && this.TS.getCellData(this.sheets[i], j, 2) != null) {
                    SOWs.add(this.TS.getCellData(this.sheets[i], j, 2).toString().trim());
                }
            }
        }
        return SOWs;
    }
    
    public Map<String, List<Double>> TotalTime(final HashSet<String> sets, final String StartDate, final String LastDate) {
        final HashMap<String, List<Double>> SOW_TimePercent_Mapping = new HashMap<String, List<Double>>();
        final HashMap<String, Double> SOW_Time_Mapping = new HashMap<String, Double>();
        double TotalHoursPerResource = 0.0;
        Iterator<String> iter = sets.iterator();
        while (iter.hasNext()) {
            SOW_Time_Mapping.put(iter.next().toString(), 0.0);
        }
        for (int SheetIndex = 0; SheetIndex < this.sheets.length; ++SheetIndex) {
            final int RowCount = this.TS.getRowCount(this.sheets[SheetIndex]);
            for (int DateCellColumn = 3; DateCellColumn < 10; ++DateCellColumn) {
                if (this.DateCompare(StartDate, LastDate, (Date)this.TS.getCellData(this.sheets[SheetIndex], 6, DateCellColumn))) {
                    for (int SheetRow = 8; SheetRow <= RowCount; ++SheetRow) {
                        if (this.TS.isRowExist(this.sheets[SheetIndex], SheetRow) && this.TS.getCellData(this.sheets[SheetIndex], SheetRow, 1) != null && this.TS.getCellData(this.sheets[SheetIndex], SheetRow, 1).equals("SOW")) {
                            final String keys = this.TS.getCellData(this.sheets[SheetIndex], SheetRow, 2).toString();
                            Double sum = 0.0;
                            for (int y = SheetRow + 1; y <= RowCount; ++y) {
                                if (this.TS.isRowExist(this.sheets[SheetIndex], y) && this.TS.getCellData(this.sheets[SheetIndex], y, 1) != null) {
                                    if (this.TS.getCellData(this.sheets[SheetIndex], y, 1).equals("SOW")) {
                                        break;
                                    }
                                    sum += (double)this.TS.getIntegerCellData(this.sheets[SheetIndex], y, DateCellColumn);
                                }
                            }
                            TotalHoursPerResource += sum;
                            SOW_Time_Mapping.put(keys.trim(), SOW_Time_Mapping.get(keys.trim()) + sum);
                        }
                    }
                }
            }
        }
        SOW_Time_Mapping.put("Total Hr", TotalHoursPerResource);
        iter = SOW_Time_Mapping.keySet().iterator();
        while (iter.hasNext()) {
            final List<Double> Data = new ArrayList<Double>();
            final String key = iter.next();
            Data.add(SOW_Time_Mapping.get(key));
            double percent = SOW_Time_Mapping.get(key) / DatePanel.getTotalWorkingHours() * 100.0;
            percent = Math.round(percent * 100.0) / 100.0;
            Data.add(percent);
            SOW_TimePercent_Mapping.put(key, Data);
        }
        return SOW_TimePercent_Mapping;
    }
    
    public void Display_Timesheet(final HashMap<String, Double> TS) {
        final Set set = TS.entrySet();
        for (Object entry : set) {}
    }
    
    public boolean DateCompare(final String StartDate, final String LastDate) {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        int flag = 0;
        try {
            final Date startdate = formatter.parse(StartDate);
            final Date lastdate = formatter.parse(LastDate);
            if (startdate.compareTo(this.getStartDate()) < 0 && lastdate.compareTo(this.getStartDate()) < 0) {
                ++flag;
            }
            if (lastdate.compareTo(this.getLastDate()) > 0 && startdate.compareTo(this.getLastDate()) > 0) {
                flag += 2;
            }
            return flag != 1 && flag != 2 && flag != 3;
        }
        catch (final ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean DateCompare(final String StartDate, final String LastDate, final Date CellDate) {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        int flag = 0;
        try {
            final Date startdate = formatter.parse(StartDate);
            final Date lastdate = formatter.parse(LastDate);
            if (startdate.compareTo(CellDate) > 0) {
                ++flag;
            }
            if (lastdate.compareTo(CellDate) < 0) {
                flag += 2;
            }
            return flag != 1 && flag != 2 && flag != 3;
        }
        catch (final ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Validate_OutputSheet(final String Path) {
        try {
            if (this.TS.validateExcel(Path)) {
                return true;
            }
            final int ColumnCount = this.TS.getColumnCount(this.TS.getSheetNames()[0]);
            int RowNum = 0;
            final String[] TemplateCellNames = { "Report for the Month", "ResourceName", "SOW Name", "Total Hr" };
            for (RowNum = 1; RowNum <= 3; ++RowNum) {
                if (!this.TS.getCellData(this.TS.getSheetNames()[0], RowNum, 1).equals(TemplateCellNames[RowNum - 1])) {
                    return false;
                }
            }
            for (RowNum = 4; RowNum < this.TS.getRowCount(this.TS.getSheetNames()[0]); ++RowNum) {
                if (this.TS.getCellData(this.TS.getSheetNames()[0], RowNum, 1) == null) {
                    return false;
                }
            }
            if (!this.TS.getCellData(this.TS.getSheetNames()[0], RowNum, 1).equals("Total Hr")) {
                return false;
            }
            for (int ColumnNum = 2; !this.TS.getCellData(this.TS.getSheetNames()[0], 3, ColumnNum).equals("Total Hr"); ++ColumnNum) {
                if (ColumnNum % 2 == 0) {
                    if (this.TS.getCellData(this.TS.getSheetNames()[0], 2, ColumnNum) == null && !this.TS.getCellData(this.TS.getSheetNames()[0], 3, ColumnNum).equals("Actual Hr")) {
                        return false;
                    }
                }
                else if (this.TS.getCellData(this.TS.getSheetNames()[0], 2, ColumnNum) != null & !this.TS.getCellData(this.TS.getSheetNames()[0], 3, ColumnNum).equals("Percentage(%)")) {
                    return false;
                }
            }
            return true;
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    public boolean Validate_InputSheet(final String Path) {
        final Set<String> resourseName = new HashSet<String>();
        try {
            if (this.TS.validateExcel(Path)) {
                return false;
            }
            final Set<String> ResourceName = new HashSet<String>();
            for (int SheetCount = this.TS.getSheetNames().length, SheetNumber = 0; SheetNumber < SheetCount; ++SheetNumber) {
                final int ColumnCount = this.TS.getColumnCount(this.TS.getSheetNames()[SheetNumber]);
                final int RowNum = 0;
                final String SheetName = this.TS.getSheetNames()[SheetNumber];
                final String TimeSheetNamecell = "T I M E   S H E E T";
                final String ResourceNameCell = "Resource Name:";
                final String WeekStartDateCell = "Week Start Date";
                final String WeekNoCell = "Week No.";
                final String TaskDescriptionCell = "Task Description";
                if (!this.TS.getCellData(SheetName, 1, 1).equals(TimeSheetNamecell) || !this.TS.getCellData(SheetName, 2, 1).equals(ResourceNameCell) || !this.TS.getCellData(SheetName, 3, 1).equals(WeekStartDateCell) || !this.TS.getCellData(SheetName, 3, 3).equals(WeekNoCell) || !this.TS.getCellData(SheetName, 4, 2).equals(TaskDescriptionCell)) {
                    return false;
                }
                if (!this.TS.getCellData(SheetName, 4, 3).equals("Week Days") || !this.TS.getCellData(SheetName, 7, 2).equals("Hours of the day") || this.TS.getCellData(SheetName, 2, 2) == null) {
                    return false;
                }
                ResourceName.add((String)this.TS.getCellData(SheetName, 2, 2));
            }
            return ResourceName.size() == 1;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Map<String, Map<String, List<Double>>> GetOutputSheetData() {
        Map<String, List<Double>> GetEachResourceData = null;
        final Map<String, Map<String, List<Double>>> OutputData = new HashMap<String, Map<String, List<Double>>>();
        for (int ColumnNumber = 2; this.TS.getCellData(this.TS.getSheetNames()[0], 2, ColumnNumber) != null; ColumnNumber += 2) {
            GetEachResourceData = new HashMap<String, List<Double>>();
            final String ResourceName = (String)this.TS.getCellData(this.TS.getSheetNames()[0], 2, ColumnNumber);
            List<Double> SowBasedData = null;
            int RowNum;
            for (RowNum = 4; !this.TS.getCellData(this.TS.getSheetNames()[0], RowNum, 1).equals("Total Hr"); ++RowNum) {
                SowBasedData = new ArrayList<Double>();
                final String SOW_Name = (String)this.TS.getCellData(this.TS.getSheetNames()[0], RowNum, 1);
                double Percent = 0.0;
                final double Hours = (double)this.TS.getIntegerCellData(this.TS.getSheetNames()[0], RowNum, ColumnNumber);
                try {
                    Percent = (double)this.TS.getIntegerCellData(this.TS.getSheetNames()[0], RowNum, ColumnNumber + 1);
                }
                catch (final Exception e) {
                    Percent = 0.0;
                }
                SowBasedData.add(Hours);
                SowBasedData.add(Percent);
                GetEachResourceData.put(SOW_Name, SowBasedData);
            }
            final double Hours2 = (double)this.TS.getIntegerCellData(this.TS.getSheetNames()[0], RowNum, ColumnNumber);
            SowBasedData = new ArrayList<Double>();
            SowBasedData.add(Hours2);
            SowBasedData.add(100.0);
            GetEachResourceData.put("Total Hr", SowBasedData);
            OutputData.put(ResourceName, GetEachResourceData);
        }
        return OutputData;
    }
    
    public void TemplateSet(final Map<String, Map<String, List<Double>>> ht) throws IOException {
        this.TS.Clear();
        final String Sheetname = this.TS.getSheetNames()[0];
        int SOW_StartRowNum = 4;
        int ResourceName_StartColumnNum = 2;
        final String TotalHour = "Total Hr";
        final String ActualHour = "Actual Hr";
        final String Percentage = "Percentage(%)";
        this.FIS = new FileInputStream(this.Path);
        final Workbook workbook = (Workbook)new XSSFWorkbook((InputStream)this.FIS);
        this.setCellValue(workbook, Sheetname, 1, 1, "Report for the Month");
        this.formatting(workbook, Sheetname, 1, 1, 14, "Arial", 1, "BLANK");
        this.setCellValue(workbook, Sheetname, 1, 2, "ResourceName");
        this.formatting(workbook, Sheetname, 2, 1, 11, "Arial", 1, "BLANK");
        this.setCellValue(workbook, Sheetname, 1, 3, "SOW Name");
        this.formatting(workbook, Sheetname, 3, 1, 10, "Arial", 1, "GRAY");
        final Set<String> ResourceNames = ht.keySet();
        final Set SOW_Names = new TreeSet();

        List<String> EachNames = new ArrayList<>(ResourceNames); // Copy to a List
        Collections.sort(EachNames); // Sort the list


        // Now iterate over sorted list
        for (String EachName : EachNames) {
            SOW_Names.addAll(ht.get(EachName).keySet());
        }

        SOW_Names.remove(TotalHour);
        final Iterator<String> EachSOWName = SOW_Names.iterator();
        while (EachSOWName.hasNext()) {
            this.setCellValue(workbook, Sheetname, 1, SOW_StartRowNum, EachSOWName.next());
            this.formatting(workbook, Sheetname, SOW_StartRowNum, 1, 10, "Arial", 1, "AQUA");
            ++SOW_StartRowNum;
        }
        this.setCellValue(workbook, Sheetname, 1, SOW_StartRowNum, TotalHour);
        this.formatting(workbook, Sheetname, SOW_StartRowNum, 1, 10, "Arial", 1, "YELLOW");

        for (String EachName : EachNames) {
            Double TotalHoursPerResource = 0.0;
            final String ResourceName = EachName;
            final String ResourceChangedName = TimeSheetValidation.ResourceNamesMap.get(ResourceName);
            this.setCellValue(workbook, Sheetname, ResourceName_StartColumnNum, 2, ResourceChangedName);
            this.setCellValue(workbook, Sheetname, ResourceName_StartColumnNum + 1, 2, ResourceChangedName);
            this.formatting(workbook, Sheetname, 2, ResourceName_StartColumnNum, 10, "Arial", 1, "BLANK");
            this.formatting(workbook, Sheetname, 2, ResourceName_StartColumnNum + 1, 10, "Arial", 1, "BLANK");
            this.setCellValue(workbook, Sheetname, ResourceName_StartColumnNum, 3, ActualHour);
            this.formatting(workbook, Sheetname, 3, ResourceName_StartColumnNum, 10, "Arial", 1, "GRAY");
            this.setCellValue(workbook, Sheetname, ResourceName_StartColumnNum + 1, 3, Percentage);
            this.formatting(workbook, Sheetname, 3, ResourceName_StartColumnNum + 1, 10, "Arial", 1, "GRAY");
            final Map<String, List<Double>> TimePerSOW = ht.get(ResourceName);
            TimePerSOW.remove(TotalHour);
            for (SOW_StartRowNum = 4; !this.getCellData(workbook, Sheetname, SOW_StartRowNum, 1).equals(TotalHour); ++SOW_StartRowNum) {
                try {
                    final String SOW_Name = (String)this.getCellData(workbook, Sheetname, SOW_StartRowNum, 1);
                    if (TimePerSOW.containsKey(SOW_Name)) {
                        final Double hours = TimePerSOW.get(SOW_Name).get(0);
                        this.setCellValue(workbook, Sheetname, ResourceName_StartColumnNum, SOW_StartRowNum, hours);
                        this.formatting(workbook, Sheetname, SOW_StartRowNum, ResourceName_StartColumnNum, 10, "Arial", 2, "BLANK");
                        TotalHoursPerResource += hours;
                    }
                    else {
                        this.setCellValue(workbook, Sheetname, ResourceName_StartColumnNum, SOW_StartRowNum, 0.0);
                        this.formatting(workbook, Sheetname, SOW_StartRowNum, ResourceName_StartColumnNum, 10, "Arial", 2, "BLANK");
                    }
                }
                catch (final Exception E) {
                    E.printStackTrace();
                    break;
                }
            }
            double TotalPercent = 0.0;
            this.setCellValue(workbook, Sheetname, ResourceName_StartColumnNum, SOW_StartRowNum, TotalHoursPerResource);
            this.formatting(workbook, Sheetname, SOW_StartRowNum, ResourceName_StartColumnNum, 10, "Arial", 1, "YELLOW");
            for (SOW_StartRowNum = 4; !this.getCellData(workbook, Sheetname, SOW_StartRowNum, 1).equals(TotalHour); ++SOW_StartRowNum) {
                Double Percent = 0.0;
                final Double Resourcehours = (Double)this.getIntegerCellData(workbook, Sheetname, SOW_StartRowNum, ResourceName_StartColumnNum);
                if (DatePanel.getTotalWorkingHours() != 0.0) {
                    Percent = Resourcehours / DatePanel.getTotalWorkingHours() * 100.0;
                }
                Percent = Math.round(Percent * 100.0) / 100.0;
                TotalPercent += Percent;
                this.setCellValue(workbook, Sheetname, ResourceName_StartColumnNum + 1, SOW_StartRowNum, Percent);
                this.formatting(workbook, Sheetname, SOW_StartRowNum, ResourceName_StartColumnNum + 1, 10, "Arial", 2, "BLANK");
            }
            this.setCellValue(workbook, Sheetname, ResourceName_StartColumnNum + 1, SOW_StartRowNum, Double.valueOf(Math.round(TotalPercent)));
            this.formatting(workbook, Sheetname, SOW_StartRowNum, ResourceName_StartColumnNum + 1, 10, "Arial", 1, "YELLOW");
            ResourceName_StartColumnNum += 2;
        }
        this.formatting(workbook, Sheetname, 2, ResourceName_StartColumnNum, 10, "Arial", 1, "BLANK");
        this.setCellValue(workbook, Sheetname, ResourceName_StartColumnNum, 3, TotalHour);
        this.formatting(workbook, Sheetname, 3, ResourceName_StartColumnNum, 10, "Arial", 1, "GRAY");
        for (SOW_StartRowNum = 4; !this.getCellData(workbook, Sheetname, SOW_StartRowNum, 1).equals(TotalHour); ++SOW_StartRowNum) {
            int Column = 2;
            double total = 0.0;
            while (Column < ResourceName_StartColumnNum) {
                total += (double)this.getIntegerCellData(workbook, Sheetname, SOW_StartRowNum, Column);
                Column += 2;
            }
            this.setCellValue(workbook, Sheetname, Column, SOW_StartRowNum, total);
            this.formatting(workbook, Sheetname, SOW_StartRowNum, Column, 10, "Arial", 1, "BLANK");
        }
        int Column = 2;
        double total = 0.0;
        while (Column < ResourceName_StartColumnNum) {
            total += (double)this.getIntegerCellData(workbook, Sheetname, SOW_StartRowNum, Column);
            Column += 2;
        }
        this.setCellValue(workbook, Sheetname, Column, SOW_StartRowNum, total);
        this.formatting(workbook, Sheetname, SOW_StartRowNum, ResourceName_StartColumnNum, 10, "Arial", 1, "YELLOW");
        workbook.write((OutputStream)(this.FOS = new FileOutputStream(this.Path)));
        this.FOS.close();
    }
    
    public boolean setCellValue(final Workbook workbook, final String SheetName, final int ColNum, final int RowNum, final String Data) {
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        try {
            if (RowNum <= 0) {
                return false;
            }
            if (ColNum <= 0) {
                return false;
            }
            if (this.TS.isSheetExist(SheetName)) {
                sheet = workbook.getSheet(SheetName);
                row = sheet.getRow(RowNum - 1);
                if (row == null) {
                    row = sheet.createRow(RowNum - 1);
                }
                cell = row.getCell(ColNum - 1);
                if (cell == null) {
                    cell = row.createCell(ColNum - 1);
                }
                cell.setCellValue(Data);
                return true;
            }
            return false;
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    public boolean setCellValue(final Workbook workbook, final String SheetName, final int ColNum, final int RowNum, final Double Data) {
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        try {
            if (RowNum <= 0) {
                return false;
            }
            if (ColNum <= 0) {
                return false;
            }
            if (this.TS.isSheetExist(SheetName)) {
                sheet = workbook.getSheet(SheetName);
                row = sheet.getRow(RowNum - 1);
                if (row == null) {
                    row = sheet.createRow(RowNum - 1);
                }
                cell = row.getCell(ColNum - 1);
                if (cell == null) {
                    cell = row.createCell(ColNum - 1);
                }
                cell.setCellValue((double)Data);
                return true;
            }
            return false;
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    public Object getCellData(final Workbook workbook, final String SheetName, final int RowNum, final int ColNum) {
        Sheet sheet = null;
        Row row = null;
        final Cell cell = null;
        try {
            if (RowNum <= 0 || ColNum <= 0) {
                return "RowNumber or ColNum is invalid";
            }
            final int index = workbook.getSheetIndex(SheetName);
            if (index == -1) {
                return "Sheet doesnt exist";
            }
            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(RowNum - 1);
            if (row == null) {
                return null;
            }
            if (row.getCell(ColNum - 1) == null) {
                return null;
            }
            if (row.getCell(ColNum - 1).getCellType() == CellType.STRING) {
                return row.getCell(ColNum - 1).getStringCellValue();
            }
            if (row.getCell(ColNum - 1).getCellType() == CellType.NUMERIC) {
                return row.getCell(ColNum - 1).getDateCellValue();
            }
            return row.getCell(ColNum - 1).getDateCellValue();
        }
        catch (final Exception e) {
            e.printStackTrace();
            return "The cell number and col number doesnt exist";
        }
    }
    
    public Object getIntegerCellData(final Workbook workbook, final String SheetName, final int RowNum, final int ColNum) {
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        try {
            if (RowNum <= 0 || ColNum <= 0) {
                return "RowNumber or ColNum is invalid";
            }
            final int index = workbook.getSheetIndex(SheetName);
            if (index == -1) {
                return "Sheet doesnt exist";
            }
            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(RowNum - 1);
            cell = row.getCell(ColNum - 1);
            if (cell == null) {
                return 0.0;
            }
            if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
                return cell.getNumericCellValue();
            }
            return 0.0;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return "The cell number and col number doesnt exist";
        }
    }
    
    public void formatting(final Workbook workbook, final String sheetName, final int RowNum, final int ColumnNum, final int FontSize, final String FontName, final int Bordor, final String color) {
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        try {
            sheet = workbook.getSheet(sheetName);
            row = sheet.getRow(RowNum - 1);
            if (row.getCell(ColumnNum - 1) == null) {
                row.createCell(ColumnNum - 1);
            }
            cell = row.getCell(ColumnNum - 1);
            final XSSFCellStyle cellStyle = (XSSFCellStyle)workbook.createCellStyle();
            final XSSFFont hSSFFont = (XSSFFont)workbook.createFont();
            hSSFFont.setFontName("Calibri");
            hSSFFont.setFontHeightInPoints((short)FontSize);
            hSSFFont.setBold(true);
            hSSFFont.setFontName(FontName);
            if (Bordor == 1) {
                cellStyle.setBorderBottom(BorderStyle.MEDIUM);
                cellStyle.setBorderTop(BorderStyle.MEDIUM);
                cellStyle.setBorderLeft(BorderStyle.MEDIUM);
                cellStyle.setBorderRight(BorderStyle.MEDIUM);
            }
            else {
                cellStyle.setBorderBottom(BorderStyle.THIN);
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setBorderRight(BorderStyle.THIN);
            }
            if (color.equals("YELLOW")) {
                cellStyle.setFillPattern(FillPatternType.forInt((short)1));
                cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            }
            if (color.equals("GRAY")) {
                cellStyle.setFillPattern(FillPatternType.forInt((short)1));
                cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            }
            if (color.equals("AQUA")) {
                cellStyle.setFillPattern(FillPatternType.forInt((short)1));
                cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            }
            if (color.equals("BLANK")) {
                cellStyle.setFillPattern(FillPatternType.forInt((short)0));
                cellStyle.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            }
            cellStyle.setFont((Font)hSSFFont);
            cell.setCellStyle((CellStyle)cellStyle);
            sheet.autoSizeColumn(ColumnNum);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
