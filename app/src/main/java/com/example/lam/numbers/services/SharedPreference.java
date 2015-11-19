package com.example.lam.numbers.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.lam.numbers.R;

/**
 * Created by Lam on 11/16/2015.
 */
public class SharedPreference {
    private Activity activity;
    private SharedPreferences sharedPref;

    public SharedPreference(Activity activity) {
        super();
        this.activity = activity;
        sharedPref = activity.getSharedPreferences(
                activity.getString(R.string.shared_pref), Context.MODE_PRIVATE);
    }

    public void write(String key, Integer value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int read(String key, Integer defaultValue) {
        int value = sharedPref.getInt(key, defaultValue);
        return value;
    }

}
