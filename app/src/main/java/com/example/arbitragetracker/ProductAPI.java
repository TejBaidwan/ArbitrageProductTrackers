package com.example.arbitragetracker;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductAPI {
    private Context context;

    public ProductAPI(Context context) {
        this.context = context;
    }

    public void getProduct(String barcode, ProductListener listener) {

        //trim barcode and check validity before request
        barcode.trim();
        if (!isValidBarcode(barcode)) {
            listener.onInvalidBarcode();
            return;
        }

        String url = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + barcode;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //get product name and description
                            JSONArray itemsArray = response.getJSONArray("items");
                            JSONObject item = itemsArray.getJSONObject(0);
                            String title = item.getString("title");
                            String description = item.getString("description");

                            //get product image
                            JSONArray imagesArray = item.getJSONArray("images");
                            String imgUrl = imagesArray.getString(0);

                            //get product price
                            JSONArray offersArray = item.getJSONArray("offers");
                            JSONObject offer = offersArray.getJSONObject(0);
                            double price = offer.getDouble("price");

                            Product product = new Product(title, description, price, imgUrl);
                            listener.onProductReceived(product);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onFetchError();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFetchError();
                    }
                }
        );
        ProductSingleton.getInstance(context).getRequestQueue().add(request);
    }
    public boolean isValidBarcode(String barcode) {
        // Remove any non-digit characters from the barcode
        String digitsOnly = barcode.replaceAll("[^0-9]", "");
        // Check if the length is exactly 12 digits
        if (digitsOnly.length() != 12) {
            return false;
        }
        // If the length is correct, return true
        return true;
    }

    //methods are overridden and given outcomes in the fragment
    public interface ProductListener {
        void onProductReceived(Product product);
        void onFetchError();
        void onInvalidBarcode();
    }
}
