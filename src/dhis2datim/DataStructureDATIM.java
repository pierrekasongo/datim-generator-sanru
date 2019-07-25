/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhis2datim;

/**
 *
 * @author ebongo
 */
public class DataStructureDATIM {
    private String ou="";
    private String ouUID="";
    private String de="";
    private String deUID="";
    private String cc="";
    private String ccUID="";
    private String value="";
    
    
    public DataStructureDATIM(String _ou, String _ouUID, String _de, String _deUID,
            String _cc, String _ccUID, String _value){
        this.ou=_ou;
        this.ouUID=_ouUID;
        this.de=_de;
        this.deUID=_deUID;
        this.cc=_cc;
        this.ccUID=_ccUID;
        this.value=_value;
    }

    /**
     * @return the ou
     */
    public String getOu() {
        return ou;
    }

    /**
     * @param ou the ou to set
     */
    public void setOu(String ou) {
        this.ou = ou;
    }

    /**
     * @return the ouUID
     */
    public String getOuUID() {
        return ouUID;
    }

    /**
     * @param ouUID the ouUID to set
     */
    public void setOuUID(String ouUID) {
        this.ouUID = ouUID;
    }

    /**
     * @return the de
     */
    public String getDe() {
        return de;
    }

    /**
     * @param de the de to set
     */
    public void setDe(String de) {
        this.de = de;
    }

    /**
     * @return the deUID
     */
    public String getDeUID() {
        return deUID;
    }

    /**
     * @param deUID the deUID to set
     */
    public void setDeUID(String deUID) {
        this.deUID = deUID;
    }

    /**
     * @return the cc
     */
    public String getCc() {
        return cc;
    }

    /**
     * @param cc the cc to set
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     * @return the ccUID
     */
    public String getCcUID() {
        return ccUID;
    }

    /**
     * @param ccUID the ccUID to set
     */
    public void setCcUID(String ccUID) {
        this.ccUID = ccUID;
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