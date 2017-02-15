package com.rightdecisions.diagonapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rightdecisions.diagonapp.DirectionCallback;
import com.rightdecisions.diagonapp.GoogleDirection;
import com.rightdecisions.diagonapp.R;
import com.rightdecisions.diagonapp.dialogs.SimpleDialog;
import com.rightdecisions.diagonapp.model.Direction;
import com.rightdecisions.diagonapp.util.DirectionConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tute on 02/01/2017.
 */

public class RecorridoExpandidoActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, SimpleDialog.OnSimpleDialogListener, View.OnClickListener, OnMapReadyCallback, DirectionCallback {

    ActionBarDrawerToggle toggle;
    private SharedPreferences preferenceSettingsUnique;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String MY_UNIQUE_PREFERENCE_FILE = "MyUniquePreferenceFile";
    List<DataRecorridoSitio> data = new ArrayList<>();
    private RecyclerView mRVFishPrice;
    private AdapterRecorridoExpandido mAdapter;
    private SimpleDialog pDialog;
    int posicion;
    String estado;



    private Button btnRequestDirection;
    private GoogleMap googleMap;
    private String serverKey = "AIzaSyBdDzM01Fqp8UYpHSlpYAkpfN0-tuNfScw";
    private LatLng camera = new LatLng(-34.930723,-57.940932);
    private LatLng origin;
    private LatLng destination;
    List<LatLng> asd = new ArrayList<>();





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorridoexpand);

        Globales.Globalsitioplaceid = new ArrayList<>();

        preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the LoginActivity Sign-In API and the
        // options specified by gso.
        Globales.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);


        //cargarAdapter();


        mRVFishPrice.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRVFishPrice ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {


                        /*Globales.SENombre = data.get(position).getName();
                        Globales.SEImagen = data.get(position).getImage();
                        Log.e("IMAGEN", Globales.SEImagen);

                        Intent intent = new Intent(SitiosActivity.this,
                                AnimateToolbar.class);
                        startActivity(intent);*/

                    }

                    @Override public void onLongItemClick(View view, int position) {

                        posicion = position;
                        new SimpleDialog().show(getSupportFragmentManager(), "SimpleDialog");



                    }
                })
        );

        cargarPuntos(asd);

        /*btnRequestDirection = (Button) findViewById(R.id.btn_request_direction);
        btnRequestDirection.setOnClickListener(this);*/

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        requestDirection();






    }


    public void cargarAdapter () {

        mRVFishPrice = (RecyclerView) findViewById(R.id.fishPriceList);
        mAdapter = new AdapterRecorridoExpandido(RecorridoExpandidoActivity.this, Globales.Globalsitiosrecoexp);
        mRVFishPrice.setAdapter(mAdapter);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(RecorridoExpandidoActivity.this));
    }




    public void cargarPuntos(List<LatLng> l) {


        for (int i = 0; i < Globales.Globalsitiosrecoexp.size(); i++) {
            if (Globales.Globalsitiosrecoexp.get(i).getCC().equals("cuerpo")) {
                double lat = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLat());
                double lon = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLon());
                LatLng u = new LatLng(lat, lon);
                l.add(u);
            } else if (Globales.Globalsitiosrecoexp.get(i).getCC().equals("cabeza")) {
                double lat = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLat());
                double lon = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLon());
                origin = new LatLng(lat,lon);
            } else  {
                double lat = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLat());
                double lon = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLon());
                destination = new LatLng(lat,lon);
            }
        }

    }

    public void ordenarWaypoints(){

        for (int i = 0; i < Globales.Globalsitiosrecoexp.size(); i++) {

            if (Globales.Globalsitiosrecoexp.get(i).getCC().equals("cuerpo")) {

                for (int j = 0; j < Globales.Globalsitioplaceid.size(); j++) {

                   if ((Globales.Globalsitioplaceid.get(j)).equals(Globales.Globalsitiosrecoexp.get(i).getSPID()))
                    {
                        Globales.Globalsitiosrecoexp.get(i).sitioPos = j + 1;
                    }
                }
            }

            Log.e("ORDEN: ", String.valueOf(Globales.Globalsitiosrecoexp));
        }

        Collections.sort(Globales.Globalsitiosrecoexp, new Comparator<DataRecorridoSitio>() {
            @Override
            public int compare(DataRecorridoSitio s1, DataRecorridoSitio s2) {
                return ((Integer)s1.getPos()).compareTo(s2.getPos());
            }
        });

        Log.e("ORDEN SIIIII: ", String.valueOf(Globales.Globalsitiosrecoexp));
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPossitiveButtonClick(String c) {

        Log.e("TIPO", String.valueOf(c));
        estado = c;


        for (int i = 0; i < Globales.Globalsitiosrecoexp.size(); i++) {

            switch (Globales.Globalsitiosrecoexp.get(posicion).getCC()) {
                case "cuerpo":

                    if (estado.equals(Globales.Globalsitiosrecoexp.get(i).getCC())) {
                        Globales.Globalsitiosrecoexp.get(i).sitioCC = "cuerpo";
                        Globales.Globalsitiosrecoexp.get(i).sitioPos = Globales.Globalsitiosrecoexp.get(posicion).getPos();
                    }
                    break;
                case "cabeza":

                    if (estado.equals(Globales.Globalsitiosrecoexp.get(i).getCC())) {
                        Globales.Globalsitiosrecoexp.get(i).sitioCC = "cabeza";
                        Globales.Globalsitiosrecoexp.get(i).sitioPos = Globales.Globalsitiosrecoexp.get(posicion).getPos();
                    }
                    break;
                case "cola":

                    if (estado.equals(Globales.Globalsitiosrecoexp.get(i).getCC())) {
                        Globales.Globalsitiosrecoexp.get(i).sitioCC = "cola";
                        Globales.Globalsitiosrecoexp.get(i).sitioPos = Globales.Globalsitiosrecoexp.get(posicion).getPos();
                    }
                    break;
                default:
            }
        }

        Globales.Globalsitiosrecoexp.get(posicion).sitioCC = estado;
        if (estado.equals("cabeza")){
            Globales.Globalsitiosrecoexp.get(posicion).sitioPos = 0;
        } else {
            Globales.Globalsitiosrecoexp.get(posicion).sitioPos = Globales.Globalsitiosrecoexp.size()-1;
        }

        Collections.sort(Globales.Globalsitiosrecoexp, new Comparator<DataRecorridoSitio>() {
            @Override
            public int compare(DataRecorridoSitio s1, DataRecorridoSitio s2) {
                return ((Integer)s1.getPos()).compareTo(s2.getPos());
            }
        });


        cargarAdapter();
        asd = new ArrayList<>();
        Globales.Globalsitioplaceid = new ArrayList<>();
        googleMap.clear();
        cargarPuntos(asd);
        requestDirection();

    }

    @Override
    public void onNegativeButtonClick() {

        // Acciones
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, 13));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_request_direction) {
            requestDirection();
        }
    }

    public void requestDirection() {
        //Snackbar.make(btnRequestDirection, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .waypoints(asd)
                .transportMode(TransportMode.DRIVING)
                .optimizeWaypoints(true)
                .execute(this);

    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        //Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();

        Log.e("ERROR", String.valueOf(direction));

        if (direction.isOK()) {
            googleMap.addMarker(new MarkerOptions().position(origin)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            for (int i = 0; i < asd.size(); i++) {
                googleMap.addMarker(new MarkerOptions().position(asd.get(i))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }

            googleMap.addMarker(new MarkerOptions().position(destination)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            ArrayList<LatLng> directionPositionList = null;


            for (int i = 0; i < direction.getRouteList().size(); i++) {
                for (int j = 0; j < direction.getRouteList().get(i).getLegList().size(); j++) {

                    directionPositionList = direction.getRouteList().get(i).getLegList().get(j).getDirectionPoint();
                    googleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
                    if (j != 0) {
                        Globales.Globalsitioplaceid.add(String.valueOf(direction.getGeocodedWaypointList().get(j).getPlaceId()));
                    }
                    Log.e("WAYPOINTS GEOCODE!", String.valueOf(direction.getGeocodedWaypointList().get(j).getPlaceId()));
                }
                Log.e("WAYPOINTS!", String.valueOf(direction.getGeocodedWaypointList().get(direction.getRouteList().size()).getPlaceId()));

                Log.e("LISTA!", String.valueOf(Globales.Globalsitioplaceid));

                //ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                //googleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
            }


            //Log.e("WAYPOINTS!", String.valueOf(googleMap.);

            ordenarWaypoints();

            cargarAdapter();

           // btnRequestDirection.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(btnRequestDirection, t.getMessage(), Snackbar.LENGTH_SHORT).show();
    }
}
