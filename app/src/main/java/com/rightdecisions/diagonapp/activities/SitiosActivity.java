package com.rightdecisions.diagonapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.rightdecisions.diagonapp.R;
import com.rightdecisions.diagonapp.dialogs.SimpleDialogAgSitReco;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Tute on 9/11/2016.
 */

public class SitiosActivity extends AppCompatActivity implements SimpleDialogAgSitReco.OnSimpleDialogListener, AdapterSitio.OnItemClickListenerAdapterSitios, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, SearchView.OnQueryTextListener {

    int p;
    List<DataRecoAgSitios> datar = new ArrayList<>();
    private static final String TAG = "SignInActivity";
    ActionBarDrawerToggle toggle;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String MY_UNIQUE_PREFERENCE_FILE = "MyUniquePreferenceFile";
    private SharedPreferences preferenceSettingsUnique;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private AdapterSitio mAdapter;
    List<DataSitio> data = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //CircleImageView circleImageView = (CircleImageView) findViewById(R.id.profile_image);

        preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView emailText = (TextView) headerView.findViewById(R.id.email);
        emailText.setText(preferenceSettingsUnique.getString("email",""));
        TextView nombreText = (TextView) headerView.findViewById(R.id.username);
        nombreText.setText((preferenceSettingsUnique.getString("apellido","")+" "+(preferenceSettingsUnique.getString("nombre",""))));
        CircleImageView circleImageView = (CircleImageView) headerView.findViewById(R.id.profile_image);


        Glide.with(this).load(preferenceSettingsUnique.getString("imagen","")).into(circleImageView);





        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the LoginActivity Sign-In API and the
        // options specified by gso.
        Globales.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);



        String id = "10";

        cargarSitios(id);
        cargarRecorridos(preferenceSettingsUnique.getString("ID",""));
        Globales.Globalrecoagsit = datar;


        /*mRVFishPrice.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRVFishPrice ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {


                        Globales.SENombre = data.get(position).getName();
                        Globales.SEImagen = data.get(position).getImage();
                        Globales.SEDescripcion = data.get(position).getDescripcion();
                        Globales.SETel = data.get(position).getTelefono();
                        Log.e("IMAGEN", Globales.SEImagen);

                        Intent intent = new Intent(SitiosActivity.this,
                                AnimateToolbar.class);
                        startActivity(intent);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/



    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_navu_1:
                break;

            case R.id.menu_navu_2:
                Intent intent = new Intent(
                        SitiosActivity.this,
                        MisRecorridosActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navu_3:
                intent = new Intent(
                        SitiosActivity.this,
                        MapsRecoActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navu_5:
                if (preferenceSettingsUnique.contains("IDG")) {
                    signOut();
                } else {
                    logout();
                }
                break;
        }

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (dl.isDrawerOpen(GravityCompat.START))
            dl.closeDrawer(GravityCompat.START);

        return false;
    }



    public  void logout(){
        preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);
        preferenceSettingsUnique.edit().clear().apply();
        /*Globales.Globalemail = null;
        Globales.Globalid = null;
        Globales.Globalidgoogle = null;
        Globales.Globalimage = null;
        Globales.Globalnombre = null;
        Globales.Globalapellido = null;*/
        Intent intent = new Intent(
                SitiosActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(Globales.mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                        /*Globales.Globalemail = null;
                        Globales.Globalid = null;
                        Globales.Globalidgoogle = null;
                        Globales.Globalimage = null;
                        Globales.Globalnombre = null;
                        Globales.Globalapellido = null;*/
                        preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);
                        preferenceSettingsUnique.edit().clear().apply();

                        Intent intent = new Intent(
                                SitiosActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    /*public void close(View view){
        preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);
        preferenceEditorUnique = preferenceSettingsUnique.edit();
        String prueba = preferenceSettingsUnique.getString("ID","");
        Log.e("Shared", prueba);
        finish();
    }*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void cargarSitios(final String usuemailo ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        Log.e("ERROR", "ERROR1");
        //pDialog.setMessage("Registrando cuenta...");
        //showDialog();
        Log.e("ERROR", "ERROR6");



        StringRequest strReq = new StringRequest(Request.Method.POST,
                Globales.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //hideDialog();
                //Log.e("ERROR envPass", response);
                try {
                    Log.e("ERROR", response);

                    JSONObject obj = new JSONObject(response);

                    JSONArray arrayP = obj.getJSONArray("sitios_dest");

                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < arrayP.length(); i++) {
                        JSONObject json_data = arrayP.getJSONObject(i);
                        DataSitio fishData = new DataSitio();
                        /*img = json_data.getString("sit_img");
                        Log.e("ERROR", String.valueOf(img));
                        //rearmo la imagen decodeandolo
                        Bitmap myBitmapAgain = decodeBase64(img);
                        Log.e("ERROR", String.valueOf(myBitmapAgain));*/
                        fishData.sitioId = json_data.getString("sit_id");
                        fishData.sitioImage = json_data.getString("sit_img");
                        fishData.sitioName= json_data.getString("sit_titulo");
                        fishData.sitioDescripcion = json_data.getString("sit_detalle");
                        fishData.sitioTelefono = json_data.getString("sit_telefono");
                        //fishData.catName = json_data.getString("sit_lat");
                        //fishData.sizeName = json_data.getString("sit_lon");
                        //fishData.price = json_data.getInt("sit_direccion");
                        data.add(fishData);
                    }

                    // Setup and Handover data to recyclerview
                    mRVFishPrice = (RecyclerView) findViewById(R.id.fishPriceList);
                    mAdapter = new AdapterSitio(SitiosActivity.this, data);
                    mAdapter.setOnItemClickListenerAdapterSitios(SitiosActivity.this);
                    mRVFishPrice.setAdapter(mAdapter);
                    mRVFishPrice.setLayoutManager(new LinearLayoutManager(SitiosActivity.this));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Error de conexion", Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "listsitiosdestacados");
                params.put("usu_id", usuemailo);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        /*switch (id) {
            case R.id.action_settings:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusearch, menu);

        final MenuItem item = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        mAdapter.setFilter(data);

                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }

    /* public boolean onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menusearch, menu);

        final MenuItem item = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                        mAdapter.setFilter(data);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });

        return true;

    }*/

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<DataSitio> filteredModelList = filter(data, newText);

        mAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<DataSitio> filter(List<DataSitio> models, String query) {
        query = query.toLowerCase();final List<DataSitio> filteredModelList = new ArrayList<>();
        for (DataSitio model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public void cargarRecorridos(final String usuemailo ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        //pDialog.setMessage("Registrando cuenta...");
        Log.e("ERROR", "ERROR1");
        //showDialog();
        Log.e("ERROR", "ERROR6");

        Log.e("ERROR", String.valueOf((preferenceSettingsUnique.getString("ID",""))));



        StringRequest strReq = new StringRequest(Request.Method.POST,
                Globales.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //hideDialog();
                //Log.e("ERROR envPass", response);
                try {


                    Log.e("ERROR", response);

                    JSONObject obj = new JSONObject(response);


                    if (!obj.getBoolean("error")) {

                        JSONArray arrayP = obj.getJSONArray("itinerarios");
                        datar.clear();

                        // Extract data from json and store into ArrayList as class objects
                        for (int i = 0; i < arrayP.length(); i++) {
                            JSONObject json_data = arrayP.getJSONObject(i);
                            DataRecoAgSitios fishData = new DataRecoAgSitios();


                            fishData.arraySitInt = new ArrayList<String>();

                            fishData.itiId = json_data.getString("iti_id");
                            fishData.itiName = json_data.getString("iti_nombre");


                            //fishData.catName = json_data.getString("sit_lat");
                            //fishData.sizeName = json_data.getString("sit_lon");
                            //fishData.price = json_data.getInt("sit_direccion");
                            datar.add(fishData);
                        }




                    } else {
                        Log.e("asdasdasdasd","asdasdasdasdas");

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
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "Itinerarios_sitios");
                params.put("iti_usu_id", usuemailo);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void agSitioIti (final String recoid, final String sitioid) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        Log.e("ERROR", "ERROR6");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Globales.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //hideDialog();
                //Log.e("ERROR envPass", response);
                try {


                    Log.e("ERROR", response);
                    JSONObject obj = new JSONObject(response);


                    if (!obj.getBoolean("error")) {
                        Log.e("ERROR", "FUNCIONA");
                        Toast.makeText(getApplicationContext(),
                                "El sitio fue agregado con exito", Toast.LENGTH_LONG).show();

                    } else {
                        Log.e("ERROR", "NO FUNCIONA");
                        /*Toast.makeText(getApplicationContext(),
                                "El nombre "+namereco+" ya existe", Toast.LENGTH_LONG).show();*/
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
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "agregarSitioIti");
                params.put("iti_usu_id", (preferenceSettingsUnique.getString("ID","")));
                params.put("iti_id", recoid);
                params.put("sit_id", sitioid);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void itemClicked(View view, int position) {

        Globales.SENombre = data.get(position).getName();
        Globales.SEImagen = data.get(position).getImage();
        Globales.SEDescripcion = data.get(position).getDescripcion();
        Globales.SETel = data.get(position).getTelefono();
        Log.e("IMAGEN", Globales.SEImagen);

        Intent intent = new Intent(SitiosActivity.this,
                AnimateToolbar.class);
        startActivity(intent);

    }

    @Override
    public void agSitBoton(View view, int position) {

        //FALTA EL DIALOGO ANTES DEL AGREGAR SITIO

        p = position;

        new SimpleDialogAgSitReco().show(getSupportFragmentManager(), "SimpleDialog");





    }

    @Override
    public void onPossitiveButtonClick(ArrayList<Integer> s) {

        Log.e("IMAGEN", String.valueOf(s));



            for (int j = 0; j<s.size();j++) {

                Log.e("LOS ITEMS SON :", String.valueOf(s));

                Log.e("LOS NOMBRES SON :", Globales.Globalrecoagsit.get(s.get(j)).getName());

                agSitioIti(Globales.Globalrecoagsit.get(s.get(j)).getItiId(), data.get(p).getId());


                /*for (int i = 0; i<Globales.Globalrecoagsit.size(); i++) {



                        Log.e("LOS NOMBRES SON :", Globales.Globalrecoagsit.get(i).getName());

                    }*/
                }
            }

            //items[i] = Globales.Globalrecoagsit.get(i).getName();
            //Log.e("Member name: ", Globales.Globalrecoagsit.get(i).);




    @Override
    public void onNegativeButtonClick() {

    }
}
