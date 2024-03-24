package com.example.arbitragetracker;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.arbitragetracker.scanner.ProductAPI;
import com.example.arbitragetracker.scanner.ProductSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EbayAPI {
    private Context context;
    //Token will probably expire
    private final String token = "v^1.1#i^1#I^3#f^0#p^1#r^0#t^H4sIAAAAAAAAAOVYa2wUVRTe7bYlpC0EeaYiLsMzyMzOax8zdDcu7UI30nbpbnk0IWUed7ZTZmc2M3daVvjRFCFEatIgID5+NNaaaEw0gClpDNGaKJEEiGJCAlrEqBD5gfzAZ9SZ3aVsKwGkm9jE/bOZc8899/u+e869ZwbvLp++al/9vp+rnNNK+rvx7hKnk6jAp5eXPTXDVVJd5sALHJz93Uu7S3tc12oMLqWk2WZgpDXVAO6dKUU12KwxiJi6ymqcIRusyqWAwUKBjYcbNrAkhrNpXYOaoCmIO1oXRDifIElEgJIE2s/4RK9lVe/ETGhBhKAo3C+INBegCJrigTVuGCaIqgbkVBhESJykUZxCSTpB+FmvlyUZzM8QrYh7E9ANWVMtFwxHQlm4bHauXoD1/lA5wwA6tIIgoWh4XbwpHK2LNCZqPAWxQnkd4pCDpjH+qVYTgXsTp5jg/ssYWW82bgoCMAzEE8qtMD4oG74D5hHgZ6WmRR/tJziR8xJegQyQRZFynaanOHh/HLZFFlEp68oCFcow8yBFLTX4DiDA/FOjFSJa57b/NpqcIksy0INIZG14azgWQ0KRTk6N6ZqJhnVehjqXRGPNdahAkrjIBHAe5QNUAPAUkV8oFy0v84SVajVVlG3RDHejBtcCCzWYqA1doI3l1KQ26WEJ2ogK/Zg7Ggb8rfam5nbRhO2qva8gZQnhzj4+eAfGZkOoy7wJwViEiQNZiayySqdlEZk4mM3FfPrsNIJIO4Rp1uPp6urCuihM05MeEscJz5aGDXGhHaQ4xPK1az3nLz94AipnqQhWmVr+LMykLSw7rVy1AKhJJEQzJEn78rqPhxWaaP2HoYCzZ3xFFKtC/F6GZEhe8tMkJfK4rxgVEsonqcfGAXgug6Y4fQeAaYUTACpYeWamgC6LLOWVSCogAVT0MRJKM5KE8l7RhxISADgAPC8wgf9ToTxsqseBoANYlFwvWp63N8aeoTKRdS067Io0d2r1GiVkiIyEM5tJQ2HozUJy7eYwoWxsjAQfthruSb5WkS1lEtb6xRDArvXiiVCvGRCIk6IXF7Q0iGmKLGSm1gZTuhjjdJiJA0WxDJMiGU6no8U5q4tG718eE4/Gu3h31H90P92TlWGn7NRiZc83rABcWsbsGwgTtJTHrnWNs9oP29yWRT0p3rLVuU4p1hbJHFtZzLWcWJYuZnQKmA4MzdStbhtrsjuwhLYDqNZ9BnVNUYC+aXJ5bddzKmVCjlfAVCvsIiS4zE2xy5bwEwSFW40bMyleQvYqbZtqR1IxjuLS9Y/YVnvGv+SHHNkf0eMcwXucp0qcTrwGX0YswReXu1pKXZXVhgwBJnMSZshJ1Xp31QG2A2TSnKyXzHbcev1wfW11pOnIql2JzPlXP3VUFnxj6N+GLxj7yjDdRVQUfHLAF94dKSNmzq8iaZwiacLv9ZJMK77k7mgpMa90Tu/Vl2dfOjj0/u8rK76c0Ve9YH/HHhWvGnNyOsscpT1OR9dnHw1d7xtpdL9y9lnfB83HDu7yDEjxgcGOo4efPIT/MnjyuzOjK5YvuvH2cMeRcyufficx5/Je1+C83d83vBACgy9VjTTs/q1lbhBb6Pj8z9uLo/zwqZu+pUNvXBhtYcCsNXsqbw6/qzRza9o6Vt/as2355bdGeqWtJadHyb6zg5cGVpc/zh88sLf1W3T2x2/267tuL9pP/Hix9dpXNUd7twb+Uk+e793fc+hE3/GZo/NnsOsvrBn+qXnWjQpzWdt7P5wInxk6sWiuK3nsjw+fu6iem/biY6evCpV1v3q+qLlZP3/7a+aN5weuR8iNV5PHn/jm3JV5Xy87Wt25gvzk2OItBw5vp68cUXyJoQO5vfwbcDgJSv0RAAA=";

    public EbayAPI(Context context) {
        this.context = context;
    }

    public void getProductData(String keyword, EbayListener listener) {
        String url = "https://api.ebay.com/buy/browse/v1/item_summary/search?q=" + keyword + "&limit=1&fieldgroups=FULL";

        // Request a JSON response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the itemSummaries array
                            JSONArray itemSummaries = response.getJSONArray("itemSummaries");

                            // Extract the product
                            JSONObject firstItem = itemSummaries.getJSONObject(0);

                            // Extract itemWebUrl and price
                            String itemWebUrl = firstItem.getString("itemWebUrl");

                            JSONObject priceObject = firstItem.getJSONObject("price");
                            double price = priceObject.getDouble("value");

                            Log.d("tag", "Item Web URL: " + itemWebUrl);
                            Log.d("tag", "Item price: " + price);

                            //create product to send to the listener
                            listener.onProductReceived(itemWebUrl, price);
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
                }) {
            @Override
            //creates custom http request with the application token
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        ProductSingleton.getInstance(context).getRequestQueue().add(request);
    }

    //methods are overridden and given outcomes in the fragment
    public interface EbayListener {
        void onProductReceived(String url, Double price);
        void onFetchError();
    }
}


