// 
// Decompiled by Procyon v0.6.0
// 

package com.odc.readexcel;

import java.util.Set;
import com.timesheet.panels.PanelTwo;
import javax.swing.JOptionPane;
import com.timesheet.panels.PanelOne;
import java.io.File;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSheetValidation
{
    public static Map<String, String> ResourceNamesMap;
    public static boolean alreadyCalled;
    private static Map<String, Map<String, List<Double>>> getOutput;
    
    static {
        TimeSheetValidation.ResourceNamesMap = new HashMap<String, String>();
        TimeSheetValidation.getOutput = new HashMap<String, Map<String, List<Double>>>();
    }
    
    public static boolean isAlreadyCalled() {
        return TimeSheetValidation.alreadyCalled;
    }
    
    public static void setAlreadyCalled(final boolean alreadyCalled) {
        TimeSheetValidation.alreadyCalled = alreadyCalled;
    }
    
    public static Map<String, String> getResourceNamesMap() {
        return TimeSheetValidation.ResourceNamesMap;
    }
    
    public static void setResourceNamesMap(final Map<String, String> resourceNamesMap) {
        TimeSheetValidation.ResourceNamesMap = resourceNamesMap;
    }
    
    public static int DateAndResourceValidation(final String StartDate, final String LastDate, final String ResourceName, final String FolderPath, final String Output_TimeSheet) {
        if (!DateValidation(StartDate, LastDate, ResourceName, FolderPath)) {
            return 2;
        }
        if (isResourceExist(ResourceName, Output_TimeSheet)) {
            return 0;
        }
        return 1;
    }
    
    public static boolean DateValidation(final String StartDate, final String LastDate, final String ResourceName, final String FolderPath) {
        final Timesheet TS = new Timesheet(GetResourceNamesAndFilePaths(FolderPath).get(ResourceName));
        return TS.DateCompare(StartDate, LastDate);
    }
    
    public static boolean isResourceExist(final String ResourceName, final String Output_TimeSheet) {
        HashSet<String> ResourceNames = new HashSet<String>();
        ResourceNames = GetResourceNames_OutputFile(Output_TimeSheet);
        return ResourceNames.contains(getResourceNamesMap().get(ResourceName));
    }
    
    public static HashSet<String> GetResourceNames_OutputFile(final String Output_TimeSheet) {
        HashSet<String> ResourceNames = new HashSet<String>();
        final Timesheet Output_TS = new Timesheet(Output_TimeSheet);
        ResourceNames = Output_TS.GetResourceName();
        return ResourceNames;
    }
    
    public static boolean isSheetFormatCorrect(final String Output_TimeSheet) {
        try {
            final Timesheet Output_TS = new Timesheet(Output_TimeSheet);
            return Output_TS.Validate_OutputSheet(Output_TimeSheet);
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    public static void LoadOutput_Dynamically(final Map<String, Map<String, List<Double>>> OutputFileMap, final String ResourceName, final int flag, final String FolderPath, final String StartDate, final String LastDate) {
        if (flag == 0 || flag == 1) {
            final String ResourceTimesheetPath = GetResourceNamesAndFilePaths(FolderPath).get(ResourceName);
            OutputFileMap.put(ResourceName, load(ResourceTimesheetPath, StartDate, LastDate));
        }
    }
    
    public static void UnloadOutput_Dynamically(final Map<String, Map<String, List<Double>>> OutputFileMap, final String ResourceName) {
        OutputFileMap.remove(ResourceName);
    }
    
    public static HashMap<String, String> GetResourceNamesAndFilePaths(final String FolderPath) {
        final HashMap<String, String> HashMap = new HashMap<String, String>();
        final File folder = new File(FolderPath);
        final File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; ++i) {
            if (listOfFiles[i].isFile() && (listOfFiles[i].getName().endsWith(".xlsx") || listOfFiles[i].getName().endsWith(".xls"))) {
                final String TimeSheetPath = String.valueOf(FolderPath) + "\\" + listOfFiles[i].getName();
                final Timesheet timesheet = new Timesheet(TimeSheetPath);
                if (timesheet.Validate_InputSheet(TimeSheetPath)) {
                    if (timesheet.getResourceName() == null) {
                        return null;
                    }
                    HashMap.put(timesheet.getResourceName(), TimeSheetPath);
                }
            }
        }
        final PanelOne panelOne = new PanelOne();
        if (listOfFiles.length != HashMap.size() && TimeSheetValidation.alreadyCalled) {
            TimeSheetValidation.alreadyCalled = false;
            final int selectedOption = JOptionPane.showConfirmDialog(panelOne, "There are invalid timesheets in the folder do you want to load only the valid files? ", "Alert", 2);
            if (selectedOption == 2) {
                final PanelTwo panelTwo = new PanelTwo();
                panelTwo.removeTable();
                return null;
            }
        }
        return HashMap;
    }
    
    public static Set<String> GetResourceName(final String FolderPath) {
        final HashMap<String, String> GetResorceNames = GetResourceNamesAndFilePaths(FolderPath);
        if (GetResorceNames == null) {
            return null;
        }
        final Set<String> Names = GetResorceNames.keySet();
        for (final String Name : Names) {
            TimeSheetValidation.ResourceNamesMap.put(Name, Name);
        }
        return Names;
    }
    
    public static Map<String, Map<String, List<Double>>> GetDynamicOutputData(final String OutputTemplateFilePath) {
        final Timesheet OutputFiles = new Timesheet(OutputTemplateFilePath);
        final Reader_XLS OutputFile = new Reader_XLS(OutputTemplateFilePath);
        if (OutputFile.validateExcel(OutputTemplateFilePath)) {
            return new HashMap<String, Map<String, List<Double>>>();
        }
        if (OutputFiles.Validate_OutputSheet(OutputTemplateFilePath)) {
            TimeSheetValidation.getOutput.putAll(OutputFiles.GetOutputSheetData());
            final Set<String> Names = GetResourceNames_OutputFile(OutputTemplateFilePath);
            for (final String Name : Names) {
                TimeSheetValidation.ResourceNamesMap.put(Name, Name);
            }
            return TimeSheetValidation.getOutput;
        }
        return null;
    }
    
    public static Map<String, List<Double>> load(final String TimeSheetPaths, final String StartDate, final String LastDate) {
        HashSet<String> ht = new HashSet<String>();
        final Timesheet T = new Timesheet(TimeSheetPaths);
        ht = T.getSOWNames(ht);
        final Map<String, List<Double>> SOW_Based_TS = T.TotalTime(ht, StartDate, LastDate);
        return SOW_Based_TS;
    }
    
    public static HashSet<String> InputSowNamesPerResource(final String Path) {
        final HashSet SOWs = new HashSet();
        final Timesheet TS = new Timesheet(Path);
        return TS.getSOWNames(SOWs);
    }
}
