package com.a500.sweng.sickness_locator.common;

import android.content.Context;
import android.content.SharedPreferences;


public class AppPreferences {

    public static void setLoggedIn(Context c, boolean isLoggedIn) {
        SharedPreferences sp = c.getSharedPreferences("is_logged_in", 0);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("is_logged_in", isLoggedIn);
        editor.commit();
    }

    public static Boolean getLoggedIn(Context c) {
        SharedPreferences sp = c.getSharedPreferences("is_logged_in", 0);
        return sp.getBoolean("is_logged_in", false);
    }

    public static void setLoginType(Context c, String mGPUserName) {
        SharedPreferences sp = c.getSharedPreferences("LOGIN_TYPE", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("LOGIN", mGPUserName);
        editor.commit();
    }

    public static String getLoginType(Context c) {
        SharedPreferences sp = c.getSharedPreferences("LOGIN_TYPE", 0);
        return sp.getString("LOGIN", "");
    }

}
