package com.a500.sweng.sickness_locator;

import com.a500.sweng.sickness_locator.models.User;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TabHost;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.util.Log;

public class UserSettingsActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseUser;
    private EditText inputName, inputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        inputName = (EditText) findViewById(R.id.nameText);
        inputEmail = (EditText) findViewById(R.id.emailText);

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseUser.child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                inputName.setText(user.getName());
                inputEmail.setText(user.getEmail());
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

        tabSpc = tabHost.newTabSpec("password");
        tabSpc.setContent(R.id.passwordTab);
        tabSpc.setIndicator("Password");
        tabHost.addTab(tabSpc);
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
