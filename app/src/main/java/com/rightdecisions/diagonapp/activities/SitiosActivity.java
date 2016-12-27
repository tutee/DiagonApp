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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

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

/**
 * Created by Tute on 9/11/2016.
 */

public class SitiosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    ActionBarDrawerToggle toggle;
    Button bu=null;
    Button bu2=null;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String MY_UNIQUE_PREFERENCE_FILE = "MyUniquePreferenceFile";
    private SharedPreferences preferenceSettingsUnique;
    private SharedPreferences.Editor preferenceEditorUnique;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios);

        bu=(Button)findViewById(R.id.button2);
        bu2=(Button)findViewById(R.id.button3);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //VER ESTO PARA SOLUCIONAR EL HAMBURGER


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);

        preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);

        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Log.e("ERROR", String.valueOf(preferenceSettingsUnique));

                    if (!preferenceSettingsUnique.contains("ID")) {
                        signOut();
                    } else {
                        logout();
                    }
            }
        });

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

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_navu_1:
                break;

            case R.id.menu_navu_2:
                Intent intent = new Intent(SitiosActivity.this,
                        SitiosActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (dl.isDrawerOpen(GravityCompat.START))
            dl.closeDrawer(GravityCompat.START);

        return false;
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

    public void close(View view){
        preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);
        preferenceEditorUnique = preferenceSettingsUnique.edit();
        String prueba = preferenceSettingsUnique.getString("ID","");
        Log.e("Shared", prueba);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
