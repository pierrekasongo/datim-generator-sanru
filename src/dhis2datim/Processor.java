/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhis2datim;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.Sheet;
import jxl.Workbook;

/**
 *
 * @author ebongo
 */
public class Processor {
    
//    private static final String DATA_FILE_LOCATION="dhis2_data.xls";
//    
//    private static final String METADATA_FILE_LOCATION="metadata.xls";
//    
//    private static final int CATEGORIECOMBO_HTS_TAB_ID=0;
//    
//    
//    
//    private static final int CATEGORIECOMBO_PMTCT_STAT_TAB_ID=1;
//    
//    
//    private static final int ORGUNIT_TAB_ID=2;
//    
//    private static final int DATAELEMENT_TAB_ID=3;
    
    private static List<String> DATAELEMENTKEYS;
    
    
    private Map orgUnits,categorieCombo,dataelement;
    
    
    
    private CategoryComboType type;
    
    private static Connection CNX=null; 
    
    public Processor(CategoryComboType type){
        
        this.orgUnits=new HashMap();
        
        this.orgUnits=loadOrgUnit();
        
        this.categorieCombo=new HashMap();
        
        this.type=type;
        
        this.categorieCombo=loadCategorieCombo(this.type);
        
//        this.categorieComboPMTCT_STAT=new HashMap();
//        
//        this.categorieComboPMTCT_STAT=loadCategorieCombo(CategoryComboType.PMTCT_STAT);
        
        this.dataelement=new HashMap();
        
        this.dataelement=loadDataElement(this.type);
        
        //Connector connector=new Connector();
        
//        try {
//            this.CNX=connector.getConnection();
//        } catch (SQLException ex) {
//            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    
    public static Integer tryParseInt(String value) {
        
        String nb="";
        
        if(value.length() == 0)return 0;
                                        
        for (int i = 0 ; i < value.length(); i++){

            char c=value.charAt(i);
            
            try {
                  nb+= Integer.parseInt(c+"")+"";
            } catch (NumberFormatException ex) {
                 
            }                                         
        }
        return Integer.parseInt(nb);
       
    }
    
    public List<DataStructureDATIM> processPreprocessed(Indicateur ind){
        Workbook workbook=null;
        
        String periode="";
        
        List<DataStructureDATIM>lstData=null;
        
        try{
            
            lstData=new ArrayList<>();
            
            workbook=Workbook.getWorkbook(new File(Constant.DATA_FILE_LOCATION));
            
            Sheet sheet=workbook.getSheet(ind.getNom());
            
            periode="";//sheet.getCell(0,0).getContents();
            
            int countRows=sheet.getRows();
            
            int countColumns=sheet.getColumns();
            
            String  fosa="";
            
            int numerateur=0;
            
            String porteEntree="";
            
            String tranche_age="";
            
            String genre="";
            
            String status="";
            
//            Processor p=new Processor(this.type);
            switch (ind.getNom()) {
                case "HTS_TST":
                case "HTS_TST_2":
                    for(int i =5;i< countRows;i++){//Parcourir toutes les fosa
                        
                        try{
                            if(sheet.getCell(0,i).getContents().contains("Total"))break;
                            
                            fosa=sheet.getCell(0,i).getContents().split("/")[2].trim()+"/"+sheet.getCell(0,i).getContents().split("/")[3].trim();
                        }catch(Exception e){
                            
                        }
                        if(fosa.equals(""))continue;
                        
                        for(int j=1;j < countColumns;j++){//Parcourir les colonnes pour chaque fosa
                            
                            String valeur="0";
                            
                            if(sheet.getCell(j,4).getContents().length() > 0 )
                                genre=sheet.getCell(j,4).getContents();
                            else
                                genre="";
                            
                            if(sheet.getCell(j,3).getContents().length() > 0)
                                tranche_age=sheet.getCell(j,3).getContents();
                            
                            if(genre.length() > 0){
                                
                                if(sheet.getCell(j,2).getContents().length() > 0){//Status change then it's new entry
                                            
                                    status=sheet.getCell(j,2).getContents().trim();
                                    
                                    status=status.equals("Positives")?"Positive":"Negative";
                                            
                                }
                                if(sheet.getCell(j,1).getContents().length() > 0){
                                            
                                            porteEntree=sheet.getCell(j,1).getContents();
                                } 
                                if(sheet.getCell(j,i).getContents().length() > 0)
                                    valeur=tryParseInt(sheet.getCell(j,i).getContents()).toString();

                                //Creation d'une datastructure
                                DataStructureDATIM struct=new DataStructureDATIM(fosa, porteEntree,
                                        tranche_age, genre, valeur, numerateur,status);
                                
                                lstData.add(struct);
                            }
                            
                        }
                    }
                    break;
                case "HTS_INDEX_COMM":
                    for(int i =4;i< countRows;i++){//Parcourir toutes les fosa
                        
                        try{
                            if(sheet.getCell(0,i).getContents().contains("Total"))break;
                            
                            fosa=sheet.getCell(0,i).getContents().split("/")[2].trim();
                        }catch(Exception e){
                            
                        }
                        if(fosa.equals(""))continue;
                        
                        for(int j=1;j < countColumns;j++){//Parcourir les colonnes pour chaque fosa
                            
                            String valeur="0";
                            
                            if(sheet.getCell(j,3).getContents().length() > 0 )
                                genre=sheet.getCell(j,3).getContents();
                            else genre="";
                            
                            if(sheet.getCell(j,2).getContents().length() > 0)
                                tranche_age=sheet.getCell(j,2).getContents();
                            
                            if(genre.length() > 0){
                                
                                if(genre.contains("HTS")){
                                    
                            
                                    String []headers=genre.split(",");

                                    String[]chunk=headers[0].split(" ");

                                    genre=chunk[chunk.length-1];

                                    tranche_age=headers[1].trim();
                                    
                                    status="-";

                                    porteEntree=headers[0].replace(genre, "").trim();                                      
                                    
                                }else{
                                
                                    if(sheet.getCell(j,1).getContents().length() > 0){

                                        status=sheet.getCell(j,1).getContents().trim();

                                        status=status.equals("Positives")?"Positive":"Negative";                                          
                                    }
                                    

                                    if(sheet.getCell(j,0).getContents().length() > 0){

                                        porteEntree=sheet.getCell(j,0).getContents();
                                    } 
                                }
                                if(sheet.getCell(j,i).getContents().length() > 0)
                                        valeur=tryParseInt(sheet.getCell(j,i).getContents()).toString();

                                //Creation d'une datastructure
                                DataStructureDATIM struct=new DataStructureDATIM(fosa, porteEntree,
                                        tranche_age, genre, valeur, numerateur,status);
                                
                                lstData.add(struct);
                            }
                            
                        }
                    }
                    break;
                case "HTS_TST_COMM":
                    for(int i =5;i< countRows;i++){//Parcourir toutes les fosa
                        
                        try{
                            if(sheet.getCell(0,i).getContents().contains("Total"))break;
                            
                            fosa=sheet.getCell(0,i).getContents().split("/")[2].trim();
                        
                            
                        }catch(Exception e){
                            
                        }
                        if(fosa.equals(""))continue;
                        
                        for(int j=1;j < countColumns;j++){//Parcourir les colonnes pour chaque fosa
                            
                            String valeur="0";
                            
                            if(sheet.getCell(j,4).getContents().length() > 0 )
                                genre=sheet.getCell(j,4).getContents();
                            else genre="";
                            
                            if(sheet.getCell(j,3).getContents().length() > 0)
                                tranche_age=sheet.getCell(j,3).getContents();
                            
                            if(genre.length() > 0 && !genre.contains("Total")){
                                
                                if(sheet.getCell(j,2).getContents().length() > 0){//Status change then it's new entry
                                            
                                    status=sheet.getCell(j,2).getContents().trim();
                                    
                                    status=status.equals("Positives")?"Positive":"Negative";
                                            
                                }
                                if(sheet.getCell(j,1).getContents().length() > 0){
                                            
                                            porteEntree=sheet.getCell(j,1).getContents();
                                } 
                                if(sheet.getCell(j,i).getContents().length() > 0)
                                    valeur=tryParseInt(sheet.getCell(j,i).getContents()).toString();
                                
                                //Creation d'une datastructure
                                DataStructureDATIM struct=new DataStructureDATIM(fosa, porteEntree,
                                        tranche_age, genre, valeur, numerateur,status);
                                
                                lstData.add(struct);
                            }
                            
                        }
                    }
                    break;
                case "PMTCT_STAT":
                case "HTS_INDEX":
                case "TB_STAT":
                case "TX_CURR":
                    for(int i =2;i< countRows;i++){//Parcourir toutes les fosa
                        
                        try{
                            
                            if(sheet.getCell(0,i).getContents().contains("Total"))break;
                            
                            fosa=sheet.getCell(0,i).getContents().split("/")[2].trim()+"/"+sheet.getCell(0,i).getContents().split("/")[3].trim();
                            
                        }catch(Exception e){
                            
                        }
                        if(fosa.equals(""))continue;
                        
                        for(int j=1;j < countColumns;j++){//Parcourir les colonnes pour chaque fosa
                            
                            String valeur="0";
                            
                            String header=sheet.getCell(j,1).getContents();
                            
                            if(header.length() > 0 && !header.contains("Total")){
                                
                                
                                String []headers=header.split(",");
                                
                                String[]chunk=headers[0].split(" ");
                                
                                genre=chunk[chunk.length-1];
                                
                                if(headers.length == 3){
                                    
                                    status=headers[2].trim().equals("Positives")?"Positive":"Negative"; 
                                }
                                
                                tranche_age=headers[1].trim();
                                
                                porteEntree=headers[0].replace(genre, "").trim();
                                
                                 if(sheet.getCell(j,i).getContents().length() > 0)
                                    valeur=tryParseInt(sheet.getCell(j,i).getContents()).toString();
                               
                                DataStructureDATIM struct=new DataStructureDATIM(fosa, porteEntree,
                                        tranche_age, genre, valeur, numerateur,status);
                                
                                lstData.add(struct);
                            }
                            
                        }
                    }
                    break;
          
                case "TX_NEW":
                //case "TX_PVLS":
                    for(int i =2;i< countRows;i++){//Parcourir toutes les fosa
                        
                        try{
                            
                            if(sheet.getCell(0,i).getContents().contains("Total"))break;
                            
                            fosa=sheet.getCell(0,i).getContents().split("/")[2].trim()+"/"+sheet.getCell(0,i).getContents().split("/")[3].trim();
                            
                        }catch(Exception e){
                            
                        }
                        if(fosa.equals(""))continue;
                      
                        for(int j=1;j < countColumns;j++){//Parcourir les colonnes pour chaque fosa
                            
                            String valeur="0";
                            
                            String header=sheet.getCell(j,1).getContents();
                            
                            if(header.length() > 0 && !header.contains("Total")){
                                
                                if(header.contains(",")){
                                    
                                    String []headers=header.split(",");
                                
                                    String[]chunk=headers[0].split(" ");

                                    genre=chunk[chunk.length-1];

                                    tranche_age=headers[1].trim();

                                    porteEntree=headers[0].replace(genre, "").trim();
                                }else
                                    porteEntree=header;
                                
                                
                                 if(sheet.getCell(j,i).getContents().length() > 0)
                                    valeur=tryParseInt(sheet.getCell(j,i).getContents()).toString();
                               
                                DataStructureDATIM struct=new DataStructureDATIM(fosa, porteEntree,
                                        tranche_age, genre, valeur, numerateur,status);
                                
                                lstData.add(struct);
                            }
                            
                        }
                    }
                    break;
                case "PMTCT_ART":
                    
                    genre="F";
                            
                    status="Positive";
                    
                    for(int i =3;i< countRows;i++){//Parcourir toutes les fosa
                        
                        try{
                            
                            if(sheet.getCell(0,i).getContents().contains("Total"))break;
                            
                            fosa=sheet.getCell(0,i).getContents().split("/")[2].trim()+"/"+sheet.getCell(0,i).getContents().split("/")[3].trim();
                            
                        }catch(Exception e){
                            
                        }
                        if(fosa.equals(""))continue;
                        
                        for(int j=1;j < countColumns;j++){//Parcourir les colonnes pour chaque fosa
                            
                            String valeur="0";
                            
                            if(sheet.getCell(j,2).getContents().length() > 0)
                                tranche_age=sheet.getCell(j,2).getContents();
                            else tranche_age="";
                            
                            if(tranche_age.length() > 0 && !sheet.getCell(j,2).getContents().contains("Total")){
                                
                                if(sheet.getCell(j,1).getContents().length() > 0){
                                            
                                    porteEntree=sheet.getCell(j,1).getContents();
                                } 
                                if(sheet.getCell(j,i).getContents().length() > 0)
                                    valeur=tryParseInt(sheet.getCell(j,i).getContents()).toString();

                                //Creation d'une datastructure
                                DataStructureDATIM struct=new DataStructureDATIM(fosa, porteEntree,
                                        tranche_age, genre, valeur, numerateur,status);
                                
                                lstData.add(struct);
                            }
                            
                        }
                    }
                    break;
                case "TB_ART":
                    
                    status="Positive";
                    
                    for(int i =4;i< countRows;i++){//Parcourir toutes les fosa
                        
                        try{
                            if(sheet.getCell(0,i).getContents().contains("Total"))break;
                            
                            fosa=sheet.getCell(0,i).getContents().split("/")[2].trim()+"/"+sheet.getCell(0,i).getContents().split("/")[3].trim();
                        }catch(Exception e){
                            
                        }
                        if(fosa.equals(""))continue;
                        
                        for(int j=1;j < countColumns;j++){//Parcourir les colonnes pour chaque fosa
                            
                            String valeur="0";
                            
                            if(sheet.getCell(j,3).getContents().length() > 0 )
                                tranche_age=sheet.getCell(j,3).getContents();
                            else tranche_age="";
                            
                            if(j==1 || (tranche_age.contains("<") && sheet.getCell(j-1,3).getContents().equals(""))){
                                porteEntree=sheet.getCell(j,1).getContents();
                            }
                            
                            
                            if(tranche_age.length() > 0 && !sheet.getCell(j,3).getContents().contains("Total")){
                                
                                if(tranche_age.contains("<")){
                                    
                                    genre=sheet.getCell(j,2).getContents();

                                }
                                if(sheet.getCell(j,i).getContents().length() > 0)
                                    valeur=tryParseInt(sheet.getCell(j,i).getContents()).toString();

                                //Creation d'une datastructure
                                DataStructureDATIM struct=new DataStructureDATIM(fosa, porteEntree,
                                        tranche_age, genre, valeur, numerateur,status);
                                
                                lstData.add(struct);
                            }
                            
                        }
                    }
                    break;
                case "PMTCT_HEI_POS":
                case "PMTCT_EID":
                    
                    for(int i =2;i< countRows;i++){//Parcourir toutes les fosa
                        
                        try{
                            
                            if(sheet.getCell(0,i).getContents().contains("Total"))break;
                            
                            fosa=sheet.getCell(0,i).getContents().split("/")[2].trim()+"/"+sheet.getCell(0,i).getContents().split("/")[3].trim();
                            
                        }catch(Exception e){
                            
                        }
                        if(fosa.equals(""))continue;
                        
                        for(int j=1;j < countColumns;j++){//Parcourir les colonnes pour chaque fosa
                            
                            String valeur="0";
                            
                            String header=sheet.getCell(j,1).getContents();
                            
                            if(header.length() > 0 && !header.contains("Total")){
                                
                                porteEntree=header.trim();
                                
                                 if(sheet.getCell(j,i).getContents().length() > 0)
                                    valeur=tryParseInt(sheet.getCell(j,i).getContents()).toString();
                               
                                DataStructureDATIM struct=new DataStructureDATIM(fosa, porteEntree,
                                        tranche_age, genre, valeur, numerateur,status);
                                
                                lstData.add(struct);
                            }
                            
                        }
                    }
                    break;
                    //Code to be changed
                case "TX_PVLS":
                    for(int i =4;i< countRows;i++){//Parcourir toutes les fosa
                        
                        try{
                            
                            if(sheet.getCell(0,i).getContents().contains("Total"))break;
                            
                            fosa=sheet.getCell(1,i).getContents().trim()+"/"+sheet.getCell(2,i).getContents().trim();
                            
                        }catch(Exception e){
                            
                        }
                        if(fosa.equals(""))continue;
                        
                      
                        for(int j=3;j < countColumns;j++){//Parcourir les colonnes pour chaque fosa
                            
                            String valeur="0";
                            
                            if(sheet.getCell(j,0).getContents().length() > 0)
                                porteEntree=sheet.getCell(j,0).getContents();
                            
                            if(sheet.getCell(j,3).getContents().length() > 0)
                                genre=sheet.getCell(j,3).getContents();
                            
                            if(genre.length() > 0){
                                
                                if(sheet.getCell(j,1).getContents().length() > 0)
                                    status=sheet.getCell(j,1).getContents();
                                
                                if(sheet.getCell(j,2).getContents().length() > 0)
                                    tranche_age=sheet.getCell(j,2).getContents();
                                
                                
                                if(sheet.getCell(j,i).getContents().length() > 0)
                                    valeur=tryParseInt(sheet.getCell(j,i).getContents()).toString();
                               
                                DataStructureDATIM struct=new DataStructureDATIM(fosa, porteEntree,
                                        tranche_age, genre, valeur, numerateur,status);
                                
                                lstData.add(struct);
                            }
                            
                        }
                }
                case "PP_PREV":
                    for(int i =4;i< countRows;i++){//Parcourir toutes les fosa
                        try{
             
                            
                             if(sheet.getCell(0,i).getContents().equals("Total"))break;
                                
                             fosa=sheet.getCell(0,i).getContents().split("/")[2].trim();
                            
                            if("".equals(fosa)) break;
                            
                            
                        }catch(Exception e){
                            
                        }
                                             
                        for(int j=1;j < countColumns;j++){//Parcourir les colonnes pour chaque fosa
                            
                            String valeur="0";
                            
                            if(sheet.getCell(j,3).getContents().length() > 0){
                                    tranche_age=sheet.getCell(j,3).getContents();
                            }else{ 
                                tranche_age="";
                                genre="";
                            }
                            
                            if(sheet.getCell(j,1).getContents().length() > 0)
                                porteEntree=sheet.getCell(j,1).getContents();
                                
                            if(sheet.getCell(j,2).getContents().length() > 0)
                                genre=sheet.getCell(j,2).getContents();
                                
                            if(sheet.getCell(j,i).getContents().length() > 0)
                                valeur=tryParseInt(sheet.getCell(j,i).getContents()).toString();
                               
                            DataStructureDATIM struct=new DataStructureDATIM(fosa, porteEntree,
                                tranche_age, genre, valeur, numerateur,status);
                            //System.out.println(porteEntree+" "+genre+" "+tranche_age);
                            
                                
                            lstData.add(struct);
                            
                        }
                    }
                break;
                case "TX_ML":
                    
                    for(int i =4;i< countRows;i++){//Parcourir toutes les fosa
                        
                        try{
                            
                            if(sheet.getCell(0,i).getContents().equals("Total"))break;
                            
                             fosa=sheet.getCell(0,i).getContents().split("/")[2].trim()+"/"+sheet.getCell(0,i).getContents().split("/")[3].trim();
                            
                        }catch(Exception e){
                            
                        }
                        if(fosa.equals(""))continue;
                        
                      
                        for(int j=1;j < countColumns;j++){//Parcourir les colonnes pour chaque fosa
                            
                            String valeur="0";
                            
                            if(sheet.getCell(j,3).getContents().length() > 0)
                                    tranche_age=sheet.getCell(j,3).getContents();
                            
                            if(tranche_age.length() > 0){
                                
                                if(sheet.getCell(j,1).getContents().length() > 0)
                                    porteEntree=sheet.getCell(j,1).getContents();
                                if(porteEntree.toLowerCase().equals("total"))break;
                                
                                if(sheet.getCell(j,2).getContents().length() > 0)
                                    genre=sheet.getCell(j,2).getContents();
                                
                                
                                if(sheet.getCell(j,i).getContents().length() > 0)
                                    valeur=tryParseInt(sheet.getCell(j,i).getContents()).toString();
                               
                                DataStructureDATIM struct=new DataStructureDATIM(fosa, porteEntree,
                                        tranche_age, genre, valeur, numerateur,status);
                                
                                lstData.add(struct);
                            }  
                        }
                }
                break;
                case "TX_TB":
                    
                    for(int i =4;i< countRows;i++){//Parcourir toutes les fosa
                        
                        try{
                            
                            if(sheet.getCell(0,i).getContents().equals("Total"))break;
                            
                            fosa=sheet.getCell(0,i).getContents().split("/")[2].trim()+"/"+sheet.getCell(0,i).getContents().split("/")[3].trim();
                            
                        }catch(Exception e){
                            
                        }
                        if(fosa.equals(""))continue;
                        
                      
                        for(int j=1;j < countColumns;j++){//Parcourir les colonnes pour chaque fosa
                            
                            String valeur="0";
                            
                            if(sheet.getCell(j,3).getContents().length() > 0)
                                    tranche_age=sheet.getCell(j,3).getContents();
                            else tranche_age="";
                            
                            if(tranche_age.length() > 0){
                                
                                if(sheet.getCell(j,1).getContents().length() > 0)
                                    porteEntree=sheet.getCell(j,1).getContents();
                                if(porteEntree.toLowerCase().equals("total"))break;
                                
                                if(sheet.getCell(j,2).getContents().length() > 0)
                                    genre=sheet.getCell(j,2).getContents();
                                
                                
                                if(sheet.getCell(j,i).getContents().length() > 0)
                                    valeur=tryParseInt(sheet.getCell(j,i).getContents()).toString();
                               
                                DataStructureDATIM struct=new DataStructureDATIM(fosa, porteEntree,
                                        tranche_age, genre, valeur, numerateur,status);
                                
                                lstData.add(struct);
                            }else{
                                genre="";
                            }  
                        }
                    }
                     break;
                
                default:
                        break;
            }
              
        }catch(Exception e){
            System.out.println(e.getMessage()+". Make sure "+ind.getNom()+" file sheet exists.");
        }
        return lstData;
    }
    
    private Map loadOrgUnit(){
        
        Workbook workbook=null;
        
        Map data=null;
        
        try{
                data=new HashMap();

                workbook=Workbook.getWorkbook(new File(Constant.OU_FILE_LOCATION));

                Sheet sheet=workbook.getSheet(Constant.ORGUNIT_TAB_ID);

                int countRows=sheet.getRows();
                
                String nom_fosa="";

                for(int i =1;i < countRows;i++){
                    
                    String zone=sheet.getCell(0,i).getContents().trim();

                    String  fosa=sheet.getCell(1,i).getContents().trim();

                    String  code=sheet.getCell(2,i).getContents().trim();
                    
                    if(zone.length() > 0){
                        
                        nom_fosa=zone+"/"+fosa;
                    }else{
                        nom_fosa=fosa;
                    }
                    
                    data.put(nom_fosa, code);
                }
                
            }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return data;
    }
    
    public String getOrgUnitByKey(String key){
        if(orgUnits.containsKey(key))  
            return this.orgUnits.get(key).toString();
        return "";
    }
    
    private Map loadCategorieCombo(CategoryComboType type){
        
        Workbook workbook=null;
        
        Map data=null;
        
        try{
                data=new HashMap();

                workbook=Workbook.getWorkbook(new File(Constant.METADATA_FILE_LOCATION));
                
                String sheetID="";//(type.equals(CategoryComboType.OTHERS))?CATEGORIECOMBO_HTS_TAB_ID:CATEGORIECOMBO_PMTCT_STAT_TAB_ID;
                
                if(null!=type)switch (type) {
                case HTS_TST:
                case HTS_TST_2:
                    sheetID=Constant.CATEGORIECOMBO_HTS_TAB_ID;
                    break;
                case HTS_INDEX:
                    sheetID=Constant.CATEGORIECOMBO_HTS_INDEX_TAB_ID;
                    break;
                case PMTCT_STAT:
                    sheetID=Constant.CATEGORIECOMBO_PMTCT_STAT_TAB_ID;
                    break;
                case PMTCT_ART:
                    sheetID=Constant.CATEGORIECOMBO_PMTCT_ART_TAB_ID;
                    break;
                case TX_CURR:
                    sheetID=Constant.CATEGORIECOMBO_TX_CURR_TAB;
                    break;
                case TX_NEW:
                    sheetID=Constant.CATEGORIECOMBO_TX_NEW_TAB;
                    break;
                case TX_PVLS:
                    sheetID=Constant.CATEGORIECOMBO_TX_PVLS_TAB_ID;
                    break;
                case TB_STAT:
                    sheetID=Constant.CATEGORIECOMBO_TB_STAT_TAB_ID;
                    break;
                case TB_ART:
                    sheetID=Constant.CATEGORIECOMBO_TB_ART_TAB_ID;
                    break;
                case TX_TB:
                    sheetID=Constant.CATEGORIECOMBO_TX_TB_TAB_ID;
                    break;
                case TB_PREV:
                    sheetID=Constant.CATEGORIECOMBO_TB_PREV_TAB_ID;
                    break;
                case PMTCT_FO:
                    sheetID=Constant.CATEGORIECOMBO_PMTCT_FO_TAB_ID;
                    break;
                 case GENDER_GBV:
                    sheetID=Constant.CATEGORIECOMBO_GENDER_GBV_TAB_ID;
                    break;
                case PMTCT_EID:
                    sheetID=Constant.CATEGORIECOMBO_PMTCT_EID_TAB_ID;
                    break;
                case PMTCT_HEI_POS:
                    sheetID=Constant.CATEGORIECOMBO_PMTCT_HEI_POS_TAB_ID;
                    break;
                case HTS_TST_COMM:
                    sheetID=Constant.CATEGORIECOMBO_HTS_TST_COMM_TAB_ID;
                    break;
                case HTS_INDEX_COMM:
                    sheetID=Constant.CATEGORIECOMBO_HTS_INDEX_COMM_TAB_ID;
                    break;
                case PP_PREV:
                    sheetID=Constant.CATEGORIECOMBO_PP_PREV_TAB_ID;
                    break;
                case LAB_PT_CQI:
                    sheetID=Constant.CATEGORIECOMBO_LAB_PT_CQI_TAB_ID;
                    break;
                case HRH_CURR:
                    sheetID=Constant.CATEGORIECOMBO_HRH_CURR_TAB_ID;
                    break;
                case EMR_SITE:
                    sheetID=Constant.CATEGORIECOMBO_EMR_SITE_TAB_ID;
                    break;
                case TX_ML:
                     sheetID=Constant.CATEGORIECOMBO_TX_ML_TAB_ID;
                    break;
                default:
                    break;
            }
                
                Sheet sheet=workbook.getSheet(sheetID);

                int countRows=sheet.getRows();
                
                String groupe="";
                
                String porte=type.toString();
                
                for(int i =1;i < countRows;i++){

                    String  nom=sheet.getCell(0,i).getContents();
                    
                    if(porte.equals("HTS_TST") || porte.equals("HTS_TST_2")){
                        
                        if(nom.contains("GROUPE")){
                            groupe=nom;
                        }
                        nom+="_"+groupe;
                    }

                    String  code=sheet.getCell(1,i).getContents();

                    data.put(nom, code);
                }
                
            }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return data;
    }
    
    public static String makeCategorieName(String tranche,String genre,String statut){
        
        if(tranche.equals("default"))
            return "default";
        if(genre.equals("-"))
            return tranche.trim()+", "+genre.trim();
        else if(genre.equals(""))
            return tranche.trim()+", "+statut.trim();
        return tranche.trim()+", "+genre+", "+statut.trim();
        
    }
    
    public String getCategorieComboByKey(String key){
        
        String result="";

//        if(type.equals(CategoryComboType.HTS)){
            
            if(categorieCombo.containsKey(key))  
                result=categorieCombo.get(key).toString();
//        }
        
        return result;
    }
    public Map loadDataElement(CategoryComboType type){
        
        Workbook workbook=null;
        
        Map data=null;
        
        String sheetID="";
        
        DATAELEMENTKEYS=new ArrayList<>();
        
        try{
                data=new HashMap();

                workbook=Workbook.getWorkbook(new File(Constant.METADATA_FILE_LOCATION));

                if(null!=type)
                    switch (type) {
                        case HTS_TST:
                        case HTS_TST_2:
                            sheetID=Constant.DATAELEMENT_HTS_TAB_ID;
                            break;
                        case HTS_INDEX:
                            sheetID=Constant.DATAELEMENT_HTS_INDEX_TAB_ID;
                            break;
                        case HTS_INDEX_COMM:
                            sheetID=Constant.DATAELEMENT_HTS_INDEX_COMM_TAB_ID;
                            break;
                        case PMTCT_STAT:
                            sheetID=Constant.DATAELEMENT_PMTCT_STAT_TAB_ID;
                            break;
                        case PMTCT_ART:
                            sheetID=Constant.DATAELEMENT_PMTCT_ART_TAB_ID;
                            break;
                        case TX_CURR:
                            sheetID=Constant.DATAELEMENT_TX_CURR_TAB;
                            break;
                        case TX_NEW:
                            sheetID=Constant.DATAELEMENT_TX_NEW_TAB;
                            break;
                        case TX_PVLS:
                            sheetID=Constant.DATAELEMENT_TX_PVLS_TAB_ID;
                            break;
                        case TB_STAT:
                            sheetID=Constant.DATAELEMENT_TB_STAT_TAB_ID;
                            break;
                        case TB_ART:
                            sheetID=Constant.DATAELEMENT_TB_ART_TAB_ID;
                            break;
                        case TX_TB:
                            sheetID=Constant.DATAELEMENT_TX_TB_TAB_ID;
                            break;
                        case TB_PREV:
                            sheetID=Constant.DATAELEMENT_TB_PREV_TAB_ID;
                            break;
                        case PMTCT_FO:
                            sheetID=Constant.DATAELEMENT_PMTCT_FO_TAB_ID;
                            break;
                        case GENDER_GBV:
                            sheetID=Constant.DATAELEMENT_GENDER_GBV_TAB_ID;
                            break;
                        case PMTCT_EID:
                            sheetID=Constant.DATAELEMENT_PMTCT_EID_TAB_ID;
                            break;
                        case PMTCT_HEI_POS:
                            sheetID=Constant.DATAELEMENT_PMTCT_HEI_POS_ID;
                            break;
                        case HTS_TST_COMM:
                            sheetID=Constant.DATAELEMENT_HTS_TST_COMM_ID;
                            break;
                        case PP_PREV:
                            sheetID=Constant.DATAELEMENT_PP_PREV_ID;
                            break;
                        case LAB_PT_CQI:
                            sheetID=Constant.DATAELEMENT_LAB_PT_CQI_ID;
                            break;
                        case HRH_CURR:
                            sheetID=Constant.DATAELEMENT_HRH_CURR_ID;
                            break;
                        case EMR_SITE:
                            sheetID=Constant.DATAELEMENT_EMR_SITE_ID;
                            break;
                        case TX_ML:
                            sheetID=Constant.DATAELEMENT_TX_ML_ID;
                            break;
                        default:
                            break;
                    }
                
                Sheet sheet=workbook.getSheet(sheetID);

                int countRows=sheet.getRows();

                for(int i =1;i < countRows;i++){

                    String  nom=sheet.getCell(0,i).getContents();

                    String  code=sheet.getCell(1,i).getContents();
                    
                    DATAELEMENTKEYS.add(nom);
                    
                    data.put(nom, code);
                }
                
            }catch(Exception e){
            System.out.println(e.getMessage());
        }                                                                                                                                                          
        return data;
    }
    public String getDataElementByKey(String _key,Indicateur ind){
 
        String key=replaceWithKey(_key,ind);
       

        if(dataelement.containsKey(key))  
            return this.dataelement.get(key).toString();
        return "";
    }
    
    private String replaceWithKey(String value,Indicateur ind){
        
        for(String s:DATAELEMENTKEYS){
            //System.out.println(s);
            /*if(ind.getNom().equals("TX_RET") || ind.getNom().equals("TB_STAT_TB_ART") || 
                    ind.getNom().equals("TB_PREV")|| ind.getNom().equals("TX_TB")||ind.getNom().equals("GENDER_GBV")
                    ||ind.getNom().equals("PP_PREV") || ind.getNom().equals("LAB_PT_CQI") || ind.getNom().equals("EMR_SITE")){
                if(value.equals(s))
                    return s;
            }else
            {
                if(value.contains(s))
                    return s;
            }*/
                
            if(value.contains(s))
                    return s;
        }
        return value;
    }
    
    private static String getFosaID(String _name){
        return "";
    }
    
}
