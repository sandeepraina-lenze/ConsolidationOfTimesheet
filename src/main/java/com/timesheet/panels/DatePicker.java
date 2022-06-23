// 
// Decompiled by Procyon v0.6.0
// 

package com.timesheet.panels;

import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class DatePicker
{
    int month;
    int year;
    JLabel l;
    String day;
    JDialog d;
    JButton[] button;
    
    public DatePicker(final JFrame parent) {
        this.month = Calendar.getInstance().get(2);
        this.year = Calendar.getInstance().get(1);
        this.l = new JLabel("", 0);
        this.day = "";
        this.button = new JButton[49];
        (this.d = new JDialog()).setModal(true);
        final String[] header = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
        final JPanel p1 = new JPanel(new GridLayout(7, 7));
        p1.setPreferredSize(new Dimension(430, 120));
        for (int x = 0; x < this.button.length; ++x) {
            final int selection = x;
            (this.button[x] = new JButton()).setFocusPainted(false);
            this.button[x].setBackground(Color.white);
            if (x > 6) {
                this.button[x].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent ae) {
                        DatePicker.this.day = DatePicker.this.button[selection].getActionCommand();
                        DatePicker.this.d.dispose();
                    }
                });
            }
            if (x < 7) {
                this.button[x].setText(header[x]);
                this.button[x].setForeground(Color.red);
            }
            p1.add(this.button[x]);
        }
        final JPanel p2 = new JPanel(new GridLayout(1, 3));
        final JButton previous = new JButton("<< Previous");
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ae) {
                final DatePicker this$0 = DatePicker.this;
                --this$0.month;
                DatePicker.this.displayDate();
            }
        });
        p2.add(previous);
        p2.add(this.l);
        final JButton next = new JButton("Next >>");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ae) {
                final DatePicker this$0 = DatePicker.this;
                ++this$0.month;
                DatePicker.this.displayDate();
            }
        });
        p2.add(next);
        this.d.add(p1, "Center");
        this.d.add(p2, "South");
        this.d.pack();
        this.d.setLocationRelativeTo(parent);
        this.displayDate();
        this.d.setVisible(true);
    }
    
    public void displayDate() {
        for (int x = 7; x < this.button.length; ++x) {
            this.button[x].setText("");
        }
        final SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        final Calendar cal = Calendar.getInstance();
        cal.set(this.year, this.month, 1);
        final int dayOfWeek = cal.get(7);
        final int daysInMonth = cal.getActualMaximum(5);
        int x2 = 6 + dayOfWeek;
        for (int day = 1; day <= daysInMonth; ++day) {
            this.button[x2].setText(new StringBuilder().append(day).toString());
            ++x2;
        }
        this.l.setText(sdf.format(cal.getTime()));
        this.d.setTitle("Date Picker");
    }
    
    public String setPickedDate() {
        if (this.day.equals("")) {
            return this.day;
        }
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        final Calendar cal = Calendar.getInstance();
        cal.set(this.year, this.month, Integer.parseInt(this.day));
        return sdf.format(cal.getTime());
    }
}
