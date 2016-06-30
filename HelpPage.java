package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * Created by ozgur on 22.06.2016.
 */
public class HelpPage extends JFrame {
    JTextArea _resultArea = new JTextArea(6, 20);
    public HelpPage() {
        //... Set textarea's initial text, scrolling, and border.
        _resultArea.setText("Türkiye");
        JScrollPane scrollingArea = new JScrollPane(_resultArea);

        //... Get the content pane, set layout, add to center
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(scrollingArea, BorderLayout.CENTER);

        //... Set window characteristics.
        this.setContentPane(content);
        this.setTitle("TextAreaDemo B");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    }
}
