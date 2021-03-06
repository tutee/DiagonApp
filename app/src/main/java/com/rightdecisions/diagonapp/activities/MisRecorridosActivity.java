package com.rightdecisions.diagonapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.rightdecisions.diagonapp.R;
import com.rightdecisions.diagonapp.dialogs.AgRecoDialog;
import com.rightdecisions.diagonapp.dialogs.NoSitiosDialog;
import com.rightdecisions.diagonapp.dialogs.SimpleDialogAgReco;
import com.rightdecisions.diagonapp.dialogs.SimpleDialogCamNomReco;
import com.rightdecisions.diagonapp.dialogs.SimpleDialogDelReco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Tute on 02/01/2017.
 */

public class MisRecorridosActivity extends AppCompatActivity implements SimpleDialogCamNomReco.OnSimpleDialogListener, SimpleDialogDelReco.OnSimpleDialogListener, AdapterRecorrido.OnItemClickListenerAdapter, NavigationView.OnNavigationItemSelectedListener,SimpleDialogAgReco.OnSimpleDialogListener ,NoSitiosDialog.OnSimpleDialogListener, GoogleApiClient.OnConnectionFailedListener {

    ActionBarDrawerToggle toggle;
    private SharedPreferences preferenceSettingsUnique;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String MY_UNIQUE_PREFERENCE_FILE = "MyUniquePreferenceFile";
    List<DataRecorrido> data = new ArrayList<>();
    List<DataRecorridoSitio> datasitio = new ArrayList<>();
    private RecyclerView mRVFishPrice;
    private AdapterRecorrido mAdapter;
    private ImageView imagen;
    private TextView texto;
    private FloatingActionButton addReco,addReco2;
    private LinearLayout l1;
    private ImageButton btnDelReco;
    private int pos,cant,posmanual;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorridos);

        imagen = (ImageView) findViewById(R.id.imagen1);
        texto = (TextView) findViewById(R.id.txt);
        addReco = (FloatingActionButton)findViewById(R.id.agrecoButton);
        addReco2 = (FloatingActionButton)findViewById(R.id.agrecoButton2);
        l1 = (LinearLayout)findViewById(R.id.linear);

        l1.setVisibility(View.GONE);
        addReco2.setVisibility(View.GONE);
        /*imagen.setVisibility(View.GONE);
        texto.setVisibility(View.GONE);
        addReco.setVisibility(View.GONE);*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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

        btnDelReco = (ImageButton) mRVFishPrice.findViewById(R.id.btnrecodelete);




        cargarRecorridos((preferenceSettingsUnique.getString("ID","")));




        //mRVFishPrice.setOnClickListener();
        /*mRVFishPrice.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRVFishPrice ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {


                        Log.e("ESTOY TOCANDO ESTE ID: ", String.valueOf(view.getId()));


                        //Globales.Globalidrecoexp = data.get(position).getItiId();
                        Globales.Globalsitiosrecoexp = new ArrayList<>();
                        Globales.Globalnombrecoexp = data.get(position).getName();

                        Log.e("ERROR", String.valueOf(datasitio.size()));

                        for (int i = 0; i < datasitio.size(); i++) {

                            Log.e("ERROR", data.get(position).getItiId());
                            Log.e("ERROR", datasitio.get(i).sitioRecoId);

                            if(data.get(position).getItiId().equals(datasitio.get(i).sitioRecoId) ) {

                                Log.e("ERROR", String.valueOf(datasitio.get(i)));
                                Log.e("ERROR", String.valueOf(datasitio.get(i).getName()));
                                Globales.Globalsitiosrecoexp.add(datasitio.get(i));

                            }
                        }



                        if (Globales.Globalsitiosrecoexp.size() != 0){

                            Intent intent = new Intent(MisRecorridosActivity.this,
                                    RecorridoExpandidoActivity.class);
                            startActivity(intent);

                        } else {
                            new NoSitiosDialog().show(getSupportFragmentManager(), "SimpleDialog");
                        }



                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/

        //Boton debajo de la Recycler recorridos
        addReco2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ERROR", "Esta tocando el boton mas");
                new SimpleDialogAgReco().show(getSupportFragmentManager(), "SimpleDialog");
            }
        });

        //Boton debajo del mensaje "no hay recorridos"
        addReco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ERROR", "Esta tocando el boton mas");
                new SimpleDialogAgReco().show(getSupportFragmentManager(), "SimpleDialog");
            }
        });

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_navu_1:
                Intent intent = new Intent(
                        MisRecorridosActivity.this,
                        SitiosActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navu_2:

                break;

            case R.id.menu_navu_3:
                intent = new Intent(
                        MisRecorridosActivity.this,
                        MapsRecoActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navu_5:
                if (!preferenceSettingsUnique.contains("ID")) {
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
                        data.clear();

                        // Extract data from json and store into ArrayList as class objects
                        for (int i = 0; i < arrayP.length(); i++) {
                            JSONObject json_data = arrayP.getJSONObject(i);
                            DataRecorrido fishData = new DataRecorrido();


                            fishData.arraySitInt = new ArrayList<String>();

                            JSONArray sitios = json_data.optJSONArray("sitios_interes");
                            Log.e("ERROR", String.valueOf(sitios));
                            if (sitios != null) {


                        /*img = json_data.getString("sit_img");
                        Log.e("ERROR", String.valueOf(img));
                        //rearmo la imagen decodeandolo
                        Bitmap myBitmapAgain = decodeBase64(img);
                        Log.e("ERROR", String.valueOf(myBitmapAgain));*/

                                //fishData.sitioImage = json_data.getString("sit_img");


                                Log.e("ERROR", String.valueOf(sitios.length()));

                                for (int j = 0; j < sitios.length(); j++) {

                                    DataRecorridoSitio sitioData = new DataRecorridoSitio();
                                    JSONObject json_sit = sitios.getJSONObject(j);
                                    Log.e("JSON OBJECT", String.valueOf(json_sit));
                                    sitioData.sitioId = json_sit.getString("sitint_sit_id");
                                    sitioData.sitioName = json_sit.getString("sit_titulo");
                                    Log.e("UN SITIO", sitioData.sitioName);
                                    sitioData.sitioRecoId = json_sit.getString("sitint_iti_id");
                                    sitioData.sitioLat = json_sit.getString("sit_lat");
                                    sitioData.sitioLon = json_sit.getString("sit_lon");
                                    sitioData.sitioPos = j;
                                    Log.e("POSICION DEL SITIO", String.valueOf(sitioData.sitioPos));
                                    sitioData.sitioPID = json_sit.getString("sit_place_id");
                                    sitioData.sitioTipo = json_sit.getString("sitint_tipo");
                                    Log.e("TIPO DE SITIO", String.valueOf(sitioData.sitioTipo));
                                    if (j == 0) {
                                        sitioData.sitioCC = "cabeza";
                                    } else if (j == sitios.length() - 1) {
                                        sitioData.sitioCC = "cola";
                                    } else {
                                        sitioData.sitioCC = "cuerpo";
                                    }


                                    datasitio.add(sitioData);

                                }

                            }

                            cant = 0;


                            fishData.itiId = json_data.getString("iti_id");
                            fishData.itiName = json_data.getString("iti_nombre");

                            for (int x = 0; x < datasitio.size(); x++) {
                                if (datasitio.get(x).getRecoId().equals(fishData.itiId)){
                                    cant = cant + 1;
                                }
                            }
                            fishData.itiCantSitios = cant;


                            //fishData.catName = json_data.getString("sit_lat");
                            //fishData.sizeName = json_data.getString("sit_lon");
                            //asdasd
                            //fishData.price = json_data.getInt("sit_direccion");
                            data.add(fishData);
                        }


                        // Setup and Handover data to recyclerview
                        mRVFishPrice = (RecyclerView) findViewById(R.id.fishPriceList);
                        mAdapter = new AdapterRecorrido(MisRecorridosActivity.this, data);
                        mAdapter.setOnItemClickListenerAdapter(MisRecorridosActivity.this);
                        mRVFishPrice.setAdapter(mAdapter);
                        mRVFishPrice.setLayoutManager(new LinearLayoutManager(MisRecorridosActivity.this));
                        l1.setVisibility(View.GONE);
                        mRVFishPrice.setVisibility(View.VISIBLE);
                        addReco2.setVisibility(View.VISIBLE);

                    } else {
                        Log.e("asdasdasdasd","asdasdasdasdas");

                        l1.setVisibility(View.VISIBLE);
                        mRVFishPrice.setVisibility(View.GONE);
                        addReco2.setVisibility(View.GONE);
                        /*imagen.setVisibility(View.VISIBLE);
                        texto.setVisibility(View.VISIBLE);
                        addReco.setVisibility(View.VISIBLE);*/
                        //new AgRecoDialog().show(getSupportFragmentManager(), "SimpleDialog");
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

    public void enviarNuevoRecorrido (final String namereco ) {
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
                                "El recorrido "+namereco +" fue cargado con exito", Toast.LENGTH_LONG).show();

                    } else {
                        Log.e("ERROR", "NO FUNCIONA");
                        Toast.makeText(getApplicationContext(),
                                "El nombre "+namereco+" ya existe", Toast.LENGTH_LONG).show();
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
                params.put("tag", "nuevoItinerario");
                params.put("iti_usu_id", (preferenceSettingsUnique.getString("ID","")));
                params.put("iti_nombre", namereco);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void eliminarRecorrido (final String recoid ) {
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
                                "El recorrido fue eliminado con exito", Toast.LENGTH_LONG).show();

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
                params.put("tag", "borrarItinerario");
                params.put("iti_usu_id", (preferenceSettingsUnique.getString("ID","")));
                params.put("iti_id", recoid);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void modinombre (final String recoid, final String reconame ) {
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
                                "El nombre fue cambiado con exito", Toast.LENGTH_LONG).show();

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
                params.put("tag", "nuevoNombreItinerario");
                params.put("iti_usu_id", (preferenceSettingsUnique.getString("ID","")));
                params.put("iti_id", recoid);
                params.put("iti_nombre", reconame);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public  void logout(){
        preferenceSettingsUnique = getSharedPreferences(MY_UNIQUE_PREFERENCE_FILE, PREFERENCE_MODE_PRIVATE);
        preferenceSettingsUnique.edit().clear().apply();
        Globales.Globalemail = null;
        Globales.Globalid = null;
        Globales.Globalidgoogle = null;
        Globales.Globalimage = null;
        Globales.Globalnombre = null;
        Globales.Globalapellido = null;
        Intent intent = new Intent(
                MisRecorridosActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(Globales.mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                        Globales.Globalemail = null;
                        Globales.Globalid = null;
                        Globales.Globalidgoogle = null;
                        Globales.Globalimage = null;
                        Globales.Globalnombre = null;
                        Globales.Globalapellido = null;

                        Intent intent = new Intent(
                                MisRecorridosActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //Dialogo SimpleDialog
    @Override
    public void onPossitiveButtonClick() {

    }


    //Dialogo AgRecoDialog
    @Override
    public void onPossitiveButtonClick(String s) {
        Log.e("asdasdasdasd",s);
        if (s.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "No ingreso el nombre de su nuevo recorrido", Toast.LENGTH_LONG).show();
        } else {
            enviarNuevoRecorrido(s);
            cargarRecorridos(preferenceSettingsUnique.getString("ID", ""));
        }
    }

    @Override
    public void onNegativeButtonClick() {

    }






    @Override
    public void itemClicked(View view, int position) {

        //Globales.Globalidrecoexp = data.get(position).getItiId();
        Globales.Globalidrecoexp = data.get(position).getItiId();
        Globales.Globalsitiosrecoexp = new ArrayList<>();
        Globales.lista = new ArrayList<>();
        Globales.Globalnombrecoexp = data.get(position).getName();
        posmanual = 1;

        Log.e("ERROR", String.valueOf(datasitio.size()));

        for (int i = 0; i < datasitio.size(); i++) {

            Log.e("ERROR", data.get(position).getItiId());
            Log.e("ERROR", datasitio.get(i).sitioRecoId);

            if(data.get(position).getItiId().equals(datasitio.get(i).sitioRecoId) ) {

                Log.e("ITEM A AGREGAR", String.valueOf(datasitio.get(i)));
                Log.e("NOMBRE DEL SITIO", String.valueOf(datasitio.get(i).getName()));
                Log.e("POSICION DEL SITIO", String.valueOf(datasitio.get(i).getPos()));

                //datasitio.get(i).sitioPos = posmanual;
                Globales.lista.add(datasitio.get(i));


            }

        }

        if (Globales.lista.size() != 1) {

            while (!Globales.lista.isEmpty()) {

                for (int j = 0; j < Globales.lista.size(); j++) {

                    if (Globales.lista.get(j).getTipo().equals("Origen")) {

                        Globales.lista.get(j).sitioPos = 0;
                        Globales.Globalsitiosrecoexp.add(Globales.lista.get(j));

                        Globales.lista.remove(Globales.lista.get(j));
                        Log.e("ORIGEN LISTA GSRE", String.valueOf(Globales.Globalsitiosrecoexp));

                    }

                }

                for (int j = 0; j < Globales.lista.size(); j++) {

                    if (Globales.lista.get(j).getTipo().equals("Parada")) {

                        Globales.lista.get(j).sitioPos = posmanual;
                        Globales.Globalsitiosrecoexp.add(Globales.lista.get(j));
                        Globales.lista.remove(Globales.lista.get(j));
                        Log.e("PARADA LISTA GSRE", String.valueOf(Globales.Globalsitiosrecoexp));
                        posmanual = posmanual + 1;
                    }

                }

                for (int j = 0; j < Globales.lista.size(); j++) {

                    if (Globales.lista.get(j).getTipo().equals("Destino")) {

                        Globales.lista.get(j).sitioPos = Globales.Globalsitiosrecoexp.size() + 1;
                        Globales.Globalsitiosrecoexp.add(Globales.lista.get(j));
                        Globales.lista.remove(Globales.lista.get(j));
                        Log.e("DESTINO LISTA GSRE", String.valueOf(Globales.Globalsitiosrecoexp));
                    }

                }
            }
        } else if (Globales.lista.size() == 1) {
            Globales.Globalsitiosrecoexp.add(Globales.lista.get(0));
            Globales.lista.remove(Globales.lista.get(0));
            Log.e("ORIGEN LISTA GSRE", "ENTRE POR UNICO!!!!");
            for (int j = 0; j < Globales.Globalsitiosrecoexp.size(); j++) {

                Log.e("EL UNICO ES", String.valueOf(Globales.Globalsitiosrecoexp.get(j)));

            }
        }



        Log.e("ORIGEN LISTA GSRE", String.valueOf(Globales.Globalsitiosrecoexp));


        if (Globales.Globalsitiosrecoexp.size() != 0){

            Intent intent = new Intent(MisRecorridosActivity.this,
                    RecorridoExpandidoActivity.class);
            startActivity(intent);

        } else {
            new NoSitiosDialog().show(getSupportFragmentManager(), "SimpleDialog");
        }

    }

    @Override
    public void deleteItemClick(View view, int position) {

        pos = position;
        new SimpleDialogDelReco().show(getSupportFragmentManager(), "SimpleDialog");


    }

    @Override
    public void modificarNombre(View view, int position) {

        pos = position;
        new SimpleDialogCamNomReco().show(getSupportFragmentManager(), "SimpleDialog");


    }


    @Override
    public void onPossitiveButtonClickDel() {

        eliminarRecorrido(data.get(pos).getItiId());
        cargarRecorridos(preferenceSettingsUnique.getString("ID", ""));

    }

    @Override
    public void onNegativeButtonClickDel() {

    }

    @Override
    public void onPossitiveButtonClickCNR(String s) {

        modinombre(data.get(pos).getItiId(), s);
        cargarRecorridos(preferenceSettingsUnique.getString("ID", ""));

    }

    @Override
    public void onNegativeButtonClickCNR() {

    }
}
