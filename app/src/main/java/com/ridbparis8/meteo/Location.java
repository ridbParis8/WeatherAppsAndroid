package com.ridbparis8.meteo;

public class Location {

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
