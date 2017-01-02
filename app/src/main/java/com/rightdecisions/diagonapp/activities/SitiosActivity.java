package com.rightdecisions.diagonapp.activities;

import android.content.Intent;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

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
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tute on 9/11/2016.
 */

public class SitiosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    ActionBarDrawerToggle toggle;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String MY_UNIQUE_PREFERENCE_FILE = "MyUniquePreferenceFile";
    private SharedPreferences preferenceSettingsUnique;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private AdapterFish mAdapter;
    List<DataSitio> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios);

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

        cargarSitios(id);


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_navu_1:
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
                SitiosActivity.this,
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
                                SitiosActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    /*public void close(View view){
        preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);
        preferenceEditorUnique = preferenceSettingsUnique.edit();
        String prueba = preferenceSettingsUnique.getString("ID","");
        Log.e("Shared", prueba);
        finish();
    }*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void cargarSitios(final String usuemailo ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        Log.e("ERROR", "ERROR1");
        //pDialog.setMessage("Registrando cuenta...");
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

                    JSONArray arrayP = obj.getJSONArray("sitios_dest");

                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < arrayP.length(); i++) {
                        JSONObject json_data = arrayP.getJSONObject(i);
                        DataSitio fishData = new DataSitio();
                        /*img = json_data.getString("sit_img");
                        Log.e("ERROR", String.valueOf(img));
                        //rearmo la imagen decodeandolo
                        Bitmap myBitmapAgain = decodeBase64(img);
                        Log.e("ERROR", String.valueOf(myBitmapAgain));*/

                        fishData.sitioImage = json_data.getString("sit_img");
                        fishData.sitioName= json_data.getString("sit_titulo");
                        //fishData.catName = json_data.getString("sit_lat");
                        //fishData.sizeName = json_data.getString("sit_lon");
                        //fishData.price = json_data.getInt("sit_direccion");
                        data.add(fishData);
                    }

                    // Setup and Handover data to recyclerview
                    mRVFishPrice = (RecyclerView) findViewById(R.id.fishPriceList);
                    mAdapter = new AdapterFish(SitiosActivity.this, data);
                    mRVFishPrice.setAdapter(mAdapter);
                    mRVFishPrice.setLayoutManager(new LinearLayoutManager(SitiosActivity.this));


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
                params.put("tag", "listsitiosdestacados");
                params.put("usu_id", usuemailo);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }








}
