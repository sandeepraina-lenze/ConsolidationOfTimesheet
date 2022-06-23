// 
// Decompiled by Procyon v0.6.0
// 

package com.timesheet.panels;

import com.odc.readexcel.TimeSheetValidation;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.Set;
import javax.swing.table.JTableHeader;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class PanelThree
{
    public JPanel createThirdPanel() {
        final JPanel thirdPanel = new JPanel();
        thirdPanel.setLayout(new BorderLayout());
        return thirdPanel;
    }
    
    public JScrollPane createResource(final JTable table) {
        final JScrollPane scrollPane = new JScrollPane(table);
        return scrollPane;
    }
    
    public JTable createResourceTable() {
        final JTable table = new JTable(this.prepareTableData(), this.getColumnNames().toArray());
        table.setAutoResizeMode(0);
        table.getTableHeader().setReorderingAllowed(false);
        final JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Serif", 1, 15));
        table.setEnabled(false);
        return table;
    }
    
    private Set<String> getResourceNames() {
        return PanelOne.OutputFileMap.keySet();
    }
    
    private Set<String> getSOWNames() {
        final Set<String> sowNames = new TreeSet<String>();
        for (final String resourceName : this.getResourceNames()) {
            final Set<String> sowNameSet = PanelOne.OutputFileMap.get(resourceName).keySet();
            for (final String sowName : sowNameSet) {
                sowNames.add(sowName);
            }
        }
        final Set<String> updatedSowNames = new LinkedHashSet<String>();
        for (final String name : sowNames) {
            if (!name.trim().equals("Total Hr")) {
                updatedSowNames.add(name);
            }
        }
        updatedSowNames.add("Total Hr");
        return updatedSowNames;
    }
    
    private Object[][] prepareTableData() {
        final int rowNo = this.getSOWNames().size();
        final int columnNo = this.getColumnNames().size();
        final Object[][] data = new Object[rowNo][columnNo];
        final Object[] SOWNames = this.getSOWNames().toArray();
        for (int row = 0; row < rowNo; ++row) {
            data[row][0] = SOWNames[row];
        }
        int columnPosition = 1;
        for (int position = 0; position < this.getResourceNames().size(); ++position) {
            final String resourceName = (String)this.getResourceNames().toArray()[position];
            final Map<String, List<Double>> sowMap = PanelOne.OutputFileMap.get(resourceName);
            int row2 = 0;
            for (final String sowName : this.getSOWNames()) {
                final List<Double> list = sowMap.get(sowName);
                if (list != null) {
                    data[row2][columnPosition] = list.get(0);
                    data[row2][columnPosition + 1] = list.get(1);
                }
                else {
                    data[row2][columnPosition] = 0.0;
                    data[row2][columnPosition + 1] = 0.0;
                }
                ++row2;
            }
            columnPosition += 2;
        }
        final double[] sowTotal = this.sowTotalHr();
        for (int position2 = 0; position2 < sowTotal.length; ++position2) {
            data[position2][this.getColumnNames().size() - 1] = sowTotal[position2];
        }
        return data;
    }
    
    private void updateResourcePercentageTotalCellWithBlankData(final Object[][] data) {
        for (int i = 2; i < data[data.length - 1].length - 1; i += 2) {
            data[data.length - 1][i] = "";
        }
    }
    
    private void updateResourcePercentageTotalCell(final Object[][] data) {
    }
    
    private double[] sowTotalHr() {
        int index = 0;
        final double[] sowTotalArray = new double[this.getSOWNames().size()];
        for (final String sowName : this.getSOWNames()) {
            double sowTotal = 0.0;
            for (final String name : this.getResourceNames()) {
                final Map<String, List<Double>> map = PanelOne.OutputFileMap.get(name);
                final List<Double> list = map.get(sowName);
                if (list != null) {
                    sowTotal += list.get(0);
                }
                else {
                    sowTotal += 0.0;
                }
            }
            sowTotalArray[index] = sowTotal;
            ++index;
        }
        return sowTotalArray;
    }
    
    private Set<String> getColumnNames() {
        final Set<String> columnNames = new LinkedHashSet<String>();
        columnNames.add("SOW Names");
        for (final String resourceName : this.getResourceNames()) {
            columnNames.add(TimeSheetValidation.ResourceNamesMap.get(resourceName));
            columnNames.add(String.valueOf(TimeSheetValidation.ResourceNamesMap.get(resourceName)) + "(%)");
        }
        columnNames.add("Total SOW HR");
        return columnNames;
    }
}
