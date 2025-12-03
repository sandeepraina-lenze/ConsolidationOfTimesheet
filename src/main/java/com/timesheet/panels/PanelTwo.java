// 
// Decompiled by Procyon v0.6.0
// 

package com.timesheet.panels;

import java.awt.Dimension;
import java.io.File;
import javax.swing.JFileChooser;
import com.odc.readexcel.TimeSheetValidation;
import com.odc.timesheet.TimesheetContainer;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class PanelTwo extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private static final Insets insets;
    private static final String LOAD = "Load Timesheets";
    private static JButton loadButton;
    private PathSelectionPanel timesheetPathSelection;
    private JButton browseButton;
    private static PanelTwoTable panelTwoTable;
    private boolean tableCreated;
    private Map<String, Double> innerFileMap;
    private PanelOne panelOne;
    
    static {
        insets = new Insets(0, 0, 0, 0);
    }
    
    public static JButton getLoadButton() {
        return PanelTwo.loadButton;
    }
    
    public PanelTwo() {
        this.createLayout();
    }
    
    protected void createLayout() {
        this.add(this.timesheetPathSelection = new PathSelectionPanel("Load Timesheets"));
        (this.browseButton = this.timesheetPathSelection.getBrowseButton()).addActionListener(this);
    }
    
    @Override
    public void actionPerformed(final ActionEvent ae) {
        if (ae.getSource() == PanelTwo.loadButton) {
            this.panelOne = new PanelOne();
            PanelOne.OutputFileMap = PanelOne.getOutputFileMap();
            TimesheetContainer.getGenerateOutputTemplate().setEnabled(true);
            TimesheetContainer.loadResourceTable();
        }
        if (ae.getSource() == this.browseButton) {
            TimeSheetValidation.setAlreadyCalled(true);
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setName("Browse");
            fileChooser.setCurrentDirectory(new File(this.timesheetPathSelection.getPath()));
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.addChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
            if (fileChooser.showOpenDialog(this) == 0) {
                final String path = fileChooser.getSelectedFile().getParent();
                this.timesheetPathSelection.setPath(path);
            }
            if (this.tableCreated) {
                this.remove(PanelTwo.panelTwoTable);
                this.remove(PanelTwo.loadButton);
            }
            PanelTwo.panelTwoTable = new PanelTwoTable(this.getTimesheetPathSelection().getPath());
            final int width = PanelTwoTable.getTable().getPreferredSize().width;
            final int height = PanelTwoTable.getTable().getRowCount() * PanelTwoTable.getTable().getRowHeight();
            PanelTwoTable.getTable().setPreferredScrollableViewportSize(new Dimension(830, 100));
            this.add(PanelTwo.panelTwoTable);
            this.add(PanelTwo.loadButton = new JButton(LOAD));
            PanelTwo.loadButton.addActionListener(this);
            if (!this.tableCreated) {
                this.tableCreated = true;
            }
            this.revalidate();
            this.repaint();
            PanelOne.OutputFileMap = TimeSheetValidation.GetDynamicOutputData(PanelOne.getOutputpath());
        }
    }
    
    public void removeTable() {
        this.remove(PanelTwo.panelTwoTable);
        this.remove(PanelTwo.loadButton);
    }
    
    public static PanelTwoTable getPanelTwoTable() {
        return PanelTwo.panelTwoTable;
    }
    
    public PathSelectionPanel getTimesheetPathSelection() {
        return this.timesheetPathSelection;
    }
    
    public void setTimesheetPathSelection(final PathSelectionPanel timesheetPathSelection) {
        this.timesheetPathSelection = timesheetPathSelection;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        this.timesheetPathSelection.setEnabled(enabled);
        this.browseButton.setEnabled(enabled);
    }
}
