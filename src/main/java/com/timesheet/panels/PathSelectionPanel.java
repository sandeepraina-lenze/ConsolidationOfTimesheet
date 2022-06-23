// 
// Decompiled by Procyon v0.6.0
// 

package com.timesheet.panels;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;

public class PathSelectionPanel extends JPanel
{
    private static final long serialVersionUID = 3618700794577105718L;
    private JTextField textField;
    private JButton browseButton;
    private static final int TEXT_FIELD_SIZE = 50;
    
    public PathSelectionPanel(final String message) {
        this.createLayout(message);
    }
    
    protected void createLayout(final String message) {
        final JLabel sowLabel = new JLabel();
        sowLabel.setText(message);
        this.add(sowLabel);
        (this.textField = new JTextField(50)).setEditable(false);
        this.add(this.textField);
        (this.browseButton = new JButton()).setText("Browse");
        this.add(this.browseButton);
    }
    
    public String getPath() {
        return this.textField.getText();
    }
    
    public void setPath(final String path) {
        this.textField.setText(path);
    }
    
    public JTextField getPathInputField() {
        return this.textField;
    }
    
    public JButton getBrowseButton() {
        return this.browseButton;
    }
}
