package com.example.cafefinder;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    String url;
    GoogleMap map;
    double searchLat, searchLng;
    List<HashMap<String,String>> mNearbyPlaceList;


    @Override
    protected String doInBackground(Object... objects) {
        map = (GoogleMap)objects[0];
        url = (String)objects[1];
        searchLat = (Double) objects[2];
        searchLng = (Double) objects[3];


        ConnectionUrl connectionUrl = new ConnectionUrl();
        try {
            googlePlacesData = connectionUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyPlaceList = null;
        DataParser dataParser = new DataParser();
        mNearbyPlaceList = nearbyPlaceList;

        nearbyPlaceList = dataParser.parse(s);
        showNearbyPlaces(nearbyPlaceList);

       // mNearbyPlaceList = new ArrayList<>();
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList){

        for (int i = 0; i < nearbyPlacesList.size(); i++){
            MarkerOptions mo = new MarkerOptions();
            HashMap<String,String> googlePlace = nearbyPlacesList.get(i);

            String placeName = googlePlace.get("placeName");
            String vicinity = googlePlace.get("vicinity");
            double latitude = Double.parseDouble(googlePlace.get("latitude"));
            double longitude = Double.parseDouble(googlePlace.get("longitude"));
            String reference = googlePlace.get("reference");
            double rating = Double.parseDouble(googlePlace.get("rating"));


            LatLng latLng = new LatLng(latitude, longitude);
            mo.position(latLng);
            mo.title(placeName);
            if (latitude != searchLat && longitude != searchLng)
                mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            map.addMarker(mo);
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));

        }


    }

    public List<HashMap<String,String>> getNearByPlacesList(){
//        Log.d("placesdata", String.valueOf(mNearbyPlaceList.size()));
        return mNearbyPlaceList;
    }
}
