package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 28.12.17.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    // Do steps 5 - 11 within SettingsFragment
    // COMPLETED (10) Implement OnSharedPreferenceChangeListener from SettingsFragment

    // COMPLETED (8) Create a method called setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference

    // COMPLETED (5) Override onCreatePreferences and add the preference xml file using addPreferencesFromResource

    // Do step 9 within onCreatePreference
    // COMPLETED (9) Set the preference summary on each preference that isn't a CheckBoxPreference

    // COMPLETED (13) Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop

    // COMPLETED (12) Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart

    // COMPLETED (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        for(int i=0;i<preferenceScreen.getPreferenceCount();i++) {
            Preference p = preferenceScreen.getPreference(i);
            if (!(p instanceof CheckBoxPreference)) {
                setPreferenceSummary(p,sharedPreferences.getString(p.getKey(),""));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference p = findPreference(key);
        if (p != null && !(p instanceof CheckBoxPreference)) {
            setPreferenceSummary(p,sharedPreferences.getString(p.getKey(),""));
        }
    }

    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString(), stringKey = preference.getKey();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int preferenceIndex = listPreference.findIndexOfValue(stringValue);
            if (preferenceIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[preferenceIndex]);
            }
            return;
        }
        preference.setSummary(stringValue);
    }
}
