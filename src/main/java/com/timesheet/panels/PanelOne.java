// 
// Decompiled by Procyon v0.6.0
// 

package com.timesheet.panels;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import com.odc.timesheet.TimesheetContainer;
import com.odc.readexcel.TimeSheetValidation;
import javax.swing.JOptionPane;
import com.timesheet.validation.ValidateEmptyExcel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class PanelOne extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private static final String SOW_LOAD = " Load Running MPPs list in  excel:                 ";
    private static final String OUTPUT_TEMPLATE = "Load Sample Output Template:              ";
    private PathSelectionPanel sowPathSelection;
    private PathSelectionPanel outputTemplatePathSelection;
    private JButton sowBrowseButton;
    private JButton outputBrowseButton;
    private static String sowpath;
    private static String outputpath;
    static Map<String, Map<String, List<Double>>> OutputFileMap;
    
    public PanelOne() {
        this.createLayout();
    }
    
    public static Map<String, Map<String, List<Double>>> getOutputFileMap() {
        return PanelOne.OutputFileMap;
    }
    
    public static void setOutputFileMap(final Map<String, Map<String, List<Double>>> outputFileMap) {
        PanelOne.OutputFileMap = outputFileMap;
    }
    
    protected void createLayout() {
        this.add(this.sowPathSelection = new PathSelectionPanel(SOW_LOAD));
        (this.sowBrowseButton = this.sowPathSelection.getBrowseButton()).addActionListener(this);
        this.outputTemplatePathSelection = new PathSelectionPanel(OUTPUT_TEMPLATE);
        (this.outputBrowseButton = this.outputTemplatePathSelection.getBrowseButton()).addActionListener(this);
        this.add(this.outputTemplatePathSelection);
    }
    
    public PathSelectionPanel getSowPathSelection() {
        return this.sowPathSelection;
    }
    
    public void setSowPathSelection(final PathSelectionPanel sowPathSelection) {
        this.sowPathSelection = sowPathSelection;
    }
    
    public PathSelectionPanel getOutputTemplatePathSelection() {
        return this.outputTemplatePathSelection;
    }
    
    public void setOutputTemplatePathSelection(final PathSelectionPanel outputTemplatePathSelection) {
        this.outputTemplatePathSelection = outputTemplatePathSelection;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (source == this.sowBrowseButton) {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setName("Browse");
            fileChooser.setCurrentDirectory(new File(this.sowPathSelection.getPath()));
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileSelectionMode(0);
            final FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL FILES", new String[] { "xlsx", "excel" });
            fileChooser.setFileFilter(filter);
            fileChooser.getSelectedFile();
            if (fileChooser.showSaveDialog(this) == 0) {
                final String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (ValidateEmptyExcel.validateExcel(path)) {
                    JOptionPane.showMessageDialog(this, "You have selected an invalid file.Please select a valid file.");
                    this.sowPathSelection.setPath("");
                    return;
                }
                this.sowPathSelection.setPath(path);
                PanelOne.sowpath = path;
            }
        }
        if (source == this.outputBrowseButton) {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setName("Browse");
            fileChooser.setCurrentDirectory(new File(this.outputTemplatePathSelection.getPath()));
            fileChooser.setMultiSelectionEnabled(false);
            final FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL FILES", new String[] { "xlsx", "excel" });
            fileChooser.setFileFilter(filter);
            fileChooser.setFileSelectionMode(0);
            if (fileChooser.showSaveDialog(this) == 0) {
                final String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (ValidateEmptyExcel.validateExcel(path)) {
                    JOptionPane.showMessageDialog(this, "You have selected an invalid file.Please select a valid file.");
                    this.outputTemplatePathSelection.setPath("");
                    return;
                }
                this.outputTemplatePathSelection.setPath(path);
                PanelOne.outputpath = path;
                PanelOne.OutputFileMap = TimeSheetValidation.GetDynamicOutputData(path);
                if (PanelOne.OutputFileMap == null) {
                    JOptionPane.showMessageDialog(this, "You have selected an invalid file.Please select a valid file.");
                    this.outputTemplatePathSelection.setPath("");
                }
                else if (PanelOne.OutputFileMap.size() == 0) {
                    TimesheetContainer.loadResourceTable();
                }
                else {
                    TimesheetContainer.loadResourceTable();
                }
            }
        }
        if (this.enablePanelTwo()) {
            this.getParent().getComponent(3).setEnabled(true);
        }
    }
    
    public static String getSowPath() {
        return PanelOne.sowpath;
    }
    
    public static String getOutputpath() {
        return PanelOne.outputpath;
    }
    
    public static void setSowPath(final String sowpath) {
        PanelOne.sowpath = sowpath;
    }
    
    public boolean isEmptyRow(final Row row) {
        boolean isEmptyRow = true;
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); ++cellNum) {
            final Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                isEmptyRow = false;
            }
        }
        return isEmptyRow;
    }
    
    public boolean enablePanelTwo() {
        final PathSelectionPanel sowTemplate = (PathSelectionPanel)this.getComponent(0);
        final PathSelectionPanel outputTemplate = (PathSelectionPanel)this.getComponent(1);
        final DatePanel datePanel = (DatePanel)this.getComponent(2);
        return !sowTemplate.getPath().equalsIgnoreCase("") && !outputTemplate.getPath().equalsIgnoreCase("") && !datePanel.getStartDateTextField().getText().equalsIgnoreCase("") && !datePanel.getEndDateTextField().getText().equalsIgnoreCase("");
    }
}
