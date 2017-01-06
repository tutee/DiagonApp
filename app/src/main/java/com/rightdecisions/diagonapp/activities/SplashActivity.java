package com.rightdecisions.diagonapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rightdecisions.diagonapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Timer().schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(
                                    SplashActivity.this,
                                    MainActivity.class);;
                            startActivity(intent);
                            finish();
                        }
                    },
                    2000
            );

    }
}

