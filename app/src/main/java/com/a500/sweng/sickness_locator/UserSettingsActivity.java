package com.a500.sweng.sickness_locator;

import com.a500.sweng.sickness_locator.models.User;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

public class UserSettingsActivity extends BaseActivity {

    private DatabaseReference mDatabaseUser;
    private EditText inputName, inputEmail, inputDob;
    private Button btnUpdateSettings;
    private Spinner genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        inputName = (EditText) findViewById(R.id.nameText);
        inputEmail = (EditText) findViewById(R.id.emailText);
        inputDob = (EditText) findViewById(R.id.dobText);
        btnUpdateSettings = (Button) findViewById(R.id.update_settings_button);
        genderSpinner = (Spinner) findViewById(R.id.sexSpinner);

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseUser.child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                inputName.setText(user.getName());
                inputEmail.setText(user.getEmail());
                inputDob.setText(user.getDob());

                String gender = user.getGender() + "";

                String[] genders = getResources().getStringArray(R.array.genders);
                genderSpinner.setSelection(Arrays.asList(genders).indexOf(gender));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DatabaseError", "Failed to read value.", error.toException());
            }
        });

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();
        TabHost.TabSpec tabSpc = tabHost.newTabSpec("user profile");
        tabSpc.setContent(R.id.userProfileTab);
        tabSpc.setIndicator("User Profile");
        tabHost.addTab(tabSpc);

        btnUpdateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                String name = inputName.getText().toString().trim();
                String dob = inputDob.getText().toString().trim();
                String gender = genderSpinner.getSelectedItem().toString();

                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                mDatabaseUser.child(fUser.getUid()).child("name").setValue(name);
                mDatabaseUser.child(fUser.getUid()).child("dob").setValue(dob);
                mDatabaseUser.child(fUser.getUid()).child("gender").setValue(gender);

                Toast.makeText(getApplicationContext(), "User Settings Updated", Toast.LENGTH_LONG).show();
            }
        });
    }
}
