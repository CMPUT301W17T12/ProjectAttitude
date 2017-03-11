package com.projectattitude.projectattitude.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.projectattitude.projectattitude.R;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

/**
 * This class is used for managing the Map activity. The user is able to interact with the map
 * through scrolling, zooming and clicking on pins displayed which will bring up additional
 * information, allowing the user to view moods at the associated pins.
 */

public class MapActivity extends Activity {
    private MapView map;        //the map the user sees
    private MapController mapController;    // allows moving the map, zooming in and such
    private LocationManager locationManager;    // for keeping track of the user's location
    private ItemizedOverlay<OverlayItem> mMyLocationOverlay;    // for placing icons
    private DefaultResourceProxyImpl mResourceProxy;    //graphics

    ArrayList<OverlayItem> items;
    private MyLocationNewOverlay mLocationOverlay;

    //taken from https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library
    //March 8th, 2017 at 5:38PM

    /**
     * On creation of the map activity, the map loads the appropriate information and defaults to
     * the geolocation of Edmonton, setting up infrastructure to allow the map to manipulated
     * and information to be placed.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        //Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_map);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);    //for building the map

        map.setBuiltInZoomControls(true);   //controls enabled
        map.setMultiTouchControls(true);
        map.setClickable(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = new Location(LocationManager.GPS_PROVIDER);

        mResourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map, mResourceProxy);
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);

        IMapController mapController = map.getController(); //controls position of map
        mapController.setZoom(12);
        //GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        //GeoPoint startPoint = mLocationOverlay.getMyLocation();
        GeoPoint startPoint = new GeoPoint(53.5444, -113.4909);    // Edmonton by default
        mapController.setCenter(startPoint);

        //Handling Map events
        //MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        //map.getOverlays().add(0, mapEventsOverlay); //inserted at the "bottom" of all overlays

        // taken from https://github.com/keithweaver/Android-Samples/tree/master/Location/GetUserLocation
        // on March 10th at 4:34PM
        int LOCATION_REFRESH_TIME = 1000;
        int LOCATION_REFRESH_DISTANCE = 5;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, mLocationListener);
    }

    /**
     * When the map screen is resumed, the map needs to update location if possible
     */
    protected void onResume() {
        {
            super.onResume();
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
        }


        LocationListener myLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //updateLoc(location);
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };
    }

    //taken from https://github.com/keithweaver/Android-Samples/tree/master/Location/GetUserLocation
    //at Mar 10th, 2017, 3:33PM
    /**
     * The location listener keeps the location up to date.
     */
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //code
            System.out.println("onLocationChanged");
            // mainLabel.setText("Latitude:" + String.valueOf(location.getLatitude()) + "\n" + "Longitude:" + String.valueOf(location.getLongitude()));
            IMapController mapController = map.getController(); //controls position of map
            //mapController.setZoom(11);
            //GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            //GeoPoint startPoint = mLocationOverlay.getMyLocation();
            GeoPoint position = new GeoPoint(location);
            mapController.setCenter(position);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled");
            //turns off gps services
        }
    };
}