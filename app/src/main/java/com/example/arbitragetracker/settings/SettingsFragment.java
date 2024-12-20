package com.example.arbitragetracker.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.arbitragetracker.CrossViewAnimHandler;
import com.example.arbitragetracker.Product;
import com.example.arbitragetracker.ProductDatabase;
import com.example.arbitragetracker.R;
import com.example.arbitragetracker.statistics.DepthPageTransformer;

import java.util.ArrayList;

/**
 * This class represents the Settings Fragment which uses SharedPreferences to persists settings options
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    //Property for applying setting choice to the ViewPager2
    private CrossViewAnimHandler crossViewAnimHandler;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        crossViewAnimHandler = new ViewModelProvider(requireActivity()).get(CrossViewAnimHandler.class);

        //Animation disable setting
        CheckBoxPreference disableAnimationsPreference = findPreference("anim_disable");
        if (disableAnimationsPreference != null) {
            disableAnimationsPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean disableAnimations = (boolean) newValue;

                // Change the state of disable animation setting
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                preferences.edit().putBoolean("anim_disable", disableAnimations).apply();

                crossViewAnimHandler.setAnimationsEnabled(!disableAnimations);

                return true;
            });
        }

        //Converts the inventory to a string and adds it to a text file
        Preference exportBtn = findPreference("export_inventory_preference");
        exportBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "INVENTORY");
                String inventoryString = inventoryToString();
                i.putExtra(Intent.EXTRA_TEXT, inventoryString);

                Intent chooser = Intent.createChooser(i, "Choose a note app:");
                try {
                    startActivity(chooser);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(),
                            "No Valid Application Installed",
                            Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

    }

    //Gets all products from the db and formats information to a string
    private String inventoryToString(){
        ProductDatabase db = ProductDatabase.getInstance(getContext());
        ArrayList<Product> products = db.getAllProducts();
        ArrayList<String> productStrings = new ArrayList<>();

        for (Product product : products) {
            String prodName = product.getName();
            String prodDesc = product.getDescription();
            Double prodPrice = product.getPrice();

            // Create a string with product information
            String productInfo = "**************************************\n" +
                    prodName + "\n\n" +
                    prodDesc + "\n\n" +
                    prodPrice + "\n";

            productStrings.add(productInfo); // Add the product information string to the list
        }

        StringBuilder concatenatedString = new StringBuilder();

        for (String productString : productStrings) {
            concatenatedString.append(productString);
        }

        return concatenatedString.toString();
    }




}