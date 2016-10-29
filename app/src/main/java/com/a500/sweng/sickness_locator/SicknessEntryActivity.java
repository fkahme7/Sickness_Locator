package com.a500.sweng.sickness_locator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SicknessEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sickness_entry);

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
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (selectedItem.equals("Bacterial")) {
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
                } else if (selectedItem.equals("Viral")) {

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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_entry:
                startActivity(new Intent(this, SicknessEntryActivity.class));
                return true;
            case R.id.menu_map:
                startActivity(new Intent(this, MapsActivity.class));
                return true;
            case R.id.menu_reports:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, UserSettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

