package com.cegedim.objects;

import java.util.ArrayList;

public class Garantie {
    private String code;
    private String libelle;
    private String priorite;
    private String dateDebut;
    private String dateFin;
    private String taux;
    private ArrayList<Prestation> prestations;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getTaux() {
        return taux;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }

    public ArrayList<Prestation> getPrestations() {
        return prestations;
    }

    public void setPrestations(ArrayList<Prestation> prestations) {
        this.prestations = prestations;
    }

    public Garantie(String code, String libelle, String priorite, String dateDebut, String dateFin, String taux,
            ArrayList<Prestation> prestations) {
        super();
        this.code = code;
        this.libelle = libelle;
        this.priorite = priorite;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.taux = taux;
        this.prestations = prestations;
    }

    @Override
    public String toString() {
        return "Garantie [code=" + code + ", libelle=" + libelle + ", priorite=" + priorite + ", dateDebut=" + dateDebut
                + ", dateFin=" + dateFin + ", taux=" + taux + ", prestations=" + prestations + "]";
    }

}
