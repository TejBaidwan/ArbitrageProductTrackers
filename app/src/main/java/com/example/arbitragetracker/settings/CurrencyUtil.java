package com.example.arbitragetracker.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class CurrencyUtil {
    public static String getSelectedCurrency(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("currency_preference", "USD");
    }

    public static String getCurrencySymbol(String currencyCode) {
        switch (currencyCode.toLowerCase()) {
            case "usd":
                return "$";
            case "eur":
                return "€";
            case "gbp":
                return "£";
            // Add more cases for other currencies
            default:
                return "$"; // Default to "$" if the currency code is unknown
        }
    }

    //return the conversion from usd to the other currency
    public static Double getConversion(String currencyCode) {
        switch (currencyCode.toLowerCase()) {
            case "usd":
                return 1.0;
            case "eur":
                return 0.79;
            case "gbp":
                return  0.92;
            // Add more cases for other currencies
            default:
                return 1.0;
        }
    }

    //Converts price to the correct currency and formats it with the correct symbol
    public static String formatPriceWithCurrencySymbol(double price, String currencyCode) {
        String currencySymbol = getCurrencySymbol(currencyCode);
        Double currencyConversion = getConversion(currencyCode);
        double convertedPrice = price * currencyConversion;
        String formattedPrice = String.format("%.2f", convertedPrice);
        return currencySymbol + formattedPrice;
    }
}
