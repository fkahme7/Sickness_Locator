package com.a500.sweng.sickness_locator;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.location.Address;
import android.location.Geocoder;

import com.a500.sweng.sickness_locator.common.GlobalCache;
import com.a500.sweng.sickness_locator.models.SicknessEntry;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Android activity that displays a google map. The map displays all map markers for sickness entries
 * in the firebase database. The map defaults to show the users current location, if the user allows
 * permission. The map will display the entire United States as a default if permission is not given
 * to access the users's current position.
 *
 */
public class MapsActivity extends BaseActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    // Reference to the firebase databse
    private DatabaseReference mDatabaseSicknessEntries;

    // Default latitude for the map
    private double mLatitude = 38.3887885;

    // Default longitude for the map
    private double mLongitude = -93.5316315;

    // The default zoom on the map
    private float mZoom = 4.25f;

    // The minimum zoom the map is allowed to render
    private float mMaxZoom = 14.0f;
    FirebaseDatabase db;
    List<String> sickList = new ArrayList<String>();
    protected GlobalCache gCache = GlobalCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 11);
            return;
        }
        db = FirebaseDatabase.getInstance();
        serviceCall();
        // Try to get the users current location
        LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria cri = new Criteria();
        cri.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationmanager.getBestProvider(cri, false);

        if (provider != null & !provider.equals("")) {
            LocationListener locationChangeListener = new LocationListener() {
                public void onLocationChanged(Location l) {
                    if (l != null) {
                        mLatitude = l.getLatitude();
                        mLongitude = l.getLongitude();
                        mZoom = mMaxZoom;
                    }
                }
                public void onProviderEnabled(String p) {}
                public void onProviderDisabled(String p) {}
                public void onStatusChanged(String p, int status, Bundle extras) {}
            };

            Location location = locationmanager.getLastKnownLocation(provider);
            locationmanager.requestLocationUpdates(provider, 2000, 1, locationChangeListener);
            if (location != null) {
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                mZoom = mMaxZoom;
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void serviceCall() {

        final DatabaseReference ref = db.getReference("sicknessEntries");

        Query queryRef = ref.orderByChild("sickness");

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChild) {
                System.out.println(dataSnapshot.getValue());
                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                String name1 = String.valueOf(value.get("sickness"));
                String name2 = String.valueOf(value.get("latitude"));
                Log.i("sickness", String.valueOf(value.get("sickness")));

               // latList.add(name2);
                sickList.add(name1);
                gCache.setSickEntry(sickList);
                Log.i("groupList Array ", sickList.toString());
                Log.i("sickness", "value = " + value);
                Log.i("sickness", "value size = " + value.size());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("groupList Array ", "onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i("groupList Array ", "onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.i("groupList Array ", "onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("groupList Array ", "onChildCancelled");
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * Here we are connecting to the firebase database and retrieving the sickness entries. We are then
     * displaying those entries on the map. The map will default to display the current location of the
     * user. If that permission is denied, then the map will display a default United States location.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(mMaxZoom);

        mDatabaseSicknessEntries = FirebaseDatabase.getInstance().getReference("sicknessEntries");
        mDatabaseSicknessEntries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey() != null) {
                        for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                            SicknessEntry entry = postSnapshot.getValue(SicknessEntry.class);

                            if (!postSnapshot1.getKey().equals("daysSick") && !postSnapshot1.getKey().equals("latitude") && !postSnapshot1.getKey().equals("longitude") &&
                                    !postSnapshot1.getKey().equals("markerColor") && !postSnapshot1.getKey().equals("severity") && !postSnapshot1.getKey().equals("sickness") &&
                                    !postSnapshot1.getKey().equals("type") && !postSnapshot1.getKey().equals("userId") && !postSnapshot1.getKey().equals("entryDate")) {
                                entry = postSnapshot1.getValue(SicknessEntry.class);
                            }

                            LatLng position = new LatLng(entry.getLatitude(), entry.getLongitude());
                            mMap.addMarker(new MarkerOptions()
                                    .position(position)
                                    .icon(BitmapDescriptorFactory.defaultMarker(entry.getMarkerColor()))
                                    .title(entry.getType() + ": " + entry.getSickness())
                                    .snippet("")
                            );
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        // Move the camera to either the default location, or the users current location
        LatLng position = new LatLng(mLatitude, mLongitude);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(mZoom));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    /**
     * Performs a search based on input from the user. The method uses google location services to
     * decode the string and return a latitude/longitude. The map will be updated and have the camera
     * move to the location that the user searched for.
     */
    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            // Move map camera to the location searched for
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
}
