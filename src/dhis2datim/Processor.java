/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhis2datim;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Sheet;
import jxl.Workbook;

/**
 *
 * @author ebongo
 */
public final class Processor {

    private Map orgUnits,categorieCombo,dataelement;
    private String data_file_location;
    
    public Processor(String _file_location){
        
        this.orgUnits=new HashMap();
        
        this.orgUnits=loadOrgUnit();
        
        this.categorieCombo=new HashMap();
        
        this.categorieCombo=loadCategorieCombo();

        
        this.dataelement=new HashMap();
        
        this.dataelement=loadDataElement();
        
        this.data_file_location=_file_location;
        
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
    
    public List<DataStructureDATIM> processData(){
        
        Workbook workbook=null;
        
        String periode="";
        
        List<DataStructureDATIM>lstData=null;
        
        try{
            
            lstData=new ArrayList<>();
            
            workbook=Workbook.getWorkbook(new File(this.data_file_location));
            
            Sheet sheet=workbook.getSheet(0);
            
            int countRows=sheet.getRows();
            
            int countColumns=sheet.getColumns();
            
            String ou="";
            String ouUID="";
            String de="";
            String deUID="";
            String cc="";
            String ccUID="";
            String value="";
            
           
            for(int i =1;i< countRows;i++){//Rows
                
                if(sheet.getCell(1,i).getContents().length() > 0){
                    
                    deUID = sheet.getCell(1,i).getContents().trim();
                
                    de = sheet.getCell(2,i).getContents().trim();

                    ou = sheet.getCell(3,i).getContents().trim();

                    ouUID = sheet.getCell(4,i).getContents().trim();

                    cc = sheet.getCell(7,i).getContents().trim();

                    ccUID = sheet.getCell(8,i).getContents().trim();

                    value=tryParseInt(sheet.getCell(11,i).getContents()).toString();

                    DataStructureDATIM struct=new DataStructureDATIM(ou, ouUID, de, deUID, cc, ccUID, value);

                    lstData.add(struct);
                }else
                    break;
            }
                            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return lstData;
    }
    
    private Map loadOrgUnit(){
        
        Workbook workbook=null;
        
        Map data=null;
        
        try{
                data=new HashMap();

                workbook=Workbook.getWorkbook(new File(Constant.METADATA_FILE_LOCATION));
   
                Sheet sheet=workbook.getSheet(Constant.ORGUNIT_TAB_ID);

                int countRows=sheet.getRows();
                
                
                for(int i =1;i < countRows;i++){

                    String  dhis2_code=sheet.getCell(1,i).getContents().trim();
                    
                    String datim_code = sheet.getCell(2,i).getContents().trim();
                    
                    data.put(dhis2_code, datim_code);
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
    
    private Map loadCategorieCombo(){
        
        Workbook workbook=null;
        
        Map data=null;
        
        try{
                data=new HashMap();

                workbook=Workbook.getWorkbook(new File(Constant.METADATA_FILE_LOCATION));
   
                Sheet sheet=workbook.getSheet(Constant.CATEGORIECOMBO_TAB_ID);

                int countRows=sheet.getRows();
                
                
                for(int i =1;i < countRows;i++){

                    String  dhis2_code=sheet.getCell(1,i).getContents().trim();
                    
                    String datim_code = sheet.getCell(2,i).getContents().trim();
                    
                    data.put(dhis2_code, datim_code);
                }
                
            }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return data;
    }
    
    public String getCategorieComboByKey(String key){
        
        if(categorieCombo.containsKey(key))  
            return categorieCombo.get(key).toString();
        
        return "";
    }
    
    public Map loadDataElement(){
        
        Workbook workbook=null;
        
        Map data=null;
        
        
        try{
                data=new HashMap();

                workbook=Workbook.getWorkbook(new File(Constant.METADATA_FILE_LOCATION));
                
                Sheet sheet=workbook.getSheet(Constant.DATAELEMENT_TAB_ID);

                int countRows=sheet.getRows();

                for(int i =1;i < countRows;i++){

                    String  dhis2_code=sheet.getCell(2,i).getContents().trim();
                    
                    String datim_code = sheet.getCell(3,i).getContents().trim();
                    
                    data.put(dhis2_code, datim_code);
                }
                
            }catch(Exception e){
            System.out.println(e.getMessage());
        }                                                                                                                                                          
        return data;
    }
    public String getDataElementByKey(String key){

        if(dataelement.containsKey(key))  
            return this.dataelement.get(key).toString();
        return "";
    }
    
}
