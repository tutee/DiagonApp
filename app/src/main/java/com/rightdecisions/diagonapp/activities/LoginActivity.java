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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity to demonstrate basic retrieval of the LoginActivity user's ID, email address, and basic
 * profile.
 */
public class LoginActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private EditText editmail, editpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        editmail = (EditText) findViewById(R.id.email_input);
        editpassword = (EditText) findViewById(R.id.contraseña_input);

        // Views
        // mStatusTextView = (TextView) findViewById(R.id.status);

        findViewById(R.id.signin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editmail.length() > 0 && editpassword.length() > 0) {

                    String usuemail = editmail.getText().toString();
                    String usupassword = editpassword.getText().toString();

                    checkLogin(usuemail, usupassword);

                } else {
                    Snackbar.make(view, "Complete los campos antes de ingresar", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });

    }




    private void registerUserGoogle (final String usugemail, final String usugid,
                              final String usugimage, final String usugnombre, final String usugapellido) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        Log.e("ERROR","ERROR1");
       //mProgressDialog.setMessage("Registrando ...");
        showDialog();
        Log.e("ERROR","ERROR6");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Globales.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.e("ERROR","ERROR7");
                try {
                    Log.e("ERROR","ERROR2");
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.e("ERROR","ERROR2.2");
                    if (!error) {
                        Log.e("ERROR","ERROR11");
                        Intent intent = new Intent(
                                LoginActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Log.e("ERROR","ERROR3");
                    } else {
                        Log.e("ERROR","ERROR10");
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        Log.e("ERROR","ERROR8");
                    }
                } catch (JSONException e) {
                    Log.e("ERROR","ERROR11");
                    e.printStackTrace();
                }
                Log.e("ERROR","ERROR9");
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
                Log.e("ERROR","ERROR4");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "register");
                params.put("usu_email", usugemail);
                params.put("usug_gid", usugid);
                params.put("usu_nombres", usugnombre);
                params.put("usu_apellido", usugapellido);
                Log.e("ERROR","ERROR5");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    /* Checkea los datos del login y los envia a la base. Tráe todos los datos del usuario. */
    private void checkLogin(final String correo, final String password) {
        String tag_string_req = "req_login";
        pDialog.setMessage("Iniciando sesión ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Globales.URL, new Response.Listener<String>() {
            /* El response retorna los datos que envio desde el Backend en el Json. */
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.e("ERROR Login", response);
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error){
                        JSONObject jObjU = jObj.getJSONObject("user");
                        Globales.Globalemail = jObjU.getString("email");
                        Globales.Globalid = jObjU.getString("id");
                        Globales.GlobalPassword = jObjU.getString("apellido");
                        Globales.Globalnombre = jObjU.getString("nombres");
                        Globales.Globalimage = jObjU.getString("imagen");

                        Intent intent = new Intent(
                                LoginActivity.this,
                                SitiosActivity.class);
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
                /* Si ocurre un error, me encuentro en esta situación. */
                Toast.makeText(getApplicationContext(),
                        "Error de conexión", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            /* Mapeo los datos que voy a enviar en el request. */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "loginusu");
                params.put("usu_email", correo);
                params.put("usu_password", password);
                return params;
            }

        };

        /* Agrego la request a la cola de requests. */
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