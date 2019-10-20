package com.example.cafefinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    Integer proximityRadius = 220;
    Double searchCafeLatitude, searchCafeLongitude;
    String queryCafe;
    Button searchButton, listViewButton;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setupToolBarMenu();
        setupNavigationDrawerMenu();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFrag.getMapAsync(this);


        final Context context = this;


        final SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

    }

    private void setupToolBarMenu(){
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupNavigationDrawerMenu(){
        NavigationView navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawer);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    public void searchCafe(){

        Log.d("search", "in searchCafe");

        final String cafeName = queryCafe;

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        MarkerOptions mo = new MarkerOptions();
        try {
            addressList = geocoder.getFromLocationName(cafeName, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("search", "size = " + addressList.size());
        Log.d("search", "search string= "+cafeName);


        LatLng latlng = null;

        for (Address a : addressList){
            System.out.println("loop called, cafe:= " + cafeName);
            latlng = new LatLng(a.getLatitude(), a.getLongitude());
            mo.position(latlng);
            mo.title("Result");
            mGoogleMap.addMarker(mo);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
            mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            searchCafeLatitude = a.getLatitude();
            searchCafeLongitude = a.getLongitude();
            showNearbyCafes(a.getLatitude(), a.getLongitude(), mo);
        }
    }

    public void showNearbyCafes(double latitude, double longitude, MarkerOptions mo){
        String cafe = "cafe";
        String url = getUrl(latitude, longitude, cafe);
        Object[] dataTransfer = new Object[4];
        dataTransfer[0] = mGoogleMap;
        dataTransfer[1] = url;
        dataTransfer[2] = latitude;
        dataTransfer[3] = longitude;

        NearbyPlacesData nearbyPlacesData = new NearbyPlacesData();
        nearbyPlacesData.execute(dataTransfer);

        View view = findViewById(R.id.mapView);
        Snackbar.make(view,"Showing Nearby Cafes",Snackbar.LENGTH_LONG).
                setAction("Action",null).show();
    }

    private String getUrl(double lat, double lng, String cafe){
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+lat+","+lng);
        googlePlaceUrl.append("&radius="+proximityRadius);
        googlePlaceUrl.append("&type="+cafe);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyCbg9fLFMam8aWFYe27Gl9wx_k1ydIk35M");

        return googlePlaceUrl.toString();
    }

    public List<HashMap<String,String>> getNearbyPlaces(double lat, double lng) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+lat+","+lng);
        googlePlaceUrl.append("&radius="+500);
        googlePlaceUrl.append("&type="+"cafe");
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyCbg9fLFMam8aWFYe27Gl9wx_k1ydIk35M");

        String url = googlePlaceUrl.toString();
        Object[] dataTransfer = new Object[3];
        dataTransfer[0] = url;
        dataTransfer[1] = lat;
        dataTransfer[2] = lng;

        NearbyPlacesData nearbyPlacesData = new NearbyPlacesData();
        nearbyPlacesData.execute(dataTransfer);

        List<HashMap<String,String>> nearbyPlacesList = nearbyPlacesData.mNearbyPlaceList;
        return nearbyPlacesList;
    }
    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }

        View view = findViewById(R.id.mapView);
        Snackbar.make(view,"Fetched Current Location",Snackbar.LENGTH_LONG).
                setAction("Action",null).show();
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        queryCafe = s;
        searchCafe();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        String itemName = (String)menuItem.getTitle();

        View view = findViewById(R.id.mapView);


        //start next activity

        closeDrawer();

        if ("List View".equals(itemName)){
            Snackbar.make(view,"Loading List View",Snackbar.LENGTH_LONG).
                    setAction("Action",null).show();
            //Toast.makeText(this, "List View Selected", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, CafeListActivity.class);
            intent.putExtra("searchCafeLatitude", searchCafeLatitude);
            intent.putExtra("searchCafeLongitude", searchCafeLongitude);
            startActivity(intent);
        }

        return true;
    }

    private void closeDrawer(){

        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
