package com.a500.sweng.sickness_locator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.a500.sweng.sickness_locator.models.SicknessEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.location.LocationListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SicknessEntryActivity extends BaseActivity {
    private Button btnSubmit, btnCancel;
    private Spinner typeSpinner, sicknessSpinner, severitySpinner;
    private EditText daysText;
    private String typeString, sicknessString, severityString, entryDate;
    private DatabaseReference mDatabaseEntries;
    private ProgressBar progressBar;
    private double latitude, longitude;
    LocationManager locationmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sickness_entry);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    11
            );
            return;
        }

        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria cri = new Criteria();
        cri.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationmanager.getBestProvider(cri, false);

        if (provider != null & !provider.equals("")) {

            LocationListener locationChangeListener = new LocationListener() {
                public void onLocationChanged(Location l) {
                    if (l != null) {
                        latitude = (long) l.getLatitude();
                        longitude = (long) l.getLongitude();
                    }
                }

                public void onProviderEnabled(String p) {
                }

                public void onProviderDisabled(String p) {
                }

                public void onStatusChanged(String p, int status, Bundle extras) {
                }

            };

            Location location = locationmanager.getLastKnownLocation(provider);
            locationmanager.requestLocationUpdates(provider,2000,1, locationChangeListener);
            if(location==null)
            {
                Toast.makeText(getApplicationContext(),"location not found",Toast.LENGTH_LONG ).show();
            }
            else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Provider is null",Toast.LENGTH_LONG).show();
        }

        mDatabaseEntries = FirebaseDatabase.getInstance().getReference("sicknessEntries");
        btnSubmit = (Button) findViewById(R.id.button);
        btnCancel = (Button) findViewById(R.id.button2);
        typeSpinner = (Spinner) findViewById(R.id.spinner);
        sicknessSpinner = (Spinner) findViewById(R.id.spinner2);
        severitySpinner = (Spinner) findViewById(R.id.spinner3);
        daysText = (EditText) findViewById(R.id.editText);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SicknessEntryActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(typeString) || typeString.equals("--Select--") || typeString == " ") {
                    Toast.makeText(getApplicationContext(), "Select a Sickness Type.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(sicknessString) || sicknessString.equals("--Select--")) {
                    Toast.makeText(getApplicationContext(), "Select a Sickness.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(severityString) || severityString.equals("--Select--")) {
                    Toast.makeText(getApplicationContext(), "Select a Severity level.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(daysText.toString())) {
                    Toast.makeText(getApplicationContext(), "Enter the amount of days you've been sick.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //create user

                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                if (fUser != null) {
                    Log.d("Sickness", "sickness entry:" + fUser.getUid());
                    String type = typeString.toString().trim();
                    String sick = sicknessString.toString().trim();
                    String sever = severityString.toString().trim();
                    int days = Integer.parseInt(daysText.getText().toString().trim());
                    entryDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
                    SicknessEntry entries = new SicknessEntry();
                    entries.setDaysSick(days);
                    entries.setSeverity(sever);
                    entries.setSickness(sick);
                    entries.setType(type);
                    entries.setLatitude(latitude);
                    entries.setLongitude(longitude);
                    entries.setEntryDate(entryDate);
                    entries.setUserId(fUser.getUid());

                    mDatabaseEntries.child(fUser.getUid()).child(mDatabaseEntries.child(fUser.getUid()).push().getKey()).setValue(entries);
                }
                startActivity(new Intent(SicknessEntryActivity.this, MapsActivity.class));
                finish();
            }
        });

        sicknessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sicknessString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        severitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                severityString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.severity, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        severitySpinner.setAdapter(adapter);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("sicknessType");
        ref.child("Type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> type = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String name = areaSnapshot.getValue(String.class);
                    type.add(name);
                }

                Spinner sickType = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(SicknessEntryActivity.this, android.R.layout.simple_spinner_item, type);
                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sickType.setAdapter(typeAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Spinner sickness = (Spinner) findViewById(R.id.spinner);
        sickness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeString = parent.getItemAtPosition(position).toString();

                if (typeString.equals("Bacterial")) {
                    DatabaseReference ref = db.getReference("sickness");
                    ref.child("Bacterial").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Is better to use a List, because you don't know the size
                            // of the iterator returned by dataSnapshot.getChildren() to
                            // initialize the array
                            final List<String> bacterial = new ArrayList<String>();

                            for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                                String name = areaSnapshot.getValue(String.class);
                                bacterial.add(name);
                            }

                            Spinner sick = (Spinner) findViewById(R.id.spinner2);
                            ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(SicknessEntryActivity.this, android.R.layout.simple_spinner_item, bacterial);
                            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sick.setAdapter(typeAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else if (typeString.equals("Viral")) {

                    DatabaseReference ref = db.getReference("sickness");
                    ref.child("Viral").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Is better to use a List, because you don't know the size
                            // of the iterator returned by dataSnapshot.getChildren() to
                            // initialize the array
                            final List<String> bacterial = new ArrayList<String>();

                            for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                                String name = areaSnapshot.getValue(String.class);
                                bacterial.add(name);
                            }

                            Spinner sick = (Spinner) findViewById(R.id.spinner2);
                            ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(SicknessEntryActivity.this, android.R.layout.simple_spinner_item, bacterial);
                            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sick.setAdapter(typeAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Spinner sick = (Spinner) findViewById(R.id.spinner2);
                    sick.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

