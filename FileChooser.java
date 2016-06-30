package com.company;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.io.File;
import java.awt.event.*;
import java.beans.*;

public class FileChooser extends JPanel implements ActionListener {
    static JFrame frame;

    static String metal= "Metal";
    static String metalClassName = "javax.swing.plaf.metal.MetalLookAndFeel";

    static String motif = "Motif";
    static String motifClassName = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";


    JButton button;
    JCheckBox useFileViewButton, accessoryButton, hiddenButton, showFullDescriptionButton;
    JRadioButton noFilterButton, addFiltersButton;
    JRadioButton openButton, saveButton;
    JRadioButton metalButton, motifButton;
    JRadioButton justFilesButton, justDirectoriesButton, bothFilesAndDirectoriesButton;

    JTextField customField;

    MyFileFilter xlsFilter, xlsxFilter, bothFilter;
    FileView fileView;

    JPanel buttonPanel;

    public final static Dimension hpad10 = new Dimension(10,1);
    public final static Dimension vpad10 = new Dimension(1,10);

    FilePreviewer previewer;
    JFileChooser chooser;

    public FileChooser() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        chooser = new JFileChooser();
        previewer = new FilePreviewer(chooser);
        chooser.setAccessory(previewer);

        xlsFilter = new MyFileFilter("xls", "MS Excel 2007-");
        xlsxFilter = new MyFileFilter("xlsx", "MS Excel 2007+");
        bothFilter = new MyFileFilter(new String[] {"xls", "xlsx"}, "Microsoft Excel Belgesi");

        fileView = new MyFileView();

        chooser.setAccessory(previewer);
        chooser.setFileView(fileView);

        // create a radio listener to listen to option changes
        OptionListener optionListener = new OptionListener();

        // Create options
        openButton = new JRadioButton("Aç");
        openButton.setSelected(true);
        openButton.addActionListener(optionListener);

        saveButton = new JRadioButton("Kaydet");
        saveButton.addActionListener(optionListener);

        customField = new JTextField("Doit");
        customField.setAlignmentY(JComponent.TOP_ALIGNMENT);
        customField.setEnabled(false);
        customField.addActionListener(optionListener);

        ButtonGroup group1 = new ButtonGroup();
        group1.add(openButton);
        group1.add(saveButton);

        // filter buttons
        noFilterButton = new JRadioButton("Filtreleme Yapma");
        noFilterButton.setSelected(true);
        noFilterButton.addActionListener(optionListener);

        addFiltersButton = new JRadioButton("XLS ve XLSX filtresi ekle");
        addFiltersButton.addActionListener(optionListener);

        ButtonGroup group2 = new ButtonGroup();
        group2.add(noFilterButton);
        group2.add(addFiltersButton);

        accessoryButton = new JCheckBox("Önizlemeyi göster");
        accessoryButton.addActionListener(optionListener);
        accessoryButton.setSelected(true);

        // more options
        hiddenButton = new JCheckBox("Gizli dosyaları göster");
        hiddenButton.addActionListener(optionListener);

        showFullDescriptionButton = new JCheckBox("Uzantıları göster");
        showFullDescriptionButton.addActionListener(optionListener);
        showFullDescriptionButton.setSelected(true);

        useFileViewButton = new JCheckBox("FileView kullan");
        useFileViewButton.addActionListener(optionListener);
        useFileViewButton.setSelected(true);

        // File or Directory chooser options
        ButtonGroup group3 = new ButtonGroup();
        justFilesButton = new JRadioButton("Sadece DOSYA seç");
        justFilesButton.setSelected(true);
        group3.add(justFilesButton);
        justFilesButton.addActionListener(optionListener);

        justDirectoriesButton = new JRadioButton("Sadece KLASÖR seç");
        group3.add(justDirectoriesButton);
        justDirectoriesButton.addActionListener(optionListener);

        bothFilesAndDirectoriesButton = new JRadioButton("Dosya ya da klasör seç");
        group3.add(bothFilesAndDirectoriesButton);
        bothFilesAndDirectoriesButton.addActionListener(optionListener);

        // Create show button
        button = new JButton("FileChooser'ı göster");
        button.addActionListener(this);
        button.setMnemonic('s');

        // Create laf buttons.
        metalButton = new JRadioButton(metal);
        metalButton.setMnemonic('o');
        metalButton.setActionCommand(metalClassName);

        motifButton = new JRadioButton(motif);
        motifButton.setMnemonic('m');
        motifButton.setActionCommand(motifClassName);


        ButtonGroup group4 = new ButtonGroup();
        group4.add(metalButton);
        group4.add(motifButton);

        // Register a listener for the laf buttons.
        metalButton.addActionListener(optionListener);
        motifButton.addActionListener(optionListener);

        JPanel control1 = new JPanel();
        control1.setLayout(new BoxLayout(control1, BoxLayout.X_AXIS));
        control1.add(Box.createRigidArea(hpad10));
        control1.add(openButton);
        control1.add(Box.createRigidArea(hpad10));
        control1.add(saveButton);
        control1.add(Box.createRigidArea(hpad10));
        control1.add(customField);
        control1.add(Box.createRigidArea(hpad10));

        JPanel control2 = new JPanel();
        control2.setLayout(new BoxLayout(control2, BoxLayout.X_AXIS));
        control2.add(Box.createRigidArea(hpad10));
        control2.add(noFilterButton);
        control2.add(Box.createRigidArea(hpad10));
        control2.add(addFiltersButton);
        control2.add(Box.createRigidArea(hpad10));
        control2.add(accessoryButton);
        control2.add(Box.createRigidArea(hpad10));

        JPanel control3 = new JPanel();
        control3.setLayout(new BoxLayout(control3, BoxLayout.X_AXIS));
        control3.add(Box.createRigidArea(hpad10));
        control3.add(hiddenButton);
        control3.add(Box.createRigidArea(hpad10));
        control3.add(showFullDescriptionButton);
        control3.add(Box.createRigidArea(hpad10));
        control3.add(useFileViewButton);
        control3.add(Box.createRigidArea(hpad10));

        JPanel control4 = new JPanel();
        control4.setLayout(new BoxLayout(control4, BoxLayout.X_AXIS));
        control4.add(Box.createRigidArea(hpad10));
        control4.add(justFilesButton);
        control4.add(Box.createRigidArea(hpad10));
        control4.add(justDirectoriesButton);
        control4.add(Box.createRigidArea(hpad10));
        control4.add(bothFilesAndDirectoriesButton);
        control4.add(Box.createRigidArea(hpad10));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createRigidArea(hpad10));
        panel.add(button);
        panel.add(Box.createRigidArea(hpad10));
        panel.add(metalButton);
        panel.add(Box.createRigidArea(hpad10));
        panel.add(motifButton);
        panel.add(Box.createRigidArea(hpad10));

        add(Box.createRigidArea(vpad10));
        add(control1);
        add(Box.createRigidArea(vpad10));
        add(control2);
        add(Box.createRigidArea(vpad10));
        add(control3);
        add(Box.createRigidArea(vpad10));
        add(control4);
        add(Box.createRigidArea(vpad10));
        add(Box.createRigidArea(vpad10));
        add(panel);
        add(Box.createRigidArea(vpad10));
    }

    public void actionPerformed(ActionEvent e) {
        int retval = chooser.showDialog(frame, null);
        if(retval == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();
            if(theFile != null) {
                if(theFile.isDirectory()) {
                    JOptionPane.showMessageDialog(
                            frame, "Bu klasörü seçtiniz: " +
                                    chooser.getSelectedFile().getAbsolutePath()
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            frame, "Bu dosyayı seçtiniz: " +
                                    chooser.getSelectedFile().getAbsolutePath()
                    );
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Hiçbir dosya seçilmedi, lütfen bir daha deneyin.");
    }

    /** An ActionListener that listens to the radio buttons. */
    class OptionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JComponent c = (JComponent) e.getSource();
            if(c == openButton) {
                chooser.setDialogType(JFileChooser.OPEN_DIALOG);
                customField.setEnabled(false);
                repaint();
            } else if (c == saveButton) {
                chooser.setDialogType(JFileChooser.SAVE_DIALOG);
                customField.setEnabled(false);
                repaint();
            } else if(c == noFilterButton) {
                chooser.resetChoosableFileFilters();
            } else if(c == addFiltersButton) {
                chooser.addChoosableFileFilter(bothFilter);
                chooser.addChoosableFileFilter(xlsFilter);
                chooser.addChoosableFileFilter(xlsxFilter);
            } else if(c == hiddenButton) {
                chooser.setFileHidingEnabled(!hiddenButton.isSelected());
            } else if(c == accessoryButton) {
                if(accessoryButton.isSelected()) {
                    chooser.setAccessory(previewer);
                } else {
                    chooser.setAccessory(null);
                }
            } else if(c == useFileViewButton) {
                if(useFileViewButton.isSelected()) {
                    chooser.setFileView(fileView);
                } else {
                    chooser.setFileView(null);
                }
            } else if(c == showFullDescriptionButton) {
                xlsFilter.setExtensionListInDescription(showFullDescriptionButton.isSelected());
                xlsxFilter.setExtensionListInDescription(showFullDescriptionButton.isSelected());
                bothFilter.setExtensionListInDescription(showFullDescriptionButton.isSelected());
                if(addFiltersButton.isSelected()) {
                    chooser.resetChoosableFileFilters();
                    chooser.addChoosableFileFilter(bothFilter);
                    chooser.addChoosableFileFilter(xlsFilter);
                    chooser.setFileFilter(xlsxFilter);
                }
            } else if(c == justFilesButton) {
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            } else if(c == justDirectoriesButton) {
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            } else if(c == bothFilesAndDirectoriesButton) {
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            } else {
                String lnfName = e.getActionCommand();

                try {
                    UIManager.setLookAndFeel(lnfName);
                    SwingUtilities.updateComponentTreeUI(frame);
                    if(chooser != null) {
                        SwingUtilities.updateComponentTreeUI(chooser);
                    }
                    frame.pack();
                } catch (UnsupportedLookAndFeelException exc) {
                    System.out.println("Desteklenmeyen görünüm:" + exc);
                    JRadioButton button = (JRadioButton)e.getSource();
                    button.setEnabled(false);
                    updateState();
                } catch (IllegalAccessException exc) {
                    System.out.println("Yanlış erişim hatası:" + exc);
                } catch (ClassNotFoundException exc) {
                    System.out.println("Sınıf bulunamadı hatası:" + exc);
                } catch (InstantiationException exc) {
                    System.out.println("Anlık hata:" + exc);
                }
            }

        }
    }

    public void updateState() {
        String lnfName = UIManager.getLookAndFeel().getClass().getName();
        if (lnfName.indexOf(metal) >= 0) {
            metalButton.setSelected(true);
        } else if (lnfName.indexOf(motif) >= 0) {
            motifButton.setSelected(true);
        } else {
            System.err.println(lnfName);
        }
    }

    class FilePreviewer extends JComponent implements PropertyChangeListener {
        ImageIcon thumbnail = null;
        File f = null;

        public FilePreviewer(JFileChooser fc) {
            setPreferredSize(new Dimension(100, 50));
            fc.addPropertyChangeListener(this);
        }

        public void loadImage() {
            if(f != null) {
                ImageIcon tmpIcon = new ImageIcon(f.getPath());
                if(tmpIcon.getIconWidth() > 90) {
                    thumbnail = new ImageIcon(
                            tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
                } else {
                    thumbnail = tmpIcon;
                }
            }
        }

        public void propertyChange(PropertyChangeEvent e) {
            String prop = e.getPropertyName();
            if(prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
                f = (File) e.getNewValue();
                if(isShowing()) {
                    loadImage();
                    repaint();
                }
            }
        }

        public void paint(Graphics g) {
            if(thumbnail == null) {
                loadImage();
            }
            if(thumbnail != null) {
                int x = getWidth()/2 - thumbnail.getIconWidth()/2;
                int y = getHeight()/2 - thumbnail.getIconHeight()/2;
                if(y < 0) {
                    y = 0;
                }

                if(x < 5) {
                    x = 5;
                }
                thumbnail.paintIcon(this, g, x, y);
            }
        }
    }

    public static void main(String s[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception exc) {
            System.err.println("Yükleme başarısız oldu: " + exc);
        }

        FileChooser panel = new FileChooser();

        frame = new JFrame("Haftalık Çalışma Raporu Robotu, Orman Bakanlığı, 2016");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        frame.getContentPane().add("Center", panel);
        frame.pack();
        frame.setVisible(true);

        panel.updateState();
    }
}