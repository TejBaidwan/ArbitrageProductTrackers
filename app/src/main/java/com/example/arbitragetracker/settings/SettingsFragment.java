package com.example.arbitragetracker.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arbitragetracker.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        //Animation disable setting
        CheckBoxPreference disableAnimationsPreference = findPreference("anim_disable");
        if (disableAnimationsPreference != null) {
            disableAnimationsPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean disableAnimations = (boolean) newValue;

                // Change the state of disable animation setting
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                preferences.edit().putBoolean("anim_disable", disableAnimations).apply();


                // Access the RecyclerView and disable animation if needed
                RecyclerView recyclerView = getActivity().findViewById(R.id.productRecycler);
                if (recyclerView != null) {
                    if (disableAnimations) {
                        recyclerView.setLayoutAnimation(null);
                    }
                }
                return true;
            });
        }
    }
}