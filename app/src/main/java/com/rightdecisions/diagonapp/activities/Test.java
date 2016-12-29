package com.rightdecisions.diagonapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rightdecisions.diagonapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.rightdecisions.diagonapp.activities.AppConfig.GEOMETRY;
import static com.rightdecisions.diagonapp.activities.AppConfig.GOOGLE_BROWSER_API_KEY;
import static com.rightdecisions.diagonapp.activities.AppConfig.ICON;
import static com.rightdecisions.diagonapp.activities.AppConfig.LATITUDE;
import static com.rightdecisions.diagonapp.activities.AppConfig.LATITUD_MORENO;
import static com.rightdecisions.diagonapp.activities.AppConfig.LOCATION;
import static com.rightdecisions.diagonapp.activities.AppConfig.LONGITUDE;
import static com.rightdecisions.diagonapp.activities.AppConfig.LONGITUD_MORENO;
import static com.rightdecisions.diagonapp.activities.AppConfig.NAME;
import static com.rightdecisions.diagonapp.activities.AppConfig.OK;
import static com.rightdecisions.diagonapp.activities.AppConfig.PLACE_ID;
import static com.rightdecisions.diagonapp.activities.AppConfig.PROXIMITY_RADIUS;
import static com.rightdecisions.diagonapp.activities.AppConfig.REFERENCE;
import static com.rightdecisions.diagonapp.activities.AppConfig.SITIO_ID;
import static com.rightdecisions.diagonapp.activities.AppConfig.STATUS;
import static com.rightdecisions.diagonapp.activities.AppConfig.TAG;
import static com.rightdecisions.diagonapp.activities.AppConfig.VICINITY;
import static com.rightdecisions.diagonapp.activities.AppConfig.ZERO_RESULTS;

/**
 * Created by Tute on 29/12/2016.
 */

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        loadNearByPlaces(LATITUD_MORENO,LONGITUD_MORENO);

    }

    private void loadNearByPlaces(double latitude, double longitude) {
//YOU Can change this type at your own will, e.g hospital, cafe, restaurant.... and see how it all works
        String type = "";
        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=").append(type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_BROWSER_API_KEY);

        JsonObjectRequest request = new JsonObjectRequest(googlePlacesUrl.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {

                        Log.i(TAG, "onResponse: Result= " + result.toString());
                        parseLocationResult(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: Error= " + error);
                        Log.e(TAG, "onErrorResponse: Error= " + error.getMessage());
                    }
                });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void parseLocationResult(JSONObject result) {

        String id, place_id, placeName = null, reference, icon, vicinity = null;
        double latitude, longitude;

        try {
            JSONArray jsonArray = result.getJSONArray("results");
            Log.e("error1", jsonArray.toString());
            Log.e("error1.5", result.toString());

            if (result.getString(STATUS).equalsIgnoreCase(OK)) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject place = jsonArray.getJSONObject(i);

                    id = place.getString(SITIO_ID);
                    place_id = place.getString(PLACE_ID);
                    if (!place.isNull(NAME)) {
                        placeName = place.getString(NAME);
                    }
                    if (!place.isNull(VICINITY)) {
                        vicinity = place.getString(VICINITY);
                    }
                    latitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LATITUDE);
                    longitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LONGITUDE);
                    reference = place.getString(REFERENCE);
                    icon = place.getString(ICON);
                }

                Toast.makeText(getBaseContext(), jsonArray.length() + " Supermarkets found!",
                        Toast.LENGTH_LONG).show();
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                Toast.makeText(getBaseContext(), "No Supermarket found in 5KM radius!!!",
                        Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {

            e.printStackTrace();
            Log.e(TAG, "parseLocationResult: Error=" + e.getMessage());
        }
    }
}
