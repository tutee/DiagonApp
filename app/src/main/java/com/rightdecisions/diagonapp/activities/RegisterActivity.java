package com.rightdecisions.diagonapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rightdecisions.diagonapp.R;
import com.rightdecisions.diagonapp.fragments.RegisterFragment;

/**
 * Created by Tute on 1/11/2016.
 */

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dialog_signin);

        if (savedInstanceState == null) {
            RegisterFragment fragment = new RegisterFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment, "LoginFragment")
                    .commit();
        }
    }
}
