package com.rightdecisions.diagonapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.rightdecisions.diagonapp.R;
import com.rightdecisions.diagonapp.dialogs.NoSitiosDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tute on 02/01/2017.
 */

public class MisRecorridosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,NoSitiosDialog.OnSimpleDialogListener, GoogleApiClient.OnConnectionFailedListener {

    ActionBarDrawerToggle toggle;
    private SharedPreferences preferenceSettingsUnique;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String MY_UNIQUE_PREFERENCE_FILE = "MyUniquePreferenceFile";
    List<DataRecorrido> data = new ArrayList<>();
    List<DataRecorridoSitio> datasitio = new ArrayList<>();
    private RecyclerView mRVFishPrice;
    private AdapterRecorrido mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorridos);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);

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

        String id = "10";

        cargarRecorridos(id);

        //mRVFishPrice.setOnClickListener();
        mRVFishPrice.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRVFishPrice ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        //Globales.Globalidrecoexp = data.get(position).getItiId();
                        Globales.Globalsitiosrecoexp = new ArrayList<>();
                        Globales.Globalnombrecoexp = data.get(position).getName();

                        Log.e("ERROR", String.valueOf(datasitio.size()));

                        for (int i = 0; i < datasitio.size(); i++) {

                            Log.e("ERROR", data.get(position).getItiId());
                            Log.e("ERROR", datasitio.get(i).sitioRecoId);

                            if(data.get(position).getItiId().equals(datasitio.get(i).sitioRecoId) ) {

                                Log.e("ERROR", String.valueOf(datasitio.get(i)));
                                Log.e("ERROR", String.valueOf(datasitio.get(i).getName()));
                                Globales.Globalsitiosrecoexp.add(datasitio.get(i));

                            }
                        }



                        if (Globales.Globalsitiosrecoexp.size() != 0){

                            Intent intent = new Intent(MisRecorridosActivity.this,
                                    RecorridoExpandidoActivity.class);
                            startActivity(intent);

                        } else {
                            new NoSitiosDialog().show(getSupportFragmentManager(), "SimpleDialog");
                        }



                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_navu_1:
                Intent intent = new Intent(
                        MisRecorridosActivity.this,
                        SitiosActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navu_2:

                break;

            case R.id.menu_navu_5:
                if (!preferenceSettingsUnique.contains("ID")) {
                    signOut();
                } else {
                    logout();
                }
                break;
        }

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (dl.isDrawerOpen(GravityCompat.START))
            dl.closeDrawer(GravityCompat.START);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        /*switch (id) {
            case R.id.action_settings:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void cargarRecorridos(final String usuemailo ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        //pDialog.setMessage("Registrando cuenta...");
        Log.e("ERROR", "ERROR1");
        //showDialog();
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

                    JSONArray arrayP = obj.getJSONArray("itinerarios");


                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < arrayP.length(); i++) {
                        JSONObject json_data = arrayP.getJSONObject(i);
                        DataRecorrido fishData = new DataRecorrido();


                        fishData.arraySitInt = new ArrayList<String>();

                        JSONArray sitios = json_data.optJSONArray("sitios_interes");
                        Log.e("ERROR", String.valueOf(sitios));
                        if (sitios != null){


                        /*img = json_data.getString("sit_img");
                        Log.e("ERROR", String.valueOf(img));
                        //rearmo la imagen decodeandolo
                        Bitmap myBitmapAgain = decodeBase64(img);
                        Log.e("ERROR", String.valueOf(myBitmapAgain));*/

                        //fishData.sitioImage = json_data.getString("sit_img");


                            Log.e("ERROR", String.valueOf(sitios.length()));

                            for (int j = 0; j < sitios.length(); j++){

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
                                sitioData.sitioPID = json_sit.getString("sit_place_id");
                                if (j == 0) {
                                    sitioData.sitioCC = "cabeza";
                                } else if (j == sitios.length()-1){
                                    sitioData.sitioCC = "cola";
                                } else {sitioData.sitioCC = "cuerpo";}


                                datasitio.add(sitioData);
                                Log.e("DATA SITIO POSICION", String.valueOf(datasitio.get(j).getCC()));


                            }

                        }


                        for (int x = 0; x < datasitio.size(); x++){
                            Log.e("FOR JSON", datasitio.get(x).getName());
                        }

                        fishData.itiId = json_data.getString("iti_id");
                        fishData.itiName= json_data.getString("iti_nombre");


                        //fishData.catName = json_data.getString("sit_lat");
                        //fishData.sizeName = json_data.getString("sit_lon");
                        //fishData.price = json_data.getInt("sit_direccion");
                        data.add(fishData);
                    }

                    // Setup and Handover data to recyclerview
                    mRVFishPrice = (RecyclerView) findViewById(R.id.fishPriceList);
                    mAdapter = new AdapterRecorrido(MisRecorridosActivity.this, data);
                    mRVFishPrice.setAdapter(mAdapter);
                    mRVFishPrice.setLayoutManager(new LinearLayoutManager(MisRecorridosActivity.this));


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
                params.put("iti_usu_id", usuemailo);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public  void logout(){
        preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);
        preferenceSettingsUnique.edit().clear().apply();
        Globales.Globalemail = null;
        Globales.Globalid = null;
        Globales.Globalidgoogle = null;
        Globales.Globalimage = null;
        Globales.Globalnombre = null;
        Globales.Globalapellido = null;
        Intent intent = new Intent(
                MisRecorridosActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(Globales.mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                        Globales.Globalemail = null;
                        Globales.Globalid = null;
                        Globales.Globalidgoogle = null;
                        Globales.Globalimage = null;
                        Globales.Globalnombre = null;
                        Globales.Globalapellido = null;

                        Intent intent = new Intent(
                                MisRecorridosActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPossitiveButtonClick() {

    }

}
