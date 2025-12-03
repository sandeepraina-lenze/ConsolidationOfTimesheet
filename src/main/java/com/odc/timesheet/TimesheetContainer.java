// 
// Decompiled by Procyon v0.6.0
// 

package com.odc.timesheet;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import com.odc.readexcel.Timesheet;

import java.awt.*;

import com.timesheet.panels.PanelThree;
import javax.swing.JSeparator;
import javax.swing.JOptionPane;
import com.timesheet.panels.DatePicker;
import java.awt.event.ActionEvent;
import com.timesheet.panels.DatePanel;

import javax.swing.JLabel;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import com.timesheet.panels.PanelTwo;
import javax.swing.JPanel;
import com.timesheet.panels.PanelOne;
import org.apache.poi.ss.formula.functions.T;

import javax.swing.JButton;
import javax.swing.JTextField;
import java.util.Date;
import java.awt.event.ActionListener;
import java.util.TreeSet;
import javax.swing.JFrame;

public class TimesheetContainer extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private static final Insets insets;
    private static final String CONSOLIDATION_TIMESHEET = "Consolidation of timesheet";
    private static final String OUTPUT_TEMPLATE = "Generate SOW/Resource efforts";
    private static final String TIMESHEET_LABEL = "Consolidation of timesheet";
    private Date endDate;
    private Date startDate;
    private static JFrame mainFrame;
    private JTextField endDateTextField;
    private JButton startDateButton;
    private JTextField startDateTextField;
    private static String startDateField;
    private static String endDateField;
    private static JButton generateOutputTemplate;
    private PanelOne panelOne;
    private static JPanel panelThree;
    private static PanelTwo panelTwo;
    
    static {
        insets = new Insets(0, 0, 0, 0);
    }
    
    public static JButton getGenerateOutputTemplate() {
        return TimesheetContainer.generateOutputTemplate;
    }
    
    public static PanelTwo getPanelTwo() {
        return TimesheetContainer.panelTwo;
    }
    
    public static String getStartDateField() {
        return TimesheetContainer.startDateField;
    }
    
    public static String getEndDateField() {
        return TimesheetContainer.endDateField;
    }
    
    public TimesheetContainer() {
        this.prepareGUI();
    }
    
    public static void main(final String[] args) {
        final TimesheetContainer timesheetContainer = new TimesheetContainer();
        timesheetContainer.createPanel();
    }
    
    public JTextField getEndDateTextField() {
        return this.endDateTextField;
    }
    
    public JTextField getStartDateTextField() {
        return this.startDateTextField;
    }


    private void prepareGUI() {
        TimesheetContainer.mainFrame = new JFrame("Consolidation of timesheet");
        TimesheetContainer.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TimesheetContainer.mainFrame.setResizable(true);
//        TimesheetContainer.mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        TimesheetContainer.mainFrame.setLayout(new GridBagLayout());
        TimesheetContainer.mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent windowEvent) {
                System.exit(0);
            }
        });


        TimesheetContainer.mainFrame.setVisible(true);



    }


    private void createPanel() {
        final JLabel jlabel = new JLabel("Consolidation of timesheet");
        jlabel.setForeground(Color.RED);
        final Font labelFont = new Font("labelFont", 1, 20);
        jlabel.setFont(labelFont);
        addComponent(TimesheetContainer.mainFrame, jlabel, 0, 0, 1, 1, 1.0, 0.01, 11, 11);
        this.panelOne = new PanelOne();
        TimesheetContainer.panelTwo = new PanelTwo();
        final DatePanel datePanel = new DatePanel();
        this.startDateTextField = (JTextField)datePanel.getComponent(1);
        (this.startDateButton = (JButton)datePanel.getComponent(2)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ae) {
                TimesheetContainer.this.startDateTextField.setText(new DatePicker(TimesheetContainer.mainFrame).setPickedDate());
                TimesheetContainer.access$2(TimesheetContainer.this.startDateTextField.getText().toString());
                if (!TimesheetContainer.this.startDateTextField.getText().toString().equalsIgnoreCase("")) {
                    final int date = Integer.parseInt(TimesheetContainer.this.startDateTextField.getText().substring(0, TimesheetContainer.this.startDateTextField.getText().indexOf(45)));
                    final int month = Integer.parseInt(TimesheetContainer.this.startDateTextField.getText().substring(TimesheetContainer.this.startDateTextField.getText().indexOf(45) + 1, TimesheetContainer.this.startDateTextField.getText().lastIndexOf(45)));
                    final int year = Integer.parseInt(TimesheetContainer.this.startDateTextField.getText().substring(TimesheetContainer.this.startDateTextField.getText().lastIndexOf(45) + 1, TimesheetContainer.this.startDateTextField.getText().length()));
                    TimesheetContainer.access$3(TimesheetContainer.this, new Date(year, month, date));
                }
                if (TimesheetContainer.this.startDateTextField.getText() != null && !TimesheetContainer.this.startDateTextField.getText().equals("") && TimesheetContainer.this.endDateTextField.getText() != null && !TimesheetContainer.this.endDateTextField.getText().equals("") && TimesheetContainer.this.startDate.compareTo(TimesheetContainer.this.endDate) > 0) {
                    JOptionPane.showMessageDialog(TimesheetContainer.mainFrame, "Invalid Start date");
                    TimesheetContainer.this.startDateTextField.setText("");
                }
                if (TimesheetContainer.this.panelOne.enablePanelTwo()) {
                    TimesheetContainer.panelTwo.setEnabled(true);
                }
            }
        });
        this.endDateTextField = (JTextField)datePanel.getComponent(4);
        final JButton endDateButton = (JButton)datePanel.getComponent(5);
        endDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ae) {
                TimesheetContainer.this.endDateTextField.setText(new DatePicker(TimesheetContainer.mainFrame).setPickedDate());
                TimesheetContainer.access$9(TimesheetContainer.this.endDateTextField.getText().toString());
                if (!TimesheetContainer.this.endDateTextField.getText().toString().equalsIgnoreCase("")) {
                    final int date = Integer.parseInt(TimesheetContainer.this.endDateTextField.getText().substring(0, TimesheetContainer.this.endDateTextField.getText().indexOf(45)));
                    final int month = Integer.parseInt(TimesheetContainer.this.endDateTextField.getText().substring(TimesheetContainer.this.endDateTextField.getText().indexOf(45) + 1, TimesheetContainer.this.endDateTextField.getText().lastIndexOf(45)));
                    final int year = Integer.parseInt(TimesheetContainer.this.endDateTextField.getText().substring(TimesheetContainer.this.endDateTextField.getText().lastIndexOf(45) + 1, TimesheetContainer.this.endDateTextField.getText().length()));
                    TimesheetContainer.access$10(TimesheetContainer.this, new Date(year, month, date));
                }
                if (TimesheetContainer.this.startDateTextField.getText() != null && !TimesheetContainer.this.startDateTextField.getText().equals("") && TimesheetContainer.this.endDateTextField.getText() != null && !TimesheetContainer.this.endDateTextField.getText().equals("") && TimesheetContainer.this.startDate.compareTo(TimesheetContainer.this.endDate) > 0) {
                    JOptionPane.showMessageDialog(TimesheetContainer.mainFrame, "Invalid End date");
                    TimesheetContainer.this.endDateTextField.setText("");
                }
                if (TimesheetContainer.this.panelOne.enablePanelTwo()) {
                    TimesheetContainer.panelTwo.setEnabled(true);
                }
            }
        });
        this.panelOne.add(datePanel);
        addComponent(TimesheetContainer.mainFrame, this.panelOne, 0, 1, 1, 1, 1.0, 0.12, 11, 1);
        addComponent(TimesheetContainer.mainFrame, new JSeparator(0), 0, 2, 1, 1, 1.0, 1.0E-4, 11, 1);
        addComponent(TimesheetContainer.mainFrame, TimesheetContainer.panelTwo, 0, 3, 1, 1, 1.0, 0.3, 11, 1);
        TimesheetContainer.panelTwo.setEnabled(false);
        TimesheetContainer.panelThree = new PanelThree().createThirdPanel();
        addComponent(TimesheetContainer.mainFrame, TimesheetContainer.panelThree, 0, 4, 1, 1, 1.0, 0.2, 11, 1);
        addComponent(TimesheetContainer.mainFrame, new JSeparator(0), 0, 5, 1, 1, 1.0, 0.01, 11, 1);
        TimesheetContainer.generateOutputTemplate = new JButton(OUTPUT_TEMPLATE);
        addComponent(TimesheetContainer.mainFrame, TimesheetContainer.generateOutputTemplate, 0, 6, 1, 1, 0.1, 0.005, 14, 14);
        TimesheetContainer.generateOutputTemplate.addActionListener(this);
        TimesheetContainer.generateOutputTemplate.setEnabled(false);
        TimesheetContainer.mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        TimesheetContainer.mainFrame.pack();
    }
    
    private static void addComponent(final Container container, final Component component, final int gridx, final int gridy, final int gridwidth, final int gridheight, final double weightx, final double weighty, final int anchor, final int fill) {
        final GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, TimesheetContainer.insets, 0, 0);
        container.add(component, gbc);
    }
    
    @Override
    public void actionPerformed(final ActionEvent ae) {
        if (ae.getSource() == TimesheetContainer.generateOutputTemplate) {
            try {
                final Timesheet OutputSheet = new Timesheet(PanelOne.getOutputpath());

                OutputSheet.TemplateSet(PanelOne.getOutputFileMap());
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(this.panelOne, "Timesheet generated successfully");
        }
    }
    
    public static void loadResourceTable() {
        if (PanelOne.getOutputFileMap().size() == 0) {
            TimesheetContainer.panelThree.removeAll();
        }
        else if (TimesheetContainer.panelThree.getComponents().length == 0) {
            addOrUpdateResourceTable();
        }
        else {
            TimesheetContainer.panelThree.removeAll();
            addOrUpdateResourceTable();
        }
        TimesheetContainer.mainFrame.validate();
        TimesheetContainer.mainFrame.repaint();
    }
    
    private static void addOrUpdateResourceTable() {
        final JTable table = new PanelThree().createResourceTable();
        table.getTableHeader().setReorderingAllowed(false);
        final JScrollPane scrollPane = new PanelThree().createResource(table);
        TimesheetContainer.panelThree.add(scrollPane);
    }
    
    static /* synthetic */ void access$2(final String startDateField) {
        TimesheetContainer.startDateField = startDateField;
    }
    
    static /* synthetic */ void access$3(final TimesheetContainer timesheetContainer, final Date startDate) {
        timesheetContainer.startDate = startDate;
    }
    
    static /* synthetic */ void access$9(final String endDateField) {
        TimesheetContainer.endDateField = endDateField;
    }
    
    static /* synthetic */ void access$10(final TimesheetContainer timesheetContainer, final Date endDate) {
        timesheetContainer.endDate = endDate;
    }
}
