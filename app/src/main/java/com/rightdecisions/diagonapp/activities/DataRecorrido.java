package com.rightdecisions.diagonapp.activities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DataRecorrido {

    public String itiName;
    public String itiId;
    public ArrayList<String> arraySitInt;


    public String getItiId() {
        return itiId;
    }

    public String getName() {
        return itiName;
    }

    public ArrayList<String> getArraySitios() {

        return arraySitInt;
    }

}