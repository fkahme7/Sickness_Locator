package com.a500.sweng.sickness_locator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class UserSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
