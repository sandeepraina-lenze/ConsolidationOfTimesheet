// 
// Decompiled by Procyon v0.6.0
// 

package com.timesheet.dialog;

import javax.swing.table.AbstractTableModel;
import javax.swing.JOptionPane;
import com.table.renderer.ComboTableCellRenderer;
import com.odc.readexcel.TimeSheetValidation;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.timesheet.panels.PanelTwoTable;
import com.timesheet.panels.PanelTwo;
import com.odc.timesheet.TimesheetContainer;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Container;
import javax.swing.JScrollPane;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.util.ArrayList;
import com.odc.readexcel.ReadExcelFile;
import java.util.Map;
import javax.swing.JLabel;
import com.timesheet.panels.PanelOne;
import javax.swing.JTable;
import java.util.List;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JFrame;
import java.awt.event.ActionListener;

public class ValidateTimesheetSOW implements ActionListener
{
    JFrame mainFrame;
    private static final Insets insets;
    private static final String RESOURCE_NAME = "Resource Name";
    private static final String OK_BUTTON = "Ok";
    private static final String CANCEL_BUTTON = "Cancel";
    private JButton okButton;
    private JButton cancelButton;
    private static String[] sowElements;
    private static String[] values;
    private static List<String> finalValues;
    private JTable jtable;
    private boolean alreadyPresent;
    private PanelOne panelOne;
    private static Object[][] globalData;
    private JLabel label;
    private String globalResourceName;
    private Map<String, String> resourceNamesMapLocal;
    
    static {
        insets = new Insets(0, 0, 0, 0);
    }
    
    public ValidateTimesheetSOW() {
        this.mainFrame = new JFrame();
    }
    
    public static List<String> getFinalValues() {
        return ValidateTimesheetSOW.finalValues;
    }
    
    public static Object[][] getGlobalData() {
        return ValidateTimesheetSOW.globalData;
    }
    
    public void run(final String[] notMatchedElements, final String[] resourceTimesheetSowElements, final String timesheetResourceName, final JTable table) {
        final List<String> list = ReadExcelFile.extractExcelContentByColumnIndex(PanelOne.getSowPath(), 2);
        this.jtable = table;
        ValidateTimesheetSOW.finalValues = new ArrayList<String>();
        ValidateTimesheetSOW.values = new String[list.size()];
        ValidateTimesheetSOW.values = list.toArray(ValidateTimesheetSOW.values);
        this.globalResourceName = timesheetResourceName;
        for (int i = 0; i < notMatchedElements.length; ++i) {
            for (int j = 0; j < resourceTimesheetSowElements.length; ++j) {
                if (resourceTimesheetSowElements[j].trim().equalsIgnoreCase(notMatchedElements[i].trim())) {
                    resourceTimesheetSowElements[j] = String.valueOf(resourceTimesheetSowElements[j]) + ",";
                }
            }
        }
        final JPanel panel = new JPanel();
        ValidateTimesheetSOW.sowElements = resourceTimesheetSowElements;
        for (int k = 0; k < ValidateTimesheetSOW.values.length; ++k) {
            for (int l = 0; l < resourceTimesheetSowElements.length; ++l) {
                if (ValidateTimesheetSOW.values[k].equalsIgnoreCase(resourceTimesheetSowElements[l])) {
                    this.alreadyPresent = true;
                }
            }
            if (!this.alreadyPresent) {
                ValidateTimesheetSOW.finalValues.add(ValidateTimesheetSOW.values[k]);
            }
            this.alreadyPresent = false;
        }
        final ComboBoxTableModel tablemodel = new ComboBoxTableModel();
        final TableWithDialog tableWithDialog = new TableWithDialog(tablemodel);
        this.mainFrame.setLayout(new GridBagLayout());
        final JLabel resourceName = new JLabel();
        resourceName.setText(timesheetResourceName);
        this.label = new JLabel("Resource Name");
        this.okButton = new JButton("Ok");
        this.cancelButton = new JButton("Cancel");
        final GridBagConstraints labelGridBagConstraint = new GridBagConstraints(0, 1, 1, 1, 10.0, 1.0, 0, 0, new Insets(0, 0, 10, 0), 0, 0);
        panel.add(this.label, labelGridBagConstraint);
        final GridBagConstraints resourceNameGridBagConstraint = new GridBagConstraints(1, 1, 1, 1, 10.0, 1.0, 0, 0, new Insets(0, 0, 10, 0), 0, 0);
        panel.add(resourceName, resourceNameGridBagConstraint);
        final GridBagConstraints tableDialogGridConstraint = new GridBagConstraints(2, 2, 2, 1, 10.0, 1.0, 0, 0, new Insets(0, 0, 10, 0), 0, 0);
        panel.add(new JScrollPane(tableWithDialog), tableDialogGridConstraint);
        addComponent(this.mainFrame, this.label, 0, 0, 1, 1, 10, 1);
        addComponent(this.mainFrame, resourceName, 1, 0, 1, 1, 10, 1);
        addComponent(this.mainFrame, panel, 0, 1, 2, 1, 10, 1);
        addComponent(this.mainFrame, this.okButton, 0, 3, 1, 1, 13, 1);
        addComponent(this.mainFrame, this.cancelButton, 1, 3, 1, 1, 14, 1);
        this.okButton.addActionListener(this);
        this.cancelButton.addActionListener(this);
        this.mainFrame.setSize(500, 200);
        this.mainFrame.setResizable(false);
        this.mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent windowEvent) {
                TimesheetContainer.getPanelTwo().setEnabled(true);
                TimesheetContainer.getPanelTwo();
                PanelTwo.getPanelTwoTable();
                PanelTwoTable.getTable().setEnabled(true);
                TimesheetContainer.getPanelTwo();
                PanelTwo.getLoadButton().setEnabled(true);
                ValidateTimesheetSOW.this.mainFrame.dispose();
                ValidateTimesheetSOW.this.jtable.getModel().setValueAt(false, ValidateTimesheetSOW.this.jtable.getSelectedRow(), ValidateTimesheetSOW.this.jtable.getSelectedColumn());
            }
        });
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        final int w = this.mainFrame.getSize().width;
        final int h = this.mainFrame.getSize().height;
        final int x = (dim.width - w) / 2;
        final int y = (dim.height - h) / 2;
        this.mainFrame.setLocation(x, y);
        this.mainFrame.setVisible(true);
        this.mainFrame.pack();
    }
    
    private static void addComponent(final Container container, final Component component, final int gridx, final int gridy, final int gridwidth, final int gridheight, final int anchor, final int fill) {
        final GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0, 1.0, anchor, fill, ValidateTimesheetSOW.insets, 0, 0);
        container.add(component, gbc);
    }
    
    @Override
    public void actionPerformed(final ActionEvent ae) {
        if (ae.getSource() == this.okButton) {
            Map<String, Map<String, List<Double>>> outerMap = new HashMap<String, Map<String, List<Double>>>();
            Map<String, List<Double>> innerMap = new HashMap<String, List<Double>>();
            boolean doNotDispose = true;
            this.panelOne = new PanelOne();
            outerMap = PanelOne.getOutputFileMap();
            this.resourceNamesMapLocal = TimeSheetValidation.getResourceNamesMap();
            String resourceNamekey = null;
            for (final Map.Entry<String, String> e : this.resourceNamesMapLocal.entrySet()) {
                if (e.getValue().toString().equalsIgnoreCase(this.globalResourceName)) {
                    resourceNamekey = e.getKey();
                }
            }
            innerMap = outerMap.get(resourceNamekey);
            if (innerMap != null) {
                for (int row = 0; row < ValidateTimesheetSOW.globalData.length; ++row) {
                    final Object[] rowArray = ValidateTimesheetSOW.globalData[row];
                    String oldSowName = rowArray[1].toString();
                    final String newSowName = rowArray[0].toString();
                    if (newSowName.contains(",")) {
                        doNotDispose = false;
                    }
                    if (!innerMap.containsKey(oldSowName)) {
                        if (oldSowName.contains(",")) {
                            oldSowName = oldSowName.substring(0, oldSowName.indexOf(44));
                        }
                        innerMap.put(newSowName, innerMap.get(oldSowName));
                        innerMap.remove(oldSowName);
                    }
                }
            }
            if (ComboTableCellRenderer.isFound()) {
                JOptionPane.showMessageDialog(this.panelOne, "Duplicate SOW names entered please corrrect them");
                doNotDispose = false;
            }
            if (doNotDispose) {
                this.mainFrame.dispose();
            }
        }
        if (ae.getSource() == this.cancelButton) {
            this.mainFrame.dispose();
            this.jtable.getModel().setValueAt(false, this.jtable.getSelectedRow(), this.jtable.getSelectedColumn());
        }
        TimesheetContainer.getPanelTwo().setEnabled(true);
        TimesheetContainer.getPanelTwo();
        PanelTwo.getPanelTwoTable();
        PanelTwoTable.getTable().setEnabled(true);
        TimesheetContainer.getPanelTwo();
        PanelTwo.getLoadButton().setEnabled(true);
    }
    
    static /* synthetic */ void access$2(final Object[][] globalData) {
        ValidateTimesheetSOW.globalData = globalData;
    }
    
    static class ComboBoxTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        protected static final int COLUMN_COUNT = 1;
        static String[] finalValuesArray;
        protected static String[] validStates;
        protected Object[][] data;
        protected static final String[] columnNames;
        
        static {
            ComboBoxTableModel.finalValuesArray = new String[ValidateTimesheetSOW.finalValues.size()];
            ComboBoxTableModel.validStates = ComboBoxTableModel.finalValuesArray;
            columnNames = new String[] { "SOW_Names" };
        }
        
        public ComboBoxTableModel() {
            final String[] arr = ValidateTimesheetSOW.sowElements;
            ComboBoxTableModel.finalValuesArray = ValidateTimesheetSOW.finalValues.toArray(ComboBoxTableModel.finalValuesArray);
            final Object[][] data2 = new Object[arr.length][2];
            for (int index = 0; index < arr.length; ++index) {
                data2[index][0] = arr[index];
                data2[index][1] = arr[index];
            }
            ValidateTimesheetSOW.access$2(this.data = data2);
            ComboBoxTableModel.validStates = ComboBoxTableModel.finalValuesArray;
        }
        
        @Override
        public int getRowCount() {
            return this.data.length;
        }
        
        @Override
        public int getColumnCount() {
            return 1;
        }
        
        @Override
        public Object getValueAt(final int row, final int column) {
            final String sowName = this.data[row][column].toString();
            return sowName;
        }
        
        @Override
        public Class getColumnClass(final int column) {
            return this.data[0][column].getClass();
        }
        
        @Override
        public String getColumnName(final int column) {
            return ComboBoxTableModel.columnNames[column];
        }
        
        @Override
        public boolean isCellEditable(final int row, final int column) {
            final String resourceName = this.data[row][column].toString();
            return resourceName.contains(",") || ComboTableCellRenderer.isFound();
        }
        
        @Override
        public void setValueAt(final Object value, final int row, final int column) {
            if (this.isValidValue(value)) {
                this.data[row][column] = value;
                this.fireTableRowsUpdated(row, row);
            }
        }
        
        public static String[] getValidStates() {
            return ComboBoxTableModel.validStates;
        }
        
        protected boolean isValidValue(final Object value) {
            if (value instanceof String) {
                final String sValue = (String)value;
                for (int i = 0; i < ComboBoxTableModel.validStates.length; ++i) {
                    if (sValue.equals(ComboBoxTableModel.validStates[i])) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
