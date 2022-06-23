// 
// Decompiled by Procyon v0.6.0
// 

package com.timesheet.validation;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import com.timesheet.dialog.ValidateTimesheetSOW;
import java.util.Collection;
import com.odc.readexcel.TimeSheetValidation;
import com.odc.readexcel.ReadExcelFile;
import java.util.ArrayList;
import javax.swing.JTable;

public class ValidateSOW
{
    private static String resourceName;
    
    public static void validateTimesheet(final String path, final String sowPath, final JTable table) {
        final List<String> notMatchedSOW = new ArrayList<String>();
        final List<String> sowList = ReadExcelFile.extractExcelContentByColumnIndex(sowPath, 2);
        final Set<String> timesheetSOWList = TimeSheetValidation.InputSowNamesPerResource(path);
        final List<String> finalSOWList = new ArrayList<String>();
        finalSOWList.addAll(timesheetSOWList);
        for (final String element : finalSOWList) {
            if (!sowList.contains(element.trim())) {
                notMatchedSOW.add(element);
            }
        }
        String[] sowNotMatchedElements = new String[notMatchedSOW.size()];
        sowNotMatchedElements = notMatchedSOW.toArray(sowNotMatchedElements);
        String[] resourceSOWElements = new String[finalSOWList.size()];
        resourceSOWElements = finalSOWList.toArray(resourceSOWElements);
        new ValidateTimesheetSOW().run(sowNotMatchedElements, resourceSOWElements, ValidateSOW.resourceName, table);
    }
    
    public static String getResourceName() {
        return ValidateSOW.resourceName;
    }
    
    public static void setResourceName(final String resourceName) {
        ValidateSOW.resourceName = resourceName;
    }
}
