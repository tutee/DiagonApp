package com.rightdecisions.diagonapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rightdecisions.diagonapp.R;
import com.rightdecisions.diagonapp.dialogs.LoginDialog;

/**
 * Created by Tute on 1/11/2016.
 */

public class LoginFragment extends android.support.v4.app.Fragment {

    private Button b1,b2;

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_login, container, false);

        b1 = (Button) v.findViewById(R.id.signin_button);
        b2 = (Button) v.findViewById(R.id.register_button);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                fm.beginTransaction();
                Fragment fragTwo = new RegisterFragment();
                Bundle arguments = new Bundle();
                arguments.putBoolean("asd",false);
                fragTwo.setArguments(arguments);
                ft.add(android.R.id.content, fragTwo);
                ft.commit();

                //new LoginDialog().show(fragmentManager, "LoginDialog");


            }
        });

        return v;

    }
}

