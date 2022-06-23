// 
// Decompiled by Procyon v0.6.0
// 

package com.timesheet.panels;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import java.util.Iterator;
import javax.swing.JOptionPane;
import com.timesheet.validation.ValidateSOW;
import java.util.LinkedHashMap;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.io.IOException;
import java.io.File;
import java.awt.Desktop;
import com.odc.readexcel.TimeSheetValidation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.table.JTableHeader;
import java.util.Map;
import javax.swing.JTable;
import com.odc.timesheet.TimesheetContainer;
import java.util.HashMap;
import javax.swing.JPanel;

public class PanelTwoTable extends JPanel
{
    private static final long serialVersionUID = 1L;
    private static String folderPath;
    private static Object[][] globalData;
    private HashMap<String, String> resourceFilePath;
    private HashMap<String, String> resourceFilePathOpen;
    private static PanelOne panelOne;
    private TimesheetContainer timesheetContainer;
    private static JTable table;
    private Map<String, String> ResourceNamesMapLocal;
    private static boolean nameAlreadyExistInTable;
    
    public static JTable getTable() {
        return PanelTwoTable.table;
    }
    
    public PanelTwoTable(final String folderPath) {
        PanelTwoTable.folderPath = folderPath;
        PanelTwoTable.table = new JTable(new MyTableModel()) {
            @Override
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(this.columnModel) {
                    private static final long serialVersionUID = 1L;
                };
            }
        };
        final JPopupMenu popupMenu = new JPopupMenu();
        final JMenuItem deleteItem = new JMenuItem("Edit Timesheet");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                PanelTwoTable.access$7(PanelTwoTable.this, TimeSheetValidation.GetResourceNamesAndFilePaths(folderPath));
                final int row = PanelTwoTable.table.getSelectedRow();
                final int col = PanelTwoTable.table.getSelectedColumn();
                if (col == 0) {
                    final String resource = PanelTwoTable.globalData[row][col].toString();
                    try {
                        try {
                            Desktop.getDesktop().open(new File(PanelTwoTable.this.resourceFilePathOpen.get(resource)));
                        }
                        catch (final IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                    catch (final Exception classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                }
            }
        });
        popupMenu.add(deleteItem);
        PanelTwoTable.table.setComponentPopupMenu(popupMenu);
        PanelTwoTable.table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(final TableModelEvent e) {
                PanelTwoTable.access$9(new PanelOne());
                final int selectedRow = PanelTwoTable.table.getSelectedRow();
                PanelTwoTable.access$10(PanelTwoTable.this, TimeSheetValidation.GetResourceNamesAndFilePaths(folderPath));
                final String resourceName = PanelTwoTable.globalData[selectedRow][0].toString();
                final Map<Integer, String> oldResourceStoreHashMap = new LinkedHashMap<Integer, String>();
                final Iterator<String> setIterator = MyTableModel.resourceSet.iterator();
                int i = 0;
                while (setIterator.hasNext()) {
                    oldResourceStoreHashMap.put(i++, setIterator.next());
                }
                final String oldResourceName = oldResourceStoreHashMap.get(selectedRow);
                PanelTwoTable.access$11(PanelTwoTable.this, TimeSheetValidation.getResourceNamesMap());
                for (final Map.Entry<String, String> e2 : PanelTwoTable.this.ResourceNamesMapLocal.entrySet()) {
                    if (e2.getValue().toString().equalsIgnoreCase(oldResourceName)) {
                        PanelTwoTable.this.ResourceNamesMapLocal.put(e2.getKey(), resourceName);
                    }
                }
                if ((Boolean) PanelTwoTable.globalData[selectedRow][1]) {
                    TimesheetContainer.getPanelTwo().setEnabled(false);
                    TimesheetContainer.getPanelTwo();
                    PanelTwo.getPanelTwoTable();
                    PanelTwoTable.getTable().setEnabled(false);
                    TimesheetContainer.getPanelTwo();
                    PanelTwo.getLoadButton().setEnabled(false);
                    ValidateSOW.setResourceName(resourceName);
                    final int validationResult = TimeSheetValidation.DateAndResourceValidation(TimesheetContainer.getStartDateField(), TimesheetContainer.getEndDateField(), oldResourceName, folderPath, PanelOne.getOutputpath());
                    if (validationResult == 0) {
                        final int selectedOption = JOptionPane.showConfirmDialog(PanelTwoTable.panelOne, "Resource Name Already Exist Do You Want to Override?", "Alert", 2);
                        if (selectedOption == 2) {
                            PanelTwoTable.globalData[selectedRow][1] = false;
                            TimesheetContainer.getPanelTwo().setEnabled(true);
                            TimesheetContainer.getPanelTwo();
                            PanelTwo.getPanelTwoTable();
                            PanelTwoTable.getTable().setEnabled(true);
                            TimesheetContainer.getPanelTwo();
                            PanelTwo.getLoadButton().setEnabled(true);
                        }
                        else if (selectedOption == 0) {
                            TimeSheetValidation.LoadOutput_Dynamically(PanelOne.getOutputFileMap(), oldResourceName, 0, folderPath, TimesheetContainer.getStartDateField(), TimesheetContainer.getEndDateField());
                            ValidateSOW.validateTimesheet(PanelTwoTable.this.resourceFilePath.get(oldResourceName), PanelOne.getSowPath(), PanelTwoTable.table);
                        }
                        else {
                            ValidateSOW.validateTimesheet(PanelTwoTable.this.resourceFilePath.get(oldResourceName), PanelOne.getSowPath(), PanelTwoTable.table);
                        }
                    }
                    else if (validationResult == 2) {
                        JOptionPane.showMessageDialog(PanelTwoTable.panelOne, "Timesheet Start date and End date is not valid");
                        PanelTwoTable.globalData[selectedRow][1] = false;
                        TimesheetContainer.getPanelTwo().setEnabled(true);
                        TimesheetContainer.getPanelTwo();
                        PanelTwo.getPanelTwoTable();
                        PanelTwoTable.getTable().setEnabled(true);
                        TimesheetContainer.getPanelTwo();
                        PanelTwo.getLoadButton().setEnabled(true);
                    }
                    else {
                        TimeSheetValidation.LoadOutput_Dynamically(PanelOne.getOutputFileMap(), oldResourceName, 1, folderPath, TimesheetContainer.getStartDateField(), TimesheetContainer.getEndDateField());
                        ValidateSOW.validateTimesheet(PanelTwoTable.this.resourceFilePath.get(oldResourceName), PanelOne.getSowPath(), PanelTwoTable.table);
                    }
                }
                else {
                    TimeSheetValidation.UnloadOutput_Dynamically(PanelOne.getOutputFileMap(), oldResourceName);
                }
            }
        });
        PanelTwoTable.table.setFillsViewportHeight(true);
        final JScrollPane scrollPane = new JScrollPane(PanelTwoTable.table);
        this.add(scrollPane, "Center");
    }
    
    static /* synthetic */ void access$1(final Object[][] globalData) {
        PanelTwoTable.globalData = globalData;
    }
    
    static /* synthetic */ void access$3(final boolean nameAlreadyExistInTable) {
        PanelTwoTable.nameAlreadyExistInTable = nameAlreadyExistInTable;
    }
    
    static /* synthetic */ void access$7(final PanelTwoTable panelTwoTable, final HashMap resourceFilePathOpen) {
        panelTwoTable.resourceFilePathOpen = resourceFilePathOpen;
    }
    
    static /* synthetic */ void access$9(final PanelOne panelOne) {
        PanelTwoTable.panelOne = panelOne;
    }
    
    static /* synthetic */ void access$10(final PanelTwoTable panelTwoTable, final HashMap resourceFilePath) {
        panelTwoTable.resourceFilePath = resourceFilePath;
    }
    
    static /* synthetic */ void access$11(final PanelTwoTable panelTwoTable, final Map resourceNamesMapLocal) {
        panelTwoTable.ResourceNamesMapLocal = resourceNamesMapLocal;
    }
    
    static class MyTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private String[] columnNames;
        private ArrayList tableList;
        private static Set<String> resourceSet;
        private Object[][] data;
        
        static {
            MyTableModel.resourceSet = new HashSet<String>();
        }
        
        public MyTableModel() {
            this.columnNames = new String[] { "Resource Name", "Select/UnSelect" };
            this.tableList = new ArrayList();
            MyTableModel.resourceSet = TimeSheetValidation.GetResourceName(PanelTwoTable.folderPath);
            if (MyTableModel.resourceSet != null) {
                final Iterator itr = MyTableModel.resourceSet.iterator();
                while (itr.hasNext()) {
                    this.tableList.add(itr.next());
                }
            }
            final Object[][] data2 = new Object[this.tableList.size()][2];
            for (int index = 0; index < this.tableList.size(); ++index) {
                data2[index][0] = this.tableList.get(index).toString();

                Boolean boolValue = Boolean.valueOf(false);
                data2[index][1] = boolValue;
            }
            PanelTwoTable.access$1(this.data = data2);
        }
        
        @Override
        public int getColumnCount() {
            return this.columnNames.length;
        }
        
        @Override
        public int getRowCount() {
            return this.data.length;
        }
        
        @Override
        public String getColumnName(final int col) {
            return this.columnNames[col];
        }
        
        @Override
        public Object getValueAt(final int row, final int col) {
            return this.data[row][col];
        }
        
        @Override
        public Class getColumnClass(final int c) {
            return this.getValueAt(0, c).getClass();
        }
        
        @Override
        public boolean isCellEditable(final int row, final int col) {
            return !(boolean)PanelTwoTable.globalData[row][1] || col == 1;
        }
        
        @Override
        public void setValueAt(final Object value, final int row, final int col) {
            PanelTwoTable.access$3(false);
            for (int checkName = 0; checkName < PanelTwoTable.table.getRowCount(); ++checkName) {
                if (PanelTwoTable.globalData[checkName][0].toString().equalsIgnoreCase(value.toString()) && checkName != row) {
                    PanelTwoTable.access$3(true);
                    break;
                }
            }
            if (PanelTwoTable.nameAlreadyExistInTable) {
                JOptionPane.showMessageDialog(PanelTwoTable.panelOne, "Selected Resourse Name Already exist in table Please rename the resource name");
                return;
            }
            this.data[row][col] = value;
            this.fireTableCellUpdated(row, col);
        }
    }
}
