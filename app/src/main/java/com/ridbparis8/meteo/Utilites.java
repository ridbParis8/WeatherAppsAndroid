package com.ridbparis8.meteo;

/* Formatteur de date */

public class Utilites {

    static String soleil = "@drawable/soleil";
    static String nuage_petit = "@drawable/clouds_and_sun";
    static String nuage_normal = "@drawable/clouds_and_sun";
    static String nuage_casse = "@drawable/clouds";
    static String pluie_beaucoup = "@drawable/raining";
    static String pluie_normal = "@drawable/raindrops";
    static String eclair = "@drawable/bolt";
    static String neige = "@drawable/snowflake";
    static String brouillard = "@drawable/tornado";

    public static String getMois(int mois){

        String nomDuMois = null;

        switch (mois){
            case 0:
                nomDuMois = "Janvier";
                break;
            case 1:
                nomDuMois = "Février";
                break;
            case 2:
                nomDuMois = "Mars";
                break;
            case 3:
                nomDuMois = "Avril";
                break;
            case 4:
                nomDuMois = "Mai";
                break;
            case 5:
                nomDuMois = "Juin";
                break;
            case 6:
                nomDuMois = "Juillet";
                break;
            case 7:
                nomDuMois = "Aout";
                break;
            case 8:
                nomDuMois = "Septembre";
                break;
            case 9:
                nomDuMois = "Octobre";
                break;
            case 10:
                nomDuMois = "Novembre";
                break;
            case 11:
                nomDuMois = "Décembre";
                break;
        }

        return nomDuMois;
    }

    public static String getJour(int jour){
        /* La documentation JAVA indique que le premier jour de la semaine
           est dimanche et qu'il detient donc la valeur 1. Le lundi sera donc
           le 2 etc etc
         */

        String nomDuJour = null;

        switch (jour){
            case 1:
                nomDuJour = "Dimanche";
                break;
            case 2:
                nomDuJour = "Lundi";
                break;
            case 3:
                nomDuJour = "Mardi";
                break;
            case 4:
                nomDuJour = "Mercredi";
                break;
            case 5:
                nomDuJour = "Jeudi";
                break;
            case 6:
                nomDuJour = "Vendredi";
                break;
            case 7:
                nomDuJour = "Samedi";
                break;
        }

        return nomDuJour;
    }

    public static String getIconUri(int iconId){
        String etatTemp = "@drawable/weather"; // Si on sait pas ce qui ce passe

        if(iconId >= 200 && iconId <= 232){
            etatTemp = eclair;
        }else if(iconId >= 300 && iconId <= 321){
            etatTemp = pluie_beaucoup;
        }else if(iconId >= 500 && iconId <= 504){
            etatTemp = pluie_normal;
        }else if(iconId == 511){
            etatTemp = neige;
        }else if(iconId >= 520 && iconId <= 531){
            etatTemp = pluie_beaucoup;
        }else if(iconId >= 600 && iconId <= 622){
            etatTemp = neige;
        }else if(iconId >= 701 && iconId <= 781){
            etatTemp = brouillard;
        }else if(iconId == 800){
            etatTemp = soleil;
        }else if(iconId == 801){
            etatTemp = nuage_petit;
        }else if(iconId == 802){
            etatTemp = nuage_normal;
        }else if(iconId >= 803 && iconId <= 804){
            etatTemp = nuage_casse;
        }

        return etatTemp;
    }
}
