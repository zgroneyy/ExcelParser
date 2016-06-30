package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by ozgur on 23.06.2016.
 */
public class HelpUI extends JFrame implements ActionListener {
    static JFrame frame;

    static String metal= "Metal";
    static String metalClassName = "javax.swing.plaf.metal.MetalLookAndFeel";

    static String motif = "Motif";
    static String motifClassName = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";

    static String windows = "Windows";
    static String windowsClassName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

    JRadioButton metalButton, motifButton, windowsButton;

    public HelpUI(){
        metalButton = new JRadioButton(metal);
        metalButton.setMnemonic('o');
        metalButton.setActionCommand(metalClassName);

        motifButton = new JRadioButton(motif);
        motifButton.setMnemonic('m');
        motifButton.setActionCommand(motifClassName);

        windowsButton = new JRadioButton(windows);
        windowsButton.setMnemonic('w');
        windowsButton.setActionCommand(windowsClassName);

        ButtonGroup group4 = new ButtonGroup();
        group4.add(metalButton);
        group4.add(motifButton);
        group4.add(windowsButton);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public void updateState() {
        String lnfName = UIManager.getLookAndFeel().getClass().getName();
        if (lnfName.indexOf(metal) >= 0) {
            metalButton.setSelected(true);
        }
        else if (lnfName.indexOf(motif) >= 0) {
            motifButton.setSelected(true);
        } else {
            System.err.println("FileChooserExample if using an unknown  Look and Feel: " + lnfName);
        }
    }
    public void run(){
        try {
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception exc) {
            System.err.println("Error loading  Look and Feel: " + exc);
        }

        HelpUI panel = new HelpUI();

        frame = new JFrame("TC Orman ve Su İşleri Bakanlığı, Günlük Çalışma rapor robotu, 2016.");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        frame.getContentPane().add("Center", panel);
        frame.pack();
        frame.setVisible(true);

        panel.updateState();
    }
}
