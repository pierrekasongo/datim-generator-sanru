/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhis2datim;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.write.WriteException;

/**
 *
 * @author ebongo
 */
public class Dhis2datim {

    /**
     * @param args the command line arguments
     * Format final du fichier csv
     * Data Element	Period	Org unit	Category Combo	Attribute Combo	value
     */
    
    private static List<CSVLine>list;
    
    private static List<Indicateur>LST_INDICATEUR;
    
    private static Connection CNX=null;
    
    private static Processor processor;
    
    private static int count;
    
    private static String PERIOD="2018Q3";
  
    public static void main(String[] args) throws IOException, WriteException/*, BiffException, SQLException*/ {
        // TODO code application logic here      
        
        list=new ArrayList<>();
        
        Constant.printLogo();

        System.out.println("Data element,Period,OrganisationUnit,CategorieCombo,AttributeCombo,Value");
        
        
        for(CategoryComboType type:CategoryComboType.values()){
            
//            if(type.toString().equals("TB_STAT_TB_ART") || type.toString().equals("TX_TB") 
//                    || type.toString().equals("PMTCT_FO")|| type.toString().equals("GENDER_GBV") 
//                    || type.toString().equals("HTS_TST_COMM") || type.toString().equals("TX_RET")
//                    || type.toString().equals("EMR_SITE") || type.toString().equals("PP_PREV"))continue;
            
            if(type.toString().equals("HTS_TST") || type.toString().equals("HTS_TST_2") 
                    || type.toString().equals("PMTCT_STAT")                   
                    || type.toString().equals("PMTCT_ART")|| type.toString().equals("TX_NEW") 
                    || type.toString().equals("TX_CURR") || type.toString().equals("PMTCT_EID") 
                    || type.toString().equals("PMTCT_HEI_POS") || type.toString().equals("HTS_TST_COMM") 
                    || type.toString().equals("TX_PVLS") || type.toString().equals("PP_PREV")
                    || type.toString().equals("GENDER_GBV")
                    || type.toString().equals("TB_STAT_TB_ART") || type.toString().equals("TX_TB")
                    || type.toString().equals("TB_PREV") || type.toString().equals("PMTCT_FO") 
                    || type.toString().equals("EMR_SITE") || type.toString().equals("LAB_PT_CQI")){
            //if(type.toString().equals("TB_PREV")){
                
                System.err.println(type.toString());
                
                processor=new Processor(type);
            
                Indicateur ind=new Indicateur(type.toString());

                List<DataStructureDATIM>lstData = new ArrayList<>();

                lstData = processor.processPreprocessed(ind);

                fillList(lstData,ind);
            }
        }
        
        writeToCSV();
        
    }
    
    private static void fillList(List<DataStructureDATIM>lstData,Indicateur ind){
        
        
           for (DataStructureDATIM ds : lstData) {
                              

                String fosaUID=processor.getOrgUnitByKey(ds.getFosa().trim());
                
                if(fosaUID.equals("")){
                    System.err.println("FOSA "+ds.getFosa());
                    continue;
                }
                count++;

                String dataelement=ds.getPorte();

                String dataelementUID=processor.getDataElementByKey(dataelement,ind);

                String categorieComboUID,categorieCombo="";


                if(ind.getNom().equals("PMTCT_STAT") || ind.getNom().equals("HTS_TST") || ind.getNom().equals("HTS_TST_2")|| ind.getNom().equals("TX_CURR")){
                    if(ind.getNom().equals("TX_CURR")){
                        if(ds.getTranche().equals("NUM") || ds.getStatut().equals("ok"))
                            categorieCombo="Positive";
                        else
                            categorieCombo=Processor.makeCategorieName(ds.getTranche(),ds.getGenre(),ds.getStatut());
                    }else if(ind.getNom().equals("HTS_TST") && dataelement.contains("PMTCT")){

                        categorieCombo=Processor.makeCategorieName(ds.getTranche(),ds.getGenre(),ds.getStatut())+"_PMTCT";

                    }else if(ind.getNom().equals("PMTCT_STAT") && ds.getStatut().equals("-")){
                        categorieCombo=ds.getTranche()+", "+ds.getGenre();
                    }
                    else
                        categorieCombo=Processor.makeCategorieName(ds.getTranche(),ds.getGenre(),ds.getStatut());
                }
                else if(ind.getNom().equals("PMTCT_ART"))
                    categorieCombo=ds.getCategorie();
                else if(ind.getNom().equals("TX_NEW")){

                    if(ds.getStatut().equals("-")){

                        categorieCombo=ds.getTranche();

                    }else{

                        categorieCombo=Processor.makeCategorieName(ds.getTranche(),ds.getGenre(),ds.getStatut());
                    }
                }else if(ind.getNom().equals("TX_PVLS")){
                    
                    if(!ds.getGenre().contains("Unknown")){
                        
                        categorieCombo=processor.makeCategorieName(ds.getTranche(),ds.getGenre(),ds.getStatut()+ ", Positive");
                        
                    }else if(ds.getGenre().contains("Unknown")){
                        
                        if(ds.getTranche().contains("Undocumented"))
                            categorieCombo=processor.makeCategorieName(ds.getStatut(),ds.getTranche(),"Positive");
                        else if(ds.getTranche().toLowerCase().equals("routine") || ds.getTranche().toLowerCase().equals("targeted")){
                            if(ds.getStatut().equals("-"))
                                categorieCombo=processor.makeCategorieName(ds.getTranche(),"","Positive");
                            else
                                categorieCombo=processor.makeCategorieName(ds.getStatut(),ds.getTranche(),"Positive");
                        }else{
                            categorieCombo=processor.makeCategorieName(ds.getTranche(),ds.getGenre(),ds.getStatut()+", Positive");
                        }
                        
                    }
                    else{
                        if(ds.getStatut().equals("-")){
                            
                            categorieCombo=processor.makeCategorieName(ds.getTranche(),"","Positive");
                            
                        }else{
                            
                            categorieCombo=processor.makeCategorieName(ds.getStatut(), ds.getTranche(), "Positive");
                        }
                    }
                }else if(ind.getNom().equals("TB_STAT_TB_ART")){
                    
                    if(ds.getStatut().length()>0){
                        
                        if(ds.getStatut().equals("-")){
                            if(ds.getTranche().equals("default")){
                                if(ds.getGenre().toLowerCase().contains("unknown")){
                                    categorieCombo=processor.makeCategorieName(ds.getTranche(), "", "");
                                }else
                                     categorieCombo=ds.getTranche()+", "+ds.getGenre();
                            }
                            else
                                categorieCombo=ds.getTranche()+", "+ds.getGenre();
                        }else if(ds.getStatut().toLowerCase().contains("entry") || ds.getStatut().toLowerCase().contains("newly")){
                            
                            if(ds.getStatut().toLowerCase().equals("entry-p"))
                                categorieCombo=ds.getTranche()+", Known at Entry Positive, "+ds.getGenre();
                            else if(ds.getStatut().toLowerCase().equals("newly-p"))
                                categorieCombo=ds.getTranche()+", Newly Identified Positive, "+ds.getGenre();
                            else if(ds.getStatut().toLowerCase().equals("newly-n"))
                                categorieCombo=ds.getTranche()+", Newly Identified Negative, "+ds.getGenre();
                        }else{
                            
                            if(ds.getTranche().toLowerCase().equals("positive"))
                                categorieCombo="Positive";
                            else if(ds.getTranche().toLowerCase().equals("already"))
                                categorieCombo="Life-long ART, Already, Positive";
                            else if(ds.getTranche().toLowerCase().equals("new"))
                                categorieCombo="Life-long ART, New, Positive";
                            else{
                                String statut=(ds.getStatut().equals("P"))?"Positive":"Negative";

                                categorieCombo=processor.makeCategorieName(ds.getTranche(), ds.getGenre(), statut);
                            }
                        }
                    }
                    
                }else if(ind.getNom().equals("TB_PREV")){
                    
                    if(ds.getStatut().length() > 0){
                        
                        if(ds.getStatut().equals("-")){
                            
                            categorieCombo=ds.getTranche();
                        }else{
                            String statut=(ds.getStatut().equals("P"))?"Positive":"Negative";
                            
                            categorieCombo=processor.makeCategorieName(ds.getTranche(), ds.getGenre(), statut);
                        }
                    }
                }else if(ind.getNom().equals("TX_TB")){
                    
                    String statut=(ds.getStatut().equals("P"))?"Positive":"Negative";
                    
                    if(ds.getStatut().length() > 0){
                        
                        if(ds.getGenre().toLowerCase().contains("unknown")){
                            
                            if(ds.getTranche().toLowerCase().equals("positive"))
                                categorieCombo=statut;
                            else
                                categorieCombo=ds.getTranche()+", "+statut;
                        }else{
                            
                            categorieCombo=processor.makeCategorieName(ds.getTranche(), ds.getGenre(), statut);
                        }
                    }
                }else if(ind.getNom().equals("PMTCT_FO")){
                    
                    if(ds.getStatut().length() > 0){
                        categorieCombo=ds.getStatut().trim();
                    }
                }else 
                    if(ind.getNom().equals("GENDER_GBV")){
                    
                    String status="";
                    
                    if(ds.getStatut().length() > 0){
                        
                        if(ds.getStatut().equals("default"))
                            categorieCombo=ds.getStatut();
                        else if(ds.getStatut().toLowerCase().contains("prc")){
                            
                            status="Sexual Violence (Post-Rape Care)";
                                    
                            categorieCombo=processor.makeCategorieName(ds.getTranche(), ds.getGenre(),status);
                        }
                        else
                            categorieCombo=ds.getTranche()+", "+ds.getGenre();
                    }
                }else if(ind.getNom().equals("PMTCT_EID") || ind.getNom().equals("PMTCT_HEI_POS")){
                    
                    String status="";
                    
                    if(ds.getStatut().length()  > 0){
                        
                        categorieCombo=ds.getStatut();
                    }
                }else if(ind.getNom().equals("HTS_TST_COMM")){
                    
                    if(ds.getTranche().equals("default"))
                            categorieCombo=ds.getTranche();
                        else
                            categorieCombo=Processor.makeCategorieName(ds.getTranche(),ds.getGenre(),ds.getStatut());
                }else if(ind.getNom().equals("PP_PREV")){
                    
                    if(ds.getGenre().contains("Unknown")){
                        
                       categorieCombo=ds.getTranche();
                    } else
                        categorieCombo=ds.getTranche()+", "+ds.getGenre();
                }else if(ind.getNom().equals("LAB_PT_CQI")){
                    
                    categorieCombo=ds.getStatut();
                    
                }else if(ind.getNom().equals("HRH_CURR")){
                    
                    categorieCombo=ds.getTranche()+", "+ds.getStatut();
                    
                }else if(ind.getNom().equals("EMR_SITE")){
                    
                    categorieCombo="Service Delivery Area - Care and Treatment";
                }

                categorieComboUID=processor.getCategorieComboByKey(categorieCombo);


                System.out.println(count+" "+dataelement+","+dataelementUID+","+PERIOD+","+ds.getFosa()+","+fosaUID+","+
                        categorieCombo+","+categorieComboUID+","+ds.getValeur());               
                
                //list.add(new CSVLine(dataelementUID, PERIOD, fosaUID, categorieComboUID,ds.getValeur()+""));
        }
        System.out.println(count+" Lines added successfully!");
    }

    private static void writeToCSV(){
        
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
                for(CSVLine obj:list){
                    
                    List<String>line=new ArrayList<>();
                    line.add(obj.getDataElementUID());
                    line.add(obj.getPeriod());
                    line.add(obj.getFosa());
                    line.add(obj.getCategorieComboUID());
                    line.add(obj.getAttributeComboUID());
                    line.add(obj.getValue());
                    
                    CSVUtils.writeLine(writer, line);
                }
                writer.flush();
                
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Dhis2datim.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }    
}
