package com.rightdecisions.diagonapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rightdecisions.diagonapp.R;
import com.rightdecisions.diagonapp.fragments.LoginFragment;
import com.rightdecisions.diagonapp.fragments.RegisterFragment;

/**
 * Created by Tute on 1/11/2016.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*if (savedInstanceState == null) {
            LoginFragment fragment = new LoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment, "LoginFragment")
                    .commit();
        }*/

        Button b2 = (Button) findViewById(R.id.register_button);

        final Fragment fg = new RegisterFragment();

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.loginlayout, fg);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


    }
}
