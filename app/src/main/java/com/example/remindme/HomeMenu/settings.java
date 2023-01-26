package com.example.remindme.HomeMenu;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.remindme.R;

public class settings extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}