/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhis2datim;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebongo
 */
public class Indicateur {
    
    private String nom;
    
    private static List<Indicateur>lstIndicateur;
    
    public Indicateur(){
        this.lstIndicateur=new ArrayList<>();
        lstIndicateur.add(new Indicateur("HTS"));
        lstIndicateur.add(new Indicateur("PMTCT_STAT"));
        lstIndicateur.add(new Indicateur("PMTCT_ART"));
        lstIndicateur.add(new Indicateur("TX_CURR"));
        lstIndicateur.add(new Indicateur("TX_NEW"));
        lstIndicateur.add(new Indicateur("TX_PVLS"));
        lstIndicateur.add(new Indicateur("TX_RET"));
        lstIndicateur.add(new Indicateur("TB_STAT_TB_ART"));
    }
    
    public Indicateur findIndicateurByName(String name){
        
        for(Indicateur ind:lstIndicateur){
            if(ind.getNom().equals("name"))
                return ind;
        }
        return null;
    }
    
    public Indicateur(String _nom){
        
        this.nom=_nom;
    }

    /**
     * @return the porte
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param porte the porte to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the lstIndicateur
     */
    public List<Indicateur> getLstIndicateur() {
        return lstIndicateur;
    }

    /**
     * @param lstIndicateur the lstIndicateur to set
     */
    public void setLstIndicateur(List<Indicateur> lstIndicateur) {
        this.lstIndicateur = lstIndicateur;
    }
    
    
}
