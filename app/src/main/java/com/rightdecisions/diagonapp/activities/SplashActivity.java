package com.rightdecisions.diagonapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rightdecisions.diagonapp.R;

public class SplashActivity extends AppCompatActivity {

    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String MY_UNIQUE_PREFERENCE_FILE = "MyUniquePreferenceFile";
    private SharedPreferences preferenceSettingsUnique;
    private SharedPreferences.Editor preferenceEditorUnique;
    boolean prueba;
    boolean firstTime;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);
        Log.e("BOOL", String.valueOf(firstTime));
        if (preferenceSettingsUnique.contains("first_time") && !(preferenceSettingsUnique.getBoolean("first_time", false) ) ){
            firstTime = false;
        } else {
            firstTime = true;
        }
        Log.e("BOOL", String.valueOf(firstTime));
        //boolean firstTime = preferenceSettingsUnique.contains("first_time");
        prueba = preferenceSettingsUnique.contains("ID");
        Log.e("BOOL PRUEBA ", String.valueOf(prueba));
        //Log.e("Sharedreg", prueba);
        //final boolean successfullSaved1 = preferenceEditorUnique.commit();

       /* New Handler to start the Menu-Activity
        * and close this Splash-Screen after some seconds.*/
            /* Duration of wait */
            int SPLASH_DISPLAY_LENGTH = 5000;
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    if (firstTime) {
                        Log.e("BOOL", String.valueOf(firstTime));
                        preferenceEditorUnique = preferenceSettingsUnique.edit();
                        preferenceEditorUnique.putBoolean("first_time", false);
                        preferenceEditorUnique.commit();
                        setContentView(R.layout.splashscreen);
               /* Create an Intent that will start the Menu-Activity. */


                        //if (successfullSaved1){
                        if (!prueba) {
                            Intent intent = new Intent(
                                    SplashActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(
                                    SplashActivity.this,
                                    SitiosActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        //}
                    } else if (!prueba){
                        Intent intent = new Intent(
                                SplashActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(
                                SplashActivity.this,
                                SitiosActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }

            }, SPLASH_DISPLAY_LENGTH);
        }
    }

