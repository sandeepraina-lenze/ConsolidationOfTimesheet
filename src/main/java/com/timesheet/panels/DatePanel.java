// 
// Decompiled by Procyon v0.6.0
// 

package com.timesheet.panels;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DatePanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private static final String START_DATE_LABEL = "Start Date:";
    private static final String END_DATE_LABEL = "End Date:";
    private static final String NUMBER_OF_DAYS = "Number of Days per Month:";
    private static final String HOURS_PER_DAY = "Hours per Day:";
    private static final String TOTAL_WORKING_HOURS = "Total Working Hours per Month:";
    private static final String popUp = "popup";
    private JLabel startDateLabel;
    private JTextField startDateTextField;
    private JButton startDateButton;
    private JLabel endDateLabel;
    private JTextField endDateTextField;
    private JButton endDateButton;

    private JLabel daysInMonthLabel;
    private JTextField daysInMonthTextField;

    private JLabel hrsPerDayLabel;
    private JTextField hrsPerDayTextField;

    private JLabel totalWorkingHoursLabel;
    private JTextField totalWorkingHoursTextField;

    private static double totalWorkingHours;
    
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
        this.startDateLabel = new JLabel(START_DATE_LABEL);
        (this.startDateTextField = new JTextField(6)).setEditable(false);
        this.startDateButton = new JButton("popup");

        this.add(this.startDateLabel);
        this.add(this.startDateTextField);
        this.add(this.startDateButton);

        this.endDateLabel = new JLabel(END_DATE_LABEL);
        (this.endDateTextField = new JTextField(6)).setEditable(false);
        this.endDateButton = new JButton("popup");

        this.add(this.endDateLabel);
        this.add(this.endDateTextField);
        this.add(this.endDateButton);

        this.daysInMonthLabel = new JLabel(NUMBER_OF_DAYS);
        (this.daysInMonthTextField = new JTextField(6)).setEditable(true);
        applyTwoDigitOnlyFilter(this.daysInMonthTextField);
        this.add(this.daysInMonthLabel);
        this.add(this.daysInMonthTextField);

        this.daysInMonthTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                // Update after key releases so the text model is up to date

                if (hrsPerDayTextField.getText().isEmpty())
                    hrsPerDayTextField.setText("1");

                if (daysInMonthTextField.getText().isEmpty())
                    daysInMonthTextField.setText("1");

                totalWorkingHoursTextField.setText(String.valueOf(Double.parseDouble(hrsPerDayTextField.getText()) * Integer.parseInt(daysInMonthTextField.getText())));
                totalWorkingHours = Double.parseDouble(totalWorkingHoursTextField.getText());
            }
        });

        this.hrsPerDayLabel = new JLabel(HOURS_PER_DAY);
        (this.hrsPerDayTextField = new JTextField(6)).setEditable(true);

        // Apply the custom DocumentFilter
        ((AbstractDocument) this.hrsPerDayTextField.getDocument()).setDocumentFilter(new DocumentFilter() {
            private final String pattern = "^\\d{1,2}(\\.\\d{0,2})?$"; // up to 2 decimal places, positive only

            private boolean isValid(String proposed) {
                // Allow empty (user still typing)
                if (proposed.isEmpty()) return true;
                // No leading '.' without a digit? Allow ".5" if you prefer — modify as needed.
                // Currently allows ".5" because \\d* permits empty before decimal.
                return proposed.matches(pattern);
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null) return;

                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.insert(offset, string);

                if (isValid(sb.toString())) {
                    super.insertString(fb, offset, string, attr);
                } // else ignore invalid insert
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(current);
                // delete the range, then insert new text (may be null or empty)
                sb.replace(offset, offset + length, text == null ? "" : text);

                if (isValid(sb.toString())) {
                    super.replace(fb, offset, length, text, attrs);
                } // else ignore invalid replace
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length)
                    throws BadLocationException {
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.delete(offset, offset + length);

                if (isValid(sb.toString())) {
                    super.remove(fb, offset, length);
                } // removing generally stays valid; kept for symmetry
            }
        });

        this.add(this.hrsPerDayLabel);
        this.add(this.hrsPerDayTextField);

        this.hrsPerDayTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                // Update after key releases so the text model is up to date

                if (hrsPerDayTextField.getText().isEmpty())
                    hrsPerDayTextField.setText("1");

                if (daysInMonthTextField.getText().isEmpty())
                    daysInMonthTextField.setText("1");

                totalWorkingHoursTextField.setText(String.valueOf(Double.parseDouble(hrsPerDayTextField.getText()) * Integer.parseInt(daysInMonthTextField.getText())));
                totalWorkingHours = Double.parseDouble(totalWorkingHoursTextField.getText());
            }
        });

        this.totalWorkingHoursLabel = new JLabel(TOTAL_WORKING_HOURS);
        (this.totalWorkingHoursTextField = new JTextField(6)).setEditable(false);

        if (hrsPerDayTextField.getText().isEmpty())
            hrsPerDayTextField.setText("1");

        if (daysInMonthTextField.getText().isEmpty())
            daysInMonthTextField.setText("1");

        totalWorkingHoursTextField.setText(String.valueOf(Double.parseDouble(hrsPerDayTextField.getText()) * Integer.parseInt(daysInMonthTextField.getText())));
        totalWorkingHours = Double.parseDouble(totalWorkingHoursTextField.getText());

        this.add(this.totalWorkingHoursLabel);
        this.add(this.totalWorkingHoursTextField);
    }

    /** Allows only digits and length up to 2; empty allowed while typing. */
    private static void applyTwoDigitOnlyFilter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            private boolean isValid(String proposed) {
                // Allow empty (user clearing or still typing)
                if (proposed.isEmpty()) return true;
                // Only digits, length 1 or 2
                return proposed.matches("\\d{1,2}");
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null) return;
                String cur = fb.getDocument().getText(0, fb.getDocument().getLength());
                String next = new StringBuilder(cur).insert(offset, string).toString();
                if (isValid(next)) super.insertString(fb, offset, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String cur = fb.getDocument().getText(0, fb.getDocument().getLength());
                String next = new StringBuilder(cur)
                        .replace(offset, offset + length, text == null ? "" : text).toString();
                if (isValid(next)) super.replace(fb, offset, length, text, attrs);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length)
                    throws BadLocationException {
                super.remove(fb, offset, length); // always safe to remove
            }
        });
    }

    public static double getTotalWorkingHours() {
        return totalWorkingHours;
    }
}
