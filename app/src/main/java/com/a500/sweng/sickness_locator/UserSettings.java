package com.a500.sweng.sickness_locator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class UserSettings extends AppCompatActivity {

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
}
