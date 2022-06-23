// 
// Decompiled by Procyon v0.6.0
// 

package com.timesheet.panels;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DatePanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private static final String START_DATE_LABEL = "Start Date:";
    private static final String END_DATE_LABEL = "                                                                       End Date:";
    private static final String popUp = "popup";
    private JLabel startDateLabel;
    private JTextField startDateTextField;
    private JButton startDateButton;
    private JLabel endDateLabel;
    private JTextField endDateTextField;
    private JButton endDateButton;
    
    public DatePanel() {
        this.createLayout();
    }
    
    public JTextField getStartDateTextField() {
        return this.startDateTextField;
    }
    
    public void setStartDateTextField(final JTextField startDateTextField) {
        this.startDateTextField = startDateTextField;
    }
    
    public JTextField getEndDateTextField() {
        return this.endDateTextField;
    }
    
    public void setEndDateTextField(final JTextField endDateTextField) {
        this.endDateTextField = endDateTextField;
    }
    
    protected void createLayout() {
        this.startDateLabel = new JLabel("Start Date:");
        (this.startDateTextField = new JTextField(6)).setEditable(false);
        this.startDateButton = new JButton("popup");
        this.add(this.startDateLabel);
        this.add(this.startDateTextField);
        this.add(this.startDateButton);
        this.endDateLabel = new JLabel("                                                                       End Date:");
        (this.endDateTextField = new JTextField(6)).setEditable(false);
        this.endDateButton = new JButton("popup");
        this.add(this.endDateLabel);
        this.add(this.endDateTextField);
        this.add(this.endDateButton);
    }
}
