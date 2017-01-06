package com.rightdecisions.diagonapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.IntentCompat;
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
import android.content.SharedPreferences;
import android.content.Context;

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
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String MY_UNIQUE_PREFERENCE_FILE = "MyUniquePreferenceFile";
    private SharedPreferences preferenceSettingsUnique;
    private SharedPreferences.Editor preferenceEditorUnique;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        editmail = (EditText) findViewById(R.id.email_input);
        editpassword = (EditText) findViewById(R.id.contraseña_input);

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
                        /*Globales.Globalemail = jObjU.getString("email");
                        Globales.Globalid = jObjU.getString("id");
                        Globales.Globalapellido = jObjU.getString("apellido");
                        Globales.Globalnombre = jObjU.getString("nombres");
                        Globales.Globalimage = jObjU.getString("imagen");*/
                        preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);
                        preferenceEditorUnique = preferenceSettingsUnique.edit();
                        preferenceEditorUnique.putString("ID", jObjU.getString("id"));
                        preferenceEditorUnique.putString("email", jObjU.getString("email"));
                        preferenceEditorUnique.putString("apellido", jObjU.getString("apellido"));
                        preferenceEditorUnique.putString("nombre", jObjU.getString("nombres"));
                        preferenceEditorUnique.putString("imagen", jObjU.getString("imagen"));

                        boolean successfullSaved = preferenceEditorUnique.commit();
                        if (successfullSaved){
                            preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);
                            String prueba = preferenceSettingsUnique.getString("ID","");
                            Log.e("Shared", prueba);
                        }

                        Intent intent = new Intent(
                                LoginActivity.this,
                                //Sitios.class);
                                SitiosActivity.class);
                        intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK ) ;
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