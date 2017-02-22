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
    public String sitioPID;
    public String sitioTipo;



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

    public String getSPID() {
        return sitioPID;
    }

    public String getTipo() {
        return sitioTipo;
    }

    public String getId() { return sitioId; }

    public int getSitioPos(){return sitioPos;}

    public String getRecoId(){return sitioRecoId;}
}

