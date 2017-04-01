/*
 * MIT License
 *
 * Copyright (c) 2017 CMPUT301W17T12
 * Authors rsauveho vuk bfleyshe henrywei cs3
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.projectattitude.projectattitude.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.PermissionUtils;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used for managing the Map activity. The user is able to interact with the map
 * through scrolling, zooming and clicking on pins displayed which will bring up additional
 * information, allowing the user to view moods at the associated pins.
 */

public class MapActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        //ColorMap<String, Integer> cMap = new ColorMap<>();
        //couldn't get ColorMap to work, so made one for the meantime
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("Anger", "#e3333e");
        hm.put("Confusion", "#ed8b5f");
        hm.put("Disgust", "#c0ca55");
        hm.put("Fear", "#684f15");
        hm.put("Happiness", "#7fc7af");
        hm.put("Sadness", "#919185");
        hm.put("Shame", "#005885");
        hm.put("Surprise", "#e36820");

//        Integer val = (Integer) cMap.get(mood.getEmotionState());

        //Taken from https://developers.google.com/maps/documentation/android-api/marker
        //On March 21st at 17:53
//        map.addMarker(new MarkerOptions()   // adding a marker
//                .position(new LatLng(53.5444, -113.4909))   // Edmonton location
//                .title("Edmonton"));

        //User user = (User) getIntent().getSerializableExtra("user");
        //ArrayList<Mood> userMoodList = user.getMoodList();
        if(getIntent().hasExtra("users")) {
            ArrayList<User> users = (ArrayList<User>) getIntent().getSerializableExtra("users");
            GPSTracker gps = new GPSTracker(MapActivity.this);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                latitude = Math.round(gps.getLatitude() * 10000d) / 10000d;
                longitude = Math.round(gps.getLongitude() * 10000d) / 10000d;

                if(latitude != 0 & longitude != 0) {
                    Toast.makeText(MapActivity.this, "Could not find your location, please try again!",
                            Toast.LENGTH_LONG).show();

                    Log.d("Distance", "Current Location: " + latitude + " " + longitude);
                    for (int i = 0; i < users.size(); i++) {

                        Mood mood = users.get(i).getFirstMood();

//                if(mood.getLatitude()!= null && mood.getLongitude() != null) {
                        if (mood != null) {
                            Double returned = calculateDistance(latitude, longitude, mood.getLatitude(), mood.getLongitude());

                            if (returned < 5) {
                                String color = hm.get(mood.getEmotionState());
                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(mood.getLatitude(), mood.getLongitude()))
                                        .title(mood.getMaker())
                                        .snippet(mood.getEmotionState())
                                        .icon(getMarkerColor(color)));
                            }
                        }
                    }
                }

                else{
                    Toast.makeText(MapActivity.this, "Could not find your location, please try again!",
                            Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(MapActivity.this, "Please turn on GPS for locations!",
                        Toast.LENGTH_LONG).show();
            }
        }

        else if (getIntent().hasExtra("user")){
            ArrayList<Mood> userMoodList =  (ArrayList<Mood>) getIntent().getSerializableExtra("user");
            for (int i = 0; i < userMoodList.size(); i++) { //TODO this will get EVERY mood from the user, which could be too many

                Mood mood = userMoodList.get(i);
                String color = hm.get(mood.getEmotionState());
                Log.d("MapMoodsColor", color);

                if (mood.getLongitude() == 0 && mood.getLatitude() == 0) {
                    Log.d("MapMoods", "Mood: " + mood.getEmotionState() + "not mapped");
                } else {
                    //Integer val = (Integer) cMap.get(mood.getEmotionState());
                    //Log.d("MapMoodsColor", val.toString());
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(mood.getLatitude(), mood.getLongitude()))
//                        .title(mood.getMaker() +" " + mood.getEmotionState())
                            .title(mood.getMaker())
                            .snippet(mood.getEmotionState())
                            .icon(getMarkerColor(color)));
                }
//            if(mood.getGeoLocation() != null){
//                double latitude = mood.getGeoLocation().getLatitude();  //get lat
//                double longitude = mood.getGeoLocation().getLongitude();    //get long
//
//                map.addMarker(new MarkerOptions()   // adding a marker for mood
//                        .position(new LatLng(latitude, longitude))   // Mood location
//                        .title(userMoodList.get(i).getTrigger()));  // named after the trigger
//            }
            }
        }

        else{
            Toast.makeText(MapActivity.this, "MIts Fucked, nothing go passed",
                    Toast.LENGTH_LONG).show();
        }


    }

    /**
     * taken from http://stackoverflow.com/questions/19076124/android-map-marker-color
     * @param color
     * @return
     */
    public BitmapDescriptor getMarkerColor(String color){
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

//    public double calculateDistance(GeoPoint myLocation, GeoPoint moodLocation){  // calculates the difference between two points on the earth
        //algorithm taken from https://en.wikipedia.org/wiki/Haversine_formula March 28th, 2017
    public double calculateDistance(Double myLat, Double myLong, Double moodLat, Double moodLong){

//        double myLatitude = myLocation.getLatitude();
//        double myLongitude = myLocation.getLongitude();
//        double moodLatitude = moodLocation.getLatitude();
//        double moodLongitude = moodLocation.getLongitude();
//        double earthRadius = 6371e3;

        double earthRadius = 6371;

//        double dlong = (moodLongitude - myLongitude);
//        double dlat = (moodLatitude - myLatitude);

        double dlong = (moodLong - myLong);
        double dlat = (moodLat - myLat);

////        double a =(Math.sin(dlat/2)*Math.sin(dlat/2)) + Math.cos(myLatitude) * Math.cos(moodLatitude) * (Math.sin(dlong/2) * Math.sin(dlong/2));
        double a =(Math.sin(dlat/2)*Math.sin(dlat/2)) + Math.cos(myLat) * Math.cos(moodLat) * (Math.sin(dlong/2) * Math.sin(dlong/2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//
        Log.d("Distance", earthRadius*c + "");

//        Location me   = new Location("");
//        Location dest = new Location("");
//
//        me.setLatitude(myLat);
//        me.setLongitude(myLong);
//
//        dest.setLatitude(moodLat);
//        dest.setLongitude(moodLong);

        return earthRadius * c;

//        float dist = me.distanceTo(dest);
//        Log.d("Distance",dist+"");
//        return dist;
    }

}
