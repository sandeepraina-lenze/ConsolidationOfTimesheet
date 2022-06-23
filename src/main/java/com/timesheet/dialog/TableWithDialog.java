// 
// Decompiled by Procyon v0.6.0
// 

package com.timesheet.dialog;

import javax.swing.table.TableColumnModel;
import com.table.editor.MyComboBoxEditor;
import javax.swing.JComboBox;
import com.table.renderer.ComboTableCellRenderer;
import javax.swing.JTable;

public class TableWithDialog extends JTable
{
    private static final long serialVersionUID = 1L;
    private ComboTableCellRenderer renderer;
    
    public TableWithDialog(final ValidateTimesheetSOW.ComboBoxTableModel model) {
        super(model);
        final JComboBox<String> comboBox = new JComboBox<String>(ValidateTimesheetSOW.ComboBoxTableModel.getValidStates());
        comboBox.setEditable(true);
        final MyComboBoxEditor editor = new MyComboBoxEditor(comboBox);
        comboBox.setRenderer(this.renderer = new ComboTableCellRenderer());
        final TableColumnModel tcm = this.getColumnModel();
        tcm.getColumn(0).setCellEditor(editor);
        tcm.getColumn(0).setCellRenderer(this.renderer);
        tcm.getColumn(0).setPreferredWidth(400);
        this.setRowHeight(20);
        this.setAutoResizeMode(0);
        this.setPreferredScrollableViewportSize(this.getPreferredSize());
        this.setDefaultEditor(String.class, editor);
    }
}
