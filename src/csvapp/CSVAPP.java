/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvapp;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Mert Yacan
 */
public class CSVAPP implements ActionListener {

    BufferedImage img;
    JFrame window;
    JLabel bg, llabel, rlabel, blabel;
    JPanel screen;
    JButton pick, convert;
    JTextArea l, r, b;
    JScrollPane lPane, rPane, bPane;
    File file;
    boolean isReady = false;

    public void start() {
        try {
            img = ImageIO.read(CSVAPP.class.getResourceAsStream("/images/2.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(CSVAPP.class.getName()).log(Level.SEVERE, null, ex);
        }
        ImageIcon bgimg = new ImageIcon(img);
        bg = new JLabel(bgimg);
        bg.setBounds(0, 0, bgimg.getIconWidth(), bgimg.getIconHeight());

        window = new JFrame("CSV Converter");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen = new JPanel();
        screen.setLayout(null);
        screen.setSize(585, 328);

        l = new JTextArea("Current CSV Format");
        l.setSize(252, 118);
        l.setEditable(false);
        l.setForeground(Color.white);
        l.setOpaque(false);
        l.setLineWrap(true);
        l.setWrapStyleWord(true);
        lPane = new JScrollPane(l);
        lPane.setBounds(22, 89, 248, 118);
        lPane.setOpaque(false);
        lPane.getViewport().setOpaque(false);
        lPane.setBorder(BorderFactory.createEmptyBorder());

        r = new JTextArea("Converted CSV Format");
        r.setSize(252, 118);
        r.setEditable(false);
        r.setForeground(Color.white);
        r.setOpaque(false);
        r.setLineWrap(true);
        r.setWrapStyleWord(true);
        rPane = new JScrollPane(r);
        rPane.setBounds(298, 89, 246, 118);
        rPane.setOpaque(false);
        rPane.getViewport().setOpaque(false);
        rPane.setBorder(BorderFactory.createEmptyBorder());

        b = new JTextArea("Converter is ready.\nPlease import a csv file.");
        b.setForeground(Color.white);
        b.setEditable(false);
        b.setForeground(Color.white);
        b.setOpaque(false);
        b.setLineWrap(true);
        b.setWrapStyleWord(true);
        bPane = new JScrollPane(b);
        bPane.setBounds(162, 229, 242, 35);
        bPane.setOpaque(false);
        bPane.getViewport().setOpaque(false);
        bPane.setBorder(BorderFactory.createEmptyBorder());

        pick = new JButton("Import CSV File");
        pick.setBounds(17, 226, 128, 41);
        pick.setBorderPainted(false);
        pick.setFocusable(false);

        convert = new JButton("Convert");
        convert.setBounds(421, 226, 128, 41);
        convert.setBorderPainted(false);
        convert.setFocusable(false);

        pick.addActionListener(this);
        convert.addActionListener(this);

        screen.add(bPane);
        screen.add(rPane);
        screen.add(lPane);
        screen.add(pick);
        screen.add(convert);
        screen.add(bg);

        screen.setVisible(true);

        window.add(screen);

        window.setSize(585, 328);
        window.setVisible(true);
    }

    public static void main(String[] args) {
        CSVAPP APP = new CSVAPP();
        APP.start();
    }

    public void importCSV() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            file = jfc.getSelectedFile();

            b.setText("Please wait...");
            StringBuilder imported = new StringBuilder();
            try {
                Scanner inputStream = new Scanner(file).useDelimiter(Pattern.compile("\\n"));
                while (inputStream.hasNext()) {
                    String data = inputStream.next() + "\n";
                    imported.append(data);
                }
                inputStream.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CSVAPP.class.getName()).log(Level.SEVERE, null, ex);
            }

            l.setText(imported.toString());
            b.setText("File is imported\nYou can convert it now.");
            isReady = true;
        } else {
            b.setText("File selection unsuccessful.\nPlease try again.");
        }
    }
    
    public void convertCSV(){
        CSVModel model = new CSVModel(file);
        try {
            r.setText(model.convert());
            //r.setText(model.convert());
            b.setText("Successful! File is saved to the location:\n" + file.getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(CSVAPP.class.getName()).log(Level.SEVERE, null, ex);
            b.setText("Unexpected error. Please try again.");
        }
        
    }

    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();
        if (ae.getSource() == pick) {
            importCSV();
        } else if (ae.getSource() == convert) {
            if(isReady){
                b.setText("Please wait...");
                b.repaint();
                window.repaint();
                convertCSV();
            }else{
                b.setText("You didn't import a CSV file yet.\nFirst, please import a CSV file.");
            }
        }
    }
}
