package com.rightdecisions.diagonapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rightdecisions.diagonapp.DirectionCallback;
import com.rightdecisions.diagonapp.GoogleDirection;
import com.rightdecisions.diagonapp.activities.TransportMode;
import com.rightdecisions.diagonapp.model.Direction;
import com.rightdecisions.diagonapp.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rightdecisions.diagonapp.R;

import java.util.ArrayList;
import java.util.List;

public class MapsRecoActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, DirectionCallback {
    private Button btnRequestDirection;
    private GoogleMap googleMap;
    private String serverKey = "AIzaSyBdDzM01Fqp8UYpHSlpYAkpfN0-tuNfScw";
    private LatLng camera = new LatLng(-34.930723,-57.940932);
    private LatLng origin = new LatLng(-34.930723,-57.940932);
    List<LatLng> asd = new ArrayList<>();
    private LatLng w1 = new LatLng(-34.955065,-57.935980);
    private LatLng w2 = new LatLng(-34.915065,-57.945662);
    private LatLng w3 = new LatLng(-34.925065,-57.935662);
    private LatLng w4 = new LatLng(-34.917065,-57.955662);
    private LatLng w5 = new LatLng(-34.916065,-57.985662);
    private LatLng w6 = new LatLng(-34.925065,-57.935462);

    private LatLng destination = new LatLng(-34.915065,-57.935658);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_reco);

        asd.add(w1);
        asd.add(w2);
        asd.add(w3);
        asd.add(w4);
        asd.add(w5);
        asd.add(w6);
        btnRequestDirection = (Button) findViewById(R.id.btn_request_direction);
        btnRequestDirection.setOnClickListener(this);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, 13));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_request_direction) {
            requestDirection();
        }
    }

    public void requestDirection() {
        Snackbar.make(btnRequestDirection, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .waypoints(asd)
                .transportMode(TransportMode.WALKING)
                .optimizeWaypoints(true)
                .execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            googleMap.addMarker(new MarkerOptions().position(origin)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            for (int i = 0; i < asd.size(); i++) {
                googleMap.addMarker(new MarkerOptions().position(asd.get(i))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }

            googleMap.addMarker(new MarkerOptions().position(destination)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            ArrayList<LatLng> directionPositionList = null;

            for (int i = 0; i < direction.getRouteList().size(); i++) {
                for (int j = 0; j < direction.getRouteList().get(i).getLegList().size(); j++) {
                    directionPositionList = direction.getRouteList().get(i).getLegList().get(j).getDirectionPoint();
                    googleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
                }

                //ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                //googleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
            }

            btnRequestDirection.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(btnRequestDirection, t.getMessage(), Snackbar.LENGTH_SHORT).show();
    }
}
