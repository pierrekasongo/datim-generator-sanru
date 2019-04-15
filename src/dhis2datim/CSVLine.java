/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhis2datim;

/**
 *
 * @author pierre
 */
public class CSVLine {
    
    private String dataElementUID;
    private String period;
    private String fosa;
    private String categorieComboUID;
    private String attributeComboUID;
    private String value;
    
    public CSVLine(String de,String pe,String fosa,String cc,String ac,String v){
        this.dataElementUID=de;
        this.period=pe;
        this.fosa=fosa;
        this.categorieComboUID=cc;
        this.attributeComboUID=ac;
        this.value=v;
    }

    /**
     * @return the dataElementUID
     */
    public String getDataElementUID() {
        return dataElementUID;
    }

    /**
     * @param dataElementUID the dataElementUID to set
     */
    public void setDataElementUID(String dataElementUID) {
        this.dataElementUID = dataElementUID;
    }

    /**
     * @return the period
     */
    public String getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(String period) {
        this.period = period;
    }

    /**
     * @return the fosa
     */
    public String getFosa() {
        return fosa;
    }

    /**
     * @param fosa the fosa to set
     */
    public void setFosa(String fosa) {
        this.fosa = fosa;
    }

    /**
     * @return the categorieComboUID
     */
    public String getCategorieComboUID() {
        return categorieComboUID;
    }

    /**
     * @param categorieComboUID the categorieComboUID to set
     */
    public void setCategorieComboUID(String categorieComboUID) {
        this.categorieComboUID = categorieComboUID;
    }

    /**
     * @return the attributeComboUID
     */
    public String getAttributeComboUID() {
        return attributeComboUID;
    }

    /**
     * @param attributeComboUID the attributeComboUID to set
     */
    public void setAttributeComboUID(String attributeComboUID) {
        this.attributeComboUID = attributeComboUID;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    
}
