package com.ridbparis8.meteo;

import java.util.ArrayList;

public class Climat {
    ArrayList<Temps> tempsArray;
    ArrayList<ClimatInfo> climatInfoArray;
    Location location;

    public Climat(ArrayList<Temps> tempsArray, ArrayList<ClimatInfo> climatInfoArray) {
        this.tempsArray = tempsArray;
        this.climatInfoArray = climatInfoArray;
    }

    public ArrayList<Temps> getTempsArray() {
        return tempsArray;
    }

    public void setTempsArray(ArrayList<Temps> tempsArray) {
        this.tempsArray = tempsArray;
    }

    public ArrayList<ClimatInfo> getClimatInfoArray() {
        return climatInfoArray;
    }

    public void setClimatInfoArray(ArrayList<ClimatInfo> climatInfoArray) {
        this.climatInfoArray = climatInfoArray;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
