package com.rightdecisions.diagonapp.activities;

import com.google.android.gms.maps.model.LatLng;

public class DataRecorridoSitio {

    public String sitioRecoId;
    public String sitioId;
    public String sitioName;
    public String sitioLat;
    public String sitioLon;
    public String sitioCC;
    public int sitioPos;



    public String getName() {
        return sitioName;
    }

    public int getPos() {return sitioPos;}

    public String getCC(){
        return sitioCC;
    }

    public String getLat() {
        return sitioLat;
    }

    public String getLon() {
        return sitioLon;
    }
}
