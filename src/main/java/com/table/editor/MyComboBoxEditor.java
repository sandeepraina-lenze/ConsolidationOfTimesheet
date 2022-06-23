// 
// Decompiled by Procyon v0.6.0
// 

package com.table.editor;

import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;

public class MyComboBoxEditor extends DefaultCellEditor
{
    private static final long serialVersionUID = 1L;
    
    public MyComboBoxEditor(final JComboBox<String> combobox) {
        super(combobox);
    }
}
