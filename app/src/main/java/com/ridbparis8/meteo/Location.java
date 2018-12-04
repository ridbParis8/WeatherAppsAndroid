package com.ridbparis8.meteo;

import java.io.Serializable;

public class Location implements Serializable { // Pour dire que la date peut etre passé à d'autre activité (pour passer de ClimatAdapteut a DetailActivity avec la méthode putExtra {

    int    id;
    float  lat;
    float  lon;
    String ville;
    String Pays;


    public Location(int id, float lat, float lon, String ville, String pays) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.ville = ville;
        Pays = pays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return Pays;
    }

    public void setPays(String pays) {
        Pays = pays;
    }
}
