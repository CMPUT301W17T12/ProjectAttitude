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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projectattitude.projectattitude.Controllers.UserController;
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
        //GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnInfoWindowClickListener,
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
    private int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;

    private UserController userController = UserController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Handles everything
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();


        if ( ContextCompat.checkSelfPermission( MapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( MapActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    MY_PERMISSION_ACCESS_COARSE_LOCATION );
        }

        /**
         * http://stackoverflow.com/questions/18425141/android-google-maps-api-v2-zoom-to-current-location 4/1/2017 4:20pm
         */
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        enableMyLocation();

        //couldn't get ColorMap to work, so made one for the meantime
        HashMap<String, BitmapDescriptor> hm = new HashMap<String, BitmapDescriptor>();
        hm.put("Anger", BitmapDescriptorFactory.fromResource(R.drawable.ic_anger_colour_36px));//defaultMarker(356));
        hm.put("Confusion", BitmapDescriptorFactory.fromResource(R.drawable.ic_confusion_colour_36px));//defaultMarker(19));
        hm.put("Disgust", BitmapDescriptorFactory.fromResource(R.drawable.ic_disgust_colour_36px));//defaultMarker(65));
        hm.put("Fear", BitmapDescriptorFactory.fromResource(R.drawable.ic_fear_colour_36px));//defaultMarker(42));
        hm.put("Happiness", BitmapDescriptorFactory.fromResource(R.drawable.ic_happiness_colour_36px));//defaultMarker(160));
        hm.put("Sadness", BitmapDescriptorFactory.fromResource(R.drawable.ic_sadness_colour_36px));//defaultMarker(60));
        hm.put("Shame", BitmapDescriptorFactory.fromResource(R.drawable.ic_shame_colour_36px));//defaultMarker(200));
        hm.put("Surprise", BitmapDescriptorFactory.fromResource(R.drawable.ic_surprise_colour_36px));//defaultMarker(22));



        //Taken from https://developers.google.com/maps/documentation/android-api/marker
        //On March 21st at 17:53

        map.setOnInfoWindowClickListener(this);

        if(getIntent().hasExtra("users")) {
            ArrayList<User> users = (ArrayList<User>) getIntent().getSerializableExtra("users");
            GPSTracker gps = new GPSTracker(MapActivity.this);
            //LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                latitude = Math.round(gps.getLatitude() * 10000d) / 10000d;
                longitude = Math.round(gps.getLongitude() * 10000d) / 10000d;

                if(latitude != 0 & longitude != 0) {
                    Toast.makeText(MapActivity.this, "Found your location",
                            Toast.LENGTH_LONG).show();

                    Log.d("Distance", "Current Location: " + latitude + " " + longitude);
                    for (int i = 0; i < users.size(); i++) {

                        Mood mood = users.get(i).getFirstMood();

//                if(mood.getLatitude()!= null && mood.getLongitude() != null) {
                        if (mood != null) {
                            Double returned = calculateDistance(latitude, longitude, mood.getLatitude(), mood.getLongitude());
                            returned = returned/1000;

                            Log.d("Distance", "Current Distance: " + returned);
                            Log.d("Distance", "Current comparison to: " + users.get(i).getUserName() + " " + mood.getEmotionState());

                            if (returned < 5) {
                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(mood.getLatitude(), mood.getLongitude()))
                                        .title(mood.getMaker())
                                        .snippet(mood.getEmotionState())
                                        .icon(hm.get(mood.getEmotionState())))
                                        .setTag(mood);
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

                if (mood.getLongitude() == 0 && mood.getLatitude() == 0) {
                    Log.d("MapMoods", "Mood: " + mood.getEmotionState() + "not mapped");
                } else {

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(mood.getLatitude(), mood.getLongitude()))
                            .title(mood.getMaker())
                            .snippet(mood.getEmotionState())
                            .icon(hm.get(mood.getEmotionState())))
                            .setTag(mood);

                }
            }
        }

        else{
            Toast.makeText(MapActivity.this, "MIts Fucked, nothing go passed",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Shows mood view activity on info window click
     * @param marker
     */
    @Override
    public void onInfoWindowClick(final Marker marker) {
        Intent intent = new Intent(MapActivity.this, ViewMoodActivity.class);
        intent.putExtra("mood", (Mood) marker.getTag());
        startActivityForResult(intent, 1);
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

    /**
     * Ensures it has the proper locations
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
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

    /**
     * Came with the interface
     */
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

    /**
     * This function calculates the distance from user to a mood for the 5km map
     * algorithm taken from https://en.wikipedia.org/wiki/Haversine_formula March 28th, 2017
     * @param myLat the lattitude of the user
     * @param myLong the longitude of the user
     * @param moodLat the lattitude of the mood
     * @param moodLong the longitude of the mood
     * @return the distance between the user and the mood
     */
    public double calculateDistance(Double myLat, Double myLong, Double moodLat, Double moodLong){
        Location me   = new Location("");
        Location dest = new Location("");

        me.setLatitude(myLat);
        me.setLongitude(myLong);

        dest.setLatitude(moodLat);
        dest.setLongitude(moodLong);

        double dist = me.distanceTo(dest);
        //Log.d("Distance",dist+"");
        return dist;
    }

}
