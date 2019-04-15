/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhis2datim;

import gui.FrmMain;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;



public class MyWorker extends SwingWorker<Integer, String>{
    
    private JTextArea logScreen;
   
    private List<CSVLine>list;
    
    public MyWorker(final JTextArea _logScreen,List _list){
        this.logScreen=_logScreen;
        this.list=_list;
    }
    
    @Override
    protected Integer doInBackground() throws Exception {
        
        publish("Processing started");
        
        int count=0;
        
        int size=0;
        
        FileWriter writer = null;
        try {
            String filename=Constant.CSV_FILE_NAME;
            
            //writer = new FileWriter(Constant.CSV_FILE_NAME);
            writer = new FileWriter(filename);
            
        } catch (IOException ex) {
            Logger.getLogger(Dhis2datim.class.getName()).log(Level.SEVERE, null, ex);
        }

        

        try {
            CSVUtils.writeLine(writer,Arrays.asList("Data element","Period","OrganisationUnit","CategoryCombo","AttributeCombo","Value"));
        } catch (IOException ex) {
            Logger.getLogger(Dhis2datim.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            
            
            size=list.size();
            
            int i=0;
            
            for(CSVLine obj:list){
                
                    count++;
                    
                    i=count;
                    
                    setProgress((i + 1) * 100 / size);
                    
                    List<String>line=new ArrayList<>();
                    line.add(obj.getDataElementUID());
                    line.add(obj.getPeriod());
                    line.add(obj.getFosa());
                    line.add(obj.getCategorieComboUID());
                    line.add(obj.getAttributeComboUID());
                    line.add(obj.getValue());
                    
                    CSVUtils.writeLine(writer, line);
                     publish(obj.getDataElementUID()+","+obj.getPeriod()+","+obj.getFosa()+
                             ","+obj.getCategorieComboUID()+","+obj.getAttributeComboUID()+","+obj.getValue());
            }
            writer.flush();
                
            writer.close();
            
        } catch (IOException ex) {
                Logger.getLogger(Dhis2datim.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }
    
    @Override
    protected void process(final List<String>chunks){
        for (final String string : chunks) {
            logScreen.append(string);
            logScreen.append("\n");
        }
    }
    
}
