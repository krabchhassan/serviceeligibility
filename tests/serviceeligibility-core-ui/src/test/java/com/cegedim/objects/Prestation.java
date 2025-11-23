package com.cegedim.objects;

public class Prestation {

    private String code;
    private String formule;
    private String parametre;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFormule() {
        return formule;
    }

    public void setFormule(String formule) {
        this.formule = formule;
    }

    public String getParametre() {
        return parametre;
    }

    public void setParametre(String parametre) {
        this.parametre = parametre;
    }

    public Prestation(String code, String formule, String parametre) {
        this.code = code;
        this.formule = formule;
        this.parametre = parametre;
    }

    public Prestation() {
    }

    @Override
    public String toString() {
        return "Prestation [code=" + code + ", formule=" + formule + ", parametre=" + parametre + "]";
    }

}
