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
public class RegisterActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private ProgressDialog pDialog;
    private EditText editmail, editnombre, editapellido, editpassword, editpassword2;

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
        editpassword = (EditText) findViewById(R.id.password_input);
        editpassword2 = (EditText) findViewById(R.id.passwordconfirm_input);




        findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Chequeo que ningun campo este vacio
                if (editmail.length() > 0 && editnombre.length() > 0 && editapellido.length() > 0 && editpassword.length() > 0 && editpassword2.length() > 0 ) {
                    //Checkea que ambas passwords sean iguales
                    String p1 = editpassword.getText().toString();
                    String p2 = editpassword2.getText().toString();

                    if (p1.equals(p2)) {

                        String usuemail = editmail.getText().toString();
                        String usunombre = editnombre.getText().toString();
                        String usuapellido = editapellido.getText().toString();
                        String usupassword = editpassword.getText().toString();

                        registerUser(usuemail,usunombre,usuapellido,usupassword,Globales.ImagenPD);

                    } else {

                        Snackbar.make(view, "La contraseñas no coinciden", Snackbar.LENGTH_LONG)
                                .show();
                    }

                } else {
                    Snackbar.make(view, "Complete todos los campos", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });


        // Button listeners
        findViewById(R.id.logingoogle_button).setOnClickListener(this);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        /*findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainFragment fragment = new MainFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(android.R.id.content, fragment, "LoginDialog")
                        .commit();

            }
        });*/
        //findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the LoginActivity Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when LoginActivity+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        //SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);
        //signInButton.setScopes(gso.getScopeArray());
        // [END customize_button]
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getId()));
            Globales.Globalemail = acct.getEmail();
            Globales.Globalid = acct.getId();
            Globales.Globalimage = acct.getPhotoUrl().toString();
            Globales.Globalnombre = acct.getGivenName();
            Globales.Globalapellido = acct.getFamilyName();

            Log.d("DATA", "EMAIL:" + Globales.Globalemail);
            Log.d("DATA", "ID GOOGLE:" + Globales.Globalid);
            Log.d("DATA", "IMAGEN:" + Globales.Globalimage);
            Log.d("DATA", "NOMBRE:" + Globales.Globalnombre);
            Log.d("DATA", "APELLIDO:" + Globales.Globalapellido);

            updateUI(true);

            //registerUserGoogle(Globales.Globalemail,Globales.Globalid,Globales.Globalimage,Globales.Globalnombre,Globales.Globalapellido);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and LoginActivity APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            //findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);

            //findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logingoogle_button:
                signIn();
                break;
            //case R.id.sign_out_button:
            //signOut();
            //break;
            //case R.id.disconnect_button:
            //revokeAccess();
            //break;
        }
    }


    public void registerUserGoogle (final String usugemail, final String usugid,
                                     final String usugimage, final String usugnombre, final String usugapellido) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        Log.e("ERROR","ERROR1");
        //mProgressDialog.setMessage(getString(R.string.loading));
        //showProgressDialog();
        Log.e("ERROR","ERROR6");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Globales.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //hideProgressDialog();
                Log.e("ERROR","ERROR7");
                try {
                    Log.e("ERROR","ERROR2");
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.e("ERROR","ERROR2.2");
                    if (!error) {
                        Log.e("ERROR","ERROR11");
                        Intent intent = new Intent(
                                RegisterActivity.this,
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
                //hideProgressDialog();
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
                params.put("usu_imagen", usugimage);
                params.put("usu_nombres", usugnombre);
                params.put("usu_apellido", usugapellido);
                Log.e("ERROR","ERROR5");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void registerUser (final String usuemail, final String usunombre, final String usuapellido,final String usupassword, final String usuimage ) {
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