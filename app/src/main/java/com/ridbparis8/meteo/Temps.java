package com.ridbparis8.meteo;

import java.util.Calendar;

public class Temps {

    int    dt_unix;
    int    an;
    int    mois;
    int    jour;
    String dt_text;
    String nomDeAn;
    String nomDeMois;
    String nomDeJours;

    public Temps(int dt_unix, String dt_text) {
        this.dt_unix = dt_unix;
        this.dt_text = dt_text;

        Calendar mydate = Calendar.getInstance();
        mydate.setTimeInMillis((long)dt_unix*1000);

        // Formattage de l'année, du mois et du jour
        this.an   = mydate.get(Calendar.YEAR);
        this.mois = mydate.get(Calendar.MONTH);
        this.jour = mydate.get(Calendar.DAY_OF_WEEK);

        this.nomDeMois = Utilites.getMois(mydate.get(Calendar.MONTH));
        this.nomDeJours = Utilites.getJour(mydate.get(Calendar.DAY_OF_WEEK));
    }

    public int getDt_unix() {
        return dt_unix;
    }

    public void setDt_unix(int dt_unix) {
        this.dt_unix = dt_unix;
    }

    public int getAn() {
        return an;
    }

    public void setAn(int an) {
        this.an = an;
    }

    public int getMois() {
        return mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }

    public int getJour() {
        return jour;
    }

    public void setJour(int jour) {
        this.jour = jour;
    }

    public String getDt_text() {
        return dt_text;
    }

    public void setDt_text(String dt_text) {
        this.dt_text = dt_text;
    }

    public String getNomDeAn() {
        return nomDeAn;
    }

    public void setNomDeAn(String nomDeAn) {
        this.nomDeAn = nomDeAn;
    }

    public String getNomDeMois() {
        return nomDeMois;
    }

    public void setNomDeMois(String nomDeMois) {
        this.nomDeMois = nomDeMois;
    }

    public String getNomDeJours() {
        return nomDeJours;
    }

    public void setNomDeJours(String nomDeJours) {
        this.nomDeJours = nomDeJours;
    }
}
