package com.a500.sweng.sickness_locator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.a500.sweng.sickness_locator.common.AppPreferences;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;


public class BaseActivity extends AppCompatActivity {

    /**
     * Displays the menu.
     */
    String loginType;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        loginType = AppPreferences.getLoginType(this);
        return true;
    }

    /**
     * Changes screen view.
     * This is triggered by the user selecting an item on the menu. Will change the view to whichever
     * option the user selects.
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_entry:
                startActivity(new Intent(this, SicknessEntryActivity.class));
                return true;
            case R.id.menu_map:
                Intent intentMap = new Intent(getApplicationContext(), MapsActivity.class);
                intentMap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMap);

//                startActivity(new Intent(this, MapsActivity.class));
                return true;
            case R.id.menu_reports:
                Intent intent = new Intent(this, ReportsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, UserSettingsActivity.class));
                return true;
            case R.id.logout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                /*if(loginType.equals("Facebook")){
                    LoginManager.getInstance().logOut();
                }*/
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentLogin);
                finish();
//                startActivity(new Intent(this, LoginActivity.class));
//                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}