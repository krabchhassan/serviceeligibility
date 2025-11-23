package com.cegedim.objects;

public class Volumetrie {
    int indiceLigne;
    String codePartenaire;
    String amc;
    String nbDeclaration;
    String nbPersonne;
    String nbPersDroitOuvert;
    String nbPersDroitFerme;

    public Volumetrie(int indiceLigne, String codePartenaire, String amc, String nbDeclaration, String nbPersonne,
            String nbPersDroitOuvert, String nbPersDroitFerme) {
        this.indiceLigne = indiceLigne;
        this.codePartenaire = codePartenaire;
        this.amc = amc;
        this.nbDeclaration = nbDeclaration;
        this.nbPersonne = nbPersonne;
        this.nbPersDroitOuvert = nbPersDroitOuvert;
        this.nbPersDroitFerme = nbPersDroitFerme;
    }

    public int getIndiceLigne() {
        return indiceLigne;
    }

    public String getCodePartenaire() {
        return codePartenaire;
    }

    public String getAmc() {
        return amc;
    }

    public String getNbDeclaration() {
        return nbDeclaration;
    }

    public String getNbPersonne() {
        return nbPersonne;
    }

    public String getNbPersDroitOuvert() {
        return nbPersDroitOuvert;
    }

    public String getNbPersDroitFerme() {
        return nbPersDroitFerme;
    }

}
