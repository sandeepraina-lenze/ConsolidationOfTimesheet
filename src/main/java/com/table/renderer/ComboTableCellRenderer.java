// 
// Decompiled by Procyon v0.6.0
// 

package com.table.renderer;

import com.timesheet.dialog.ValidateTimesheetSOW;
import javax.swing.JTable;
import java.awt.Component;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.DefaultListCellRenderer;
import com.timesheet.panels.PanelOne;
import javax.swing.table.TableCellRenderer;
import javax.swing.ListCellRenderer;

public class ComboTableCellRenderer implements ListCellRenderer, TableCellRenderer
{
    static boolean found;
    PanelOne panelOne;
    DefaultListCellRenderer listRenderer;
    DefaultTableCellRenderer tableRenderer;
    
    static {
        ComboTableCellRenderer.found = false;
    }
    
    public ComboTableCellRenderer() {
        this.panelOne = new PanelOne();
        this.listRenderer = new DefaultListCellRenderer();
        this.tableRenderer = new DefaultTableCellRenderer();
    }
    
    public static boolean isFound() {
        return ComboTableCellRenderer.found;
    }
    
    private void configureRenderer(final JLabel renderer, final Object value) {
        if (value == null) {
            return;
        }
        final String validateSOWMatch = (String)value;
        if (validateSOWMatch.contains(",")) {
            renderer.setText(validateSOWMatch.substring(0, validateSOWMatch.indexOf(44)));
            renderer.setForeground(Color.RED);
        }
        else {
            renderer.setText((String)value);
            renderer.setForeground(Color.BLACK);
        }
    }
    
    @Override
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        this.configureRenderer(this.listRenderer = (DefaultListCellRenderer)this.listRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus), value);
        return this.listRenderer;
    }
    
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        ComboTableCellRenderer.found = false;
        if (isSelected) {
            final Object[][] comboData = ValidateTimesheetSOW.getGlobalData();
            for (int i = 0; i < comboData.length; ++i) {
                if (comboData[i][0].toString().equalsIgnoreCase(value.toString()) && i != row) {
                    ComboTableCellRenderer.found = true;
                    break;
                }
                for (int j = i + 1; j < comboData.length; ++j) {
                    if (comboData[j][0].toString().equalsIgnoreCase(comboData[i][0].toString())) {
                        ComboTableCellRenderer.found = true;
                        break;
                    }
                }
            }
        }
        this.configureRenderer(this.tableRenderer = (DefaultTableCellRenderer)this.tableRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column), value);
        return this.tableRenderer;
    }
}
