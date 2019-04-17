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
    private String fosa="";
    private String porte="";
    private String tranche="";
    private String genre="";
    private String valeur="";
    private int numerateur=0;
    private String statut="";
    private String categorie="";
    
    public DataStructureDATIM(String _fosa,String  _porte,String _tranche,
            String _genre,String _valeur,int _numerateur,String _statut){
        this.fosa=_fosa;
        this.porte=_porte;
        this.tranche=_tranche;
        this.genre=_genre;
        this.valeur=_valeur;
        this.numerateur=_numerateur;
        this.statut=_statut;
    }
    
    public DataStructureDATIM(String _fosa,String  _porte,String _categorie,
            String _valeur,int _numerateur){
        this.fosa=_fosa;
        this.porte=_porte;
        this.categorie=_categorie;
        this.valeur=_valeur;
        this.numerateur=_numerateur;
    }

    /**
     * @return the fosa
     */
    public String getFosa() {
        return fosa;
    }

    /**
     * @return the porte
     */
    public String getPorte() {
        return porte;
    }

    /**
     * @param porte the porte to set
     */
    public void setPorte(String porte) {
        this.porte = porte;
    }

    /**
     * @param process
     * @return the tranche
     */
    public String getTranche() {
        
        String age="";
        
        if(tranche.contains(",") || tranche.contains("_"))
            return tranche;
        if(tranche.toLowerCase().contains("non documented"))
            return "Undocumented Test Indication";
        
        if(tranche.trim().toLowerCase().startsWith("num") ||
                tranche.trim().toLowerCase().startsWith("den"))
            return "default";
        
        else if(tranche.trim().contains(" ") && tranche.trim().contains("an")){
            
            String []splited = tranche.split(" ");
        
            age=splited[0].trim();
            
            if(age.startsWith("+"))
                age=age.replace('+', ' ').trim()+"+";
            if(age.startsWith(">"))
                age=age.replace('>', ' ').trim()+"+";
            return age;
        }
        return tranche;      
    }

    /**
     * @param tranche the tranche to set
     */
    public void setTranche(String tranche) {
        this.tranche = tranche;
    }

    /**
     * @return the genre
     */
    public String getGenre() {
       
        if(genre.startsWith("F")){
            return "Female";
        }else if(genre.startsWith("M")){
            return "Male";
        }else return "Unknown Sex";
        
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @return the valeur
     */
    public String getValeur() {
        return valeur;
    }

    /**
     * @param valeur the valeur to set
     */
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    /**
     * @return the numerateur
     */
    public int getNumerateur() {
        return numerateur;
    }

    /**
     * @param numerateur the numerateur to set
     */
    public void setNumerateur(int numerateur) {
        this.numerateur = numerateur;
    }

    /**
     * @return the statut
     */
    public String getStatut() {
        return statut;
    }

    /**
     * @param statut the statut to set
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    /**
     * @return the categorie
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * @param categorie the categorie to set
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
    
}
