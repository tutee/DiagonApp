package com.rightdecisions.diagonapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.rightdecisions.diagonapp.R;
import com.rightdecisions.diagonapp.fragments.RegisterFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity to demonstrate basic retrieval of the LoginActivity user's ID, email address, and basic
 * profile.
 */
public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private EditText editmail, editnombre, editapellido, editpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Views
        // mStatusTextView = (TextView) findViewById(R.id.status);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        editmail = (EditText) findViewById(R.id.email_input);
        editnombre = (EditText) findViewById(R.id.nombre_input);
        editapellido = (EditText) findViewById(R.id.apellido_input);
        editpassword = (EditText) findViewById(R.id.contraseña_input);

        findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usuemail = editmail.getText().toString();
                String usunombre = editnombre.getText().toString();
                String usuapellido = editapellido.getText().toString();
                String usupassword = editpassword.getText().toString();

                //Chequeo que ningun campo este vacio
                if (usuemail.matches("") || usunombre.matches("") || usuapellido.matches("") || usupassword.matches("")) {

                    Snackbar.make(view, "Complete todos los campos", Snackbar.LENGTH_LONG)
                            .show();

                } else {

                    registerUser(usuemail, usunombre, usuapellido, usupassword, Globales.ImagenPD);

                }
            }
        });
    }


    public void registerUser(final String usuemail, final String usunombre, final String usuapellido,final String usupassword, final String usuimage ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        Log.e("ERROR","ERROR1");
        pDialog.setMessage("Registrando cuenta...");
        showDialog();
        Log.e("ERROR","ERROR6");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Globales.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                //Log.e("ERROR envPass", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "registerusu");
                params.put("usu_email", usuemail);
                params.put("usu_nombres", usunombre);
                params.put("usu_apellido", usuapellido);
                params.put("usu_password", usupassword);
                params.put("usu_imagen", usuimage);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }


}