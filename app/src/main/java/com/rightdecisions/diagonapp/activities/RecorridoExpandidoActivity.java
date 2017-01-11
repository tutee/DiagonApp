package com.rightdecisions.diagonapp.activities;

import android.app.ProgressDialog;
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
import com.rightdecisions.diagonapp.dialogs.SimpleDialog;

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

public class RecorridoExpandidoActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, SimpleDialog.OnSimpleDialogListener {

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorridoexpand);


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

        cargarAdapter();



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




    }


    public void cargarAdapter () {

        mRVFishPrice = (RecyclerView) findViewById(R.id.fishPriceList);
        mAdapter = new AdapterRecorridoExpandido(RecorridoExpandidoActivity.this, Globales.Globalsitiosrecoexp);
        mRVFishPrice.setAdapter(mAdapter);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(RecorridoExpandidoActivity.this));
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

    }

    @Override
    public void onNegativeButtonClick() {

        // Acciones
    }

}
