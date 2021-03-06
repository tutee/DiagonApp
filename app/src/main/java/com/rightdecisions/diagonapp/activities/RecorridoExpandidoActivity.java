package com.rightdecisions.diagonapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.rightdecisions.diagonapp.dialogs.SimpleDialogDelSitioReco;
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

public class RecorridoExpandidoActivity extends AppCompatActivity implements SimpleDialogDelSitioReco.OnSimpleDialogListener,AdapterRecorridoExpandido.OnItemClickListenerAdapterRecorridoExpandido,GoogleApiClient.OnConnectionFailedListener, SimpleDialog.OnSimpleDialogListener, View.OnClickListener, OnMapReadyCallback, DirectionCallback {

    ActionBarDrawerToggle toggle;
    private SharedPreferences preferenceSettingsUnique;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String MY_UNIQUE_PREFERENCE_FILE = "MyUniquePreferenceFile";
    List<DataRecorridoSitio> data = new ArrayList<>();
    private RecyclerView mRVFishPrice;
    private AdapterRecorridoExpandido mAdapter;
    private SimpleDialog pDialog;
    int posicion, pos;
    private FloatingActionButton addReco;
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

        addReco = (FloatingActionButton)findViewById(R.id.agrecoButton);

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


        /*mRVFishPrice.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRVFishPrice ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {


                        /*Globales.SENombre = data.get(position).getName();
                        Globales.SEImagen = data.get(position).getImage();
                        Log.e("IMAGEN", Globales.SEImagen);

                        Intent intent = new Intent(SitiosActivity.this,
                                AnimateToolbar.class);
                        startActivity(intent);*/

                   /* }

                    @Override public void onLongItemClick(View view, int position) {

                        posicion = position;
                        new SimpleDialog().show(getSupportFragmentManager(), "SimpleDialog");



                    }
                })
        );*/

        cargarPuntos(asd);

        /*btnRequestDirection = (Button) findViewById(R.id.btn_request_direction);
        btnRequestDirection.setOnClickListener(this);*/

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        requestDirection();


        addReco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent a Google Maps
                String request;

                if (Globales.Globalsitiosrecoexp.size() == 1) {
                Intent intent;
                intent = new Intent (android.content.Intent.ACTION_VIEW);
                //intent.setData(Uri.parse("https://maps.google.com/maps?daddr=-34.92012300,-57.95319200+to:-34.92282880,-57.95625550"));
                //intent.setData(Uri.parse("https://maps.google.com/maps?daddr=Cordoba,Argentina+to:Tortuguitas"));
                    intent.setData(Uri.parse("https://maps.google.com/maps?daddr="+Double.parseDouble(Globales.Globalsitiosrecoexp.get(0).getLat())+","+Double.parseDouble(Globales.Globalsitiosrecoexp.get(0).getLon())));
                startActivity(intent);
                } else if (Globales.Globalsitiosrecoexp.size() > 1) {

                    Intent intent;
                    intent = new Intent (android.content.Intent.ACTION_VIEW);
                    request = "https://maps.google.com/maps?daddr=";
                    for (int i = 0; i < Globales.Globalsitiosrecoexp.size(); i++) {

                        if(i==0){
                            request = request + Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLat())+","+Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLon());
                        } else {
                            request = request + "+to:" +Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLat())+","+Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLon());
                        }


                    }
                    intent.setData(Uri.parse(request));
                    startActivity(intent);


                }
            }
        });



    }


    public void cargarAdapter () {


        for (int i = 0; i < Globales.Globalsitiosrecoexp.size(); i++) {

            Log.e("NOMBRE DEL SITIO", Globales.Globalsitiosrecoexp.get(i).getName());
            Log.e("POSICION DEL SITIO", String.valueOf(Globales.Globalsitiosrecoexp.get(i).getPos()));

        }


        mRVFishPrice = (RecyclerView) findViewById(R.id.fishPriceList);
        mAdapter = new AdapterRecorridoExpandido(RecorridoExpandidoActivity.this, Globales.Globalsitiosrecoexp);
        mAdapter.setOnItemClickListenerAdapterRecorridoExpandido(RecorridoExpandidoActivity.this);
        mRVFishPrice.setAdapter(mAdapter);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(RecorridoExpandidoActivity.this));
    }




    public void cargarPuntos(List<LatLng> l) {


        for (int i = 0; i < Globales.Globalsitiosrecoexp.size(); i++) {
            if (Globales.Globalsitiosrecoexp.get(i).getTipo().equals("Parada")) {
                double lat = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLat());
                double lon = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLon());
                LatLng u = new LatLng(lat, lon);
                l.add(u);
            } else if (Globales.Globalsitiosrecoexp.get(i).getTipo().equals("Origen")) {
                double lat = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLat());
                double lon = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLon());
                origin = new LatLng(lat,lon);
            } else if (Globales.Globalsitiosrecoexp.get(i).getTipo().equals("Destino")) {
                double lat = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLat());
                double lon = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLon());
                destination = new LatLng(lat,lon);
            } else if (Globales.Globalsitiosrecoexp.get(i).getTipo().equals("Unico")) {
                double lat = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLat());
                double lon = Double.parseDouble(Globales.Globalsitiosrecoexp.get(i).getLon());
                origin = new LatLng(lat, lon);
                destination = new LatLng(lat, lon);
            }
        }

    }


    //Ordena las PARADAS segun el orden optimo que de el Map
    public void ordenarWaypoints(){

        Log.e("LISTA PLACES ID!", String.valueOf(Globales.Globalsitioplaceid));

        for (int h = 0; h < Globales.Globalsitiosrecoexp.size(); h++) {

            Log.e("LISTA SITIOS RECO EXP!", String.valueOf(Globales.Globalsitiosrecoexp.get(h).getName()));
            Log.e("LISTA SITIOS RECO EXP!", String.valueOf(Globales.Globalsitiosrecoexp.get(h).getSPID()));

        }



        for (int i = 0; i < Globales.Globalsitiosrecoexp.size(); i++) {

            if (Globales.Globalsitiosrecoexp.get(i).getTipo().equals("Parada")) {

                for (int j = 0; j < Globales.Globalsitioplaceid.size(); j++) {

                   if ((Globales.Globalsitioplaceid.get(j)).equals(Globales.Globalsitiosrecoexp.get(i).getSPID()))
                    {
                        Globales.Globalsitiosrecoexp.get(i).sitioPos = j + 1;
                    }
                }
            }

            Log.e("ORDEN: ", String.valueOf(Globales.Globalsitiosrecoexp));
        }

        if (Globales.Globalsitiosrecoexp.size() > 1 ) {

            Collections.sort(Globales.Globalsitiosrecoexp, new Comparator<DataRecorridoSitio>() {
                @Override
                public int compare(DataRecorridoSitio s1, DataRecorridoSitio s2) {
                    return ((Integer)s1.getPos()).compareTo(s2.getPos());
                }
            });

        }


        Log.e("ORDEN SIIIII: ", String.valueOf(Globales.Globalsitiosrecoexp));
    }

    public void cargarRecorridos(final String usuid ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        //pDialog.setMessage("Registrando cuenta...");
        Log.e("ERROR", "ERROR1");
        //showDialog();
        Log.e("ERROR", "ERROR6");

        Log.e("ERROR", String.valueOf((preferenceSettingsUnique.getString("ID",""))));



        StringRequest strReq = new StringRequest(Request.Method.POST,
                Globales.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //hideDialog();
                //Log.e("ERROR envPass", response);
                try {


                    Log.e("ERROR", response);

                    JSONObject obj = new JSONObject(response);


                    if (!obj.getBoolean("error")) {

                        JSONArray arrayP = obj.getJSONArray("itinerarios");
                        data.clear();

                        // Extract data from json and store into ArrayList as class objects
                        for (int i = 0; i < arrayP.length(); i++) {
                            JSONObject json_data = arrayP.getJSONObject(i);

                            if (json_data.getString("iti_id").equals(Globales.Globalidrecoexp)){

                                JSONArray sitios = json_data.optJSONArray("sitios_interes");
                                Log.e("ERROR", String.valueOf(sitios));

                                if (sitios != null) {



                                    Log.e("ERROR", String.valueOf(sitios.length()));

                                    for (int j = 0; j < sitios.length(); j++) {

                                        DataRecorridoSitio sitioData = new DataRecorridoSitio();
                                        JSONObject json_sit = sitios.getJSONObject(j);
                                        Log.e("JSON OBJECT", String.valueOf(json_sit));
                                        sitioData.sitioId = json_sit.getString("sitint_sit_id");
                                        sitioData.sitioName = json_sit.getString("sit_titulo");
                                        Log.e("UN SITIO", sitioData.sitioName);
                                        sitioData.sitioRecoId = json_sit.getString("sitint_iti_id");
                                        sitioData.sitioLat = json_sit.getString("sit_lat");
                                        sitioData.sitioLon = json_sit.getString("sit_lon");
                                        sitioData.sitioPos = j;
                                        Log.e("POSICION DEL SITIO", String.valueOf(sitioData.sitioPos));
                                        sitioData.sitioPID = json_sit.getString("sit_place_id");
                                        sitioData.sitioTipo = json_sit.getString("sitint_tipo");
                                        Log.e("TIPO DE SITIO", String.valueOf(sitioData.sitioTipo));
                                        if (j == 0) {
                                            sitioData.sitioCC = "cabeza";
                                        } else if (j == sitios.length() - 1) {
                                            sitioData.sitioCC = "cola";
                                        } else {
                                            sitioData.sitioCC = "cuerpo";
                                        }


                                        Globales.Globalsitiosrecoexp.add(sitioData);

                                    }

                                }

                            }

                        }

                        Log.e("SITIOS DESPUES", String.valueOf(Globales.Globalsitiosrecoexp));

                        cargarPuntos(asd);
                        requestDirection();
                        ordenarWaypoints();
                        cargarAdapter();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "Itinerarios_sitios");
                params.put("iti_usu_id", usuid);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void cambiarTipo (final String sitid, final String recoid, final String sittipo ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        Log.e("ERROR", "ERROR6");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Globales.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //hideDialog();
                //Log.e("ERROR envPass", response);
                try {


                    Log.e("ERROR", response);
                    JSONObject obj = new JSONObject(response);


                    if (!obj.getBoolean("error")) {
                        Log.e("ERROR", "FUNCIONA");
                        Toast.makeText(getApplicationContext(),
                                "El cambio fue realizado con exito", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.e("ERROR", "NO FUNCIONA");
                        /*Toast.makeText(getApplicationContext(),
                                "El nombre "+namereco+" ya existe", Toast.LENGTH_LONG).show();*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "sitioCambioTipo");
                params.put("iti_usu_id", (preferenceSettingsUnique.getString("ID","")));
                params.put("sitint_sit_id", sitid);
                params.put("iti_id", recoid);
                params.put("sitint_tipo", sittipo);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPossitiveButtonClick(String c) {

        int i = 0;
        Log.e("SITIO ID", String.valueOf(Globales.Globalsitiosrecoexp.get(posicion).getId()));
        Log.e("SITIO RECO ID", String.valueOf(Globales.Globalsitiosrecoexp.get(posicion).getRecoId()));
        Log.e("SITIO TIPO nuevo", String.valueOf(c));


        cambiarTipo(Globales.Globalsitiosrecoexp.get(posicion).getId(), Globales.Globalsitiosrecoexp.get(posicion).getRecoId(), String.valueOf(c));
        Globales.Globalsitiosrecoexp.get(posicion).sitioTipo = String.valueOf(c);

        Log.e("SITIO TIPO nuevo", Globales.Globalsitiosrecoexp.get(posicion).getTipo());

        asd = new ArrayList<>();
        googleMap.clear();
        Globales.Globalsitiosrecoexp = new ArrayList<>();
        Log.e("SITIOS ANTES", String.valueOf(Globales.Globalsitiosrecoexp));
        Log.e("SITIOS ANTES", String.valueOf(Globales.Globalsitiosrecoexp.size()));


        cargarRecorridos(preferenceSettingsUnique.getString("ID", ""));










        //NO ESTA EN FUNCIONAMIENTO, HAY QUE REALIZAR LAS TAGS EN EL INDEX
        /*
        Log.e("TIPO", String.valueOf(c));
        estado = c;


        for (int i = 0; i < Globales.Globalsitiosrecoexp.size(); i++) {

            switch (Globales.Globalsitiosrecoexp.get(posicion).getTipo()) {
                case "Parada":

                    if (estado.equals(Globales.Globalsitiosrecoexp.get(i).getTipo())) {
                        Globales.Globalsitiosrecoexp.get(i).sitioTipo = "Parada";
                        Globales.Globalsitiosrecoexp.get(i).sitioPos = Globales.Globalsitiosrecoexp.get(posicion).getPos();
                    }
                    break;
                case "Origen":

                    if (estado.equals(Globales.Globalsitiosrecoexp.get(i).getTipo())) {
                        Globales.Globalsitiosrecoexp.get(i).sitioTipo = "Origen";
                        Globales.Globalsitiosrecoexp.get(i).sitioPos = Globales.Globalsitiosrecoexp.get(posicion).getPos();
                    }
                    break;
                case "Destino":

                    if (estado.equals(Globales.Globalsitiosrecoexp.get(i).getTipo())) {
                        Globales.Globalsitiosrecoexp.get(i).sitioTipo= "Destino";
                        Globales.Globalsitiosrecoexp.get(i).sitioPos = Globales.Globalsitiosrecoexp.get(posicion).getPos();
                    }
                    break;
                default:
            }
        }

        Globales.Globalsitiosrecoexp.get(posicion).sitioTipo = estado;
        if (estado.equals("Origen")){
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
        */
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

    public void eliminarSitioReco (final String sitioid, final String itiid ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        Log.e("ERROR", "ERROR6");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Globales.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //hideDialog();
                //Log.e("ERROR envPass", response);
                try {


                    Log.e("ERROR", response);
                    JSONObject obj = new JSONObject(response);


                    if (!obj.getBoolean("error")) {
                        Log.e("ERROR", "FUNCIONA");
                        Toast.makeText(getApplicationContext(),
                                "El sitio fue borrado con exito", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.e("ERROR", "NO FUNCIONA");
                        /*Toast.makeText(getApplicationContext(),
                                "El nombre "+namereco+" ya existe", Toast.LENGTH_LONG).show();*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "borrarSitioInt");
                params.put("iti_usu_id", (preferenceSettingsUnique.getString("ID","")));
                params.put("sitint_id", sitioid);
                params.put("iti_id", itiid);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void itemListLongClick(View view, int position) {

        posicion = position;
        new SimpleDialog().show(getSupportFragmentManager(), "SimpleDialog");

    }

    @Override
    public void itemListClick(View view, int position) {

        String id = Globales.Globalsitiosrecoexp.get(position).getId();

        for (int i = 0; i < Globales.Globalsitios.size(); i++) {

            if (id.equals(Globales.Globalsitios.get(i).getId())){

                Globales.SENombre = Globales.Globalsitios.get(i).getName();
                Globales.SEImagen = Globales.Globalsitios.get(i).getImage();
                Globales.SEDescripcion = Globales.Globalsitios.get(i).getDescripcion();
                Globales.SETel = Globales.Globalsitios.get(i).getTelefono();

            }
        }


        Intent intent = new Intent(RecorridoExpandidoActivity.this,
                AnimateToolbar.class);
        startActivity(intent);

    }

    @Override
    public void checkItemClick(View view, int position) {
        Log.e("ESTA TOCANDO", Globales.Globalsitiosrecoexp.get(position).getId());
    }

    @Override
    public void deleteItemClick(View view, int position) {
        Log.e("IMAGEN", Globales.Globalsitiosrecoexp.get(position).getId());
        Log.e("IMAGEN", Globales.Globalsitiosrecoexp.get(position).getRecoId());


        pos = position;
        new SimpleDialogDelSitioReco().show(getSupportFragmentManager(),"SimpleDialog");
        //NOSE SI ESTOS VAN, CUANDO CAMBIEMOS LO DE TIPO VAMOS A TENER QUE SACARLOS
        //ordenarWaypoints();
        //cargarAdapter();


    }

    @Override
    public void onPossitiveButtonClickDelSitReco() {
        eliminarSitioReco(Globales.Globalsitiosrecoexp.get(pos).getId(),Globales.Globalsitiosrecoexp.get(pos).getRecoId());
        Globales.Globalsitiosrecoexp.remove(pos);
        asd = new ArrayList<>();
        googleMap.clear();
        cargarPuntos(asd);
        requestDirection();
        ordenarWaypoints();
        cargarAdapter();
    }

    @Override
    public void onNegativeButtonClickDelSitReco() {

    }
}
