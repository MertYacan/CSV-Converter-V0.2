/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Mert Yacan
 */
public class CSVModel {
    private File file;
    private String newFileLoc;
    

    public CSVModel(File file){
        this.file = file;
        newFileLoc = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 4) + "new.csv";
    }
    
    public String convert() throws IOException{
        ArrayList<String> list = new ArrayList<String>();
        
        try {
            Scanner inputStream = new Scanner(file).useDelimiter(";\\s*");
            //inputStream.next(); //csvnin ilk satýrýný while loopuna girmeden önce nextleyerek iþleme sokmuyoruz. genelde baþlýklar bulunuyor ilk line'da.
            while (inputStream.hasNext()) {
                String data = inputStream.next();
                list.add(data);
                System.out.println(data);
            }
            inputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        File textFile = new File(newFileLoc);
        if (!textFile.exists()) {
            file.createNewFile();
        }
        
        //Lets write to our new file
        BufferedWriter out = new BufferedWriter(new FileWriter(textFile));
        StringBuilder imported = new StringBuilder();
        try {
            for(int i = 0; i<list.size(); i++){
                //if(i%howmanysutun==0){
                //    out.newLine();
                //}
                String tempStrwithproblem;
                tempStrwithproblem = list.get(i);
                String tempStr = tempStrwithproblem.replace(',','.');
                if(tempStr.equals("") || !tempStr.contains(" ")){
                //if(tempStr.equals("") || tempStr.matches("\\S+")){
                    out.append(tempStr + ",");
                    imported.append(tempStr + ",");
                }else{
                    if("\"".equals(tempStr.charAt(0))){
                        out.append(tempStr + ",");
                        imported.append(tempStr + ",");
                    }else{
                        tempStr = "\"" + tempStr + "\"";
                        out.append(tempStr + ",");
                        imported.append(tempStr + ",");
                    }
                }
            }
        } finally {
            out.close();
        }
        return imported.toString();
    }
    
    public static void main(String[] args){
    }

}
