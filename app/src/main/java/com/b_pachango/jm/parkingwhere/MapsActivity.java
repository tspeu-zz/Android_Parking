package com.b_pachango.jm.parkingwhere;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    private double latitud;
    private double longitud;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private DecimalFormat decimales = new DecimalFormat("0.0000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);

        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(28.4559911, -16.2855148), 15);
        mMap.moveCamera(camUpd1);

        LatLng cifp = new LatLng(28.455, -16.2836);

        mMap.addMarker(new MarkerOptions().position(cifp).title("your car").snippet("your car")
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.fab_ic_add))));

        mMap.setOnMapClickListener(this);

        mMap.setOnMarkerClickListener(this);

        mMap.setOnMapLongClickListener(this);

    }

    @Override
    public void onMapClick(LatLng latLng) {

        mMap.addMarker(new MarkerOptions().position(latLng).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .snippet("latitud: " + decimales.format(latLng.latitude) + "\nlongitud: " +
                        decimales.format(latLng.longitude)).title("EJ3 DAM3 JM_B"));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        removeMarker(mMap);

        Toast.makeText(MapsActivity.this, "Ahora puede Clicar en el Marcador que quiere eliminar\n " +
                "y volver a cargar el mapa para añadir marcadores", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        // marker.showInfoWindow();

        LatLng latLng = marker.getPosition();

        Toast.makeText(MapsActivity.this,
                "Click\n" + "Lat: " + decimales.format(latLng.latitude) + "\n" +
                        "Lng: " + decimales.format(latLng.longitude) + "\nMARKER pos: " + marker.getPosition() +
                        "\n TITULO: " + marker.getTitle() + "\nMARKER: " + marker.getSnippet(),
                Toast.LENGTH_SHORT).show();

        return false;
    }

    public void addMarker(View view) {
        mMap.addMarker(new MarkerOptions().position(
                new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude)));
    }

    public void removeMarker(GoogleMap v) {

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                marker.remove();
                return false;
            }
        });

    }
/*
    private LatLng getCurrentPosition() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();             // set criteria for location provider:
        criteria.setAccuracy(Criteria.ACCURACY_FINE);   // fine accuracy
        criteria.setCostAllowed(false);                 // no monetary cost

        String bestProvider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return TODO;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        LatLng myPosition= new LatLng(location.getLatitude(), location.getLongitude());
        Toast.makeText(this, "current position: "+ location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
        return myPosition;
    }

    LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

   // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Your Location is"));

*/





}
