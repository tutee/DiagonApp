package com.rightdecisions.diagonapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rightdecisions.diagonapp.R;

/**
 * Created by Tute on 1/11/2016.
 */

public class RegisterFragment extends Fragment{

    public RegisterFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_signin, container, false);
        return v;

    }
}
