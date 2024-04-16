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

/**
 * This class represents the EbayAPI usage which fetches product info with the EBAY API
 * @author Evan Proulx
 */
public class EbayAPI {
    private Context context;
    //Token will probably expire
    private final String token = "v^1.1#i^1#f^0#r^0#I^3#p^1#t^H4sIAAAAAAAAAOVYf2wTVRxv9wOdMEEBMRNGvSEmG3d9d9d2vcta060dHbB1WwsoAfHu+m47dr0rd6/bKiYsMxADgZnoZoyA4F8KIVMJoFFJBJxRItH4E41EMURMEBP+0JgY9d2tjG4SQNbEJfaf5n3f933f9/N538977x7om1ZWvTW69bdy521Fe/tAX5HTSU8HZdNKa+4sLqoodYA8B+fevkV9Jf3FF+pMIaWm+XZopnXNhK7elKqZvG0MEBlD43XBVExeE1LQ5JHEx0PNK3iGAnza0JEu6SrhagoHCJGlOTEpQwBoyDFMElu1KzETeoAQav0ikCDwMX7ASR4/7jfNDGzSTCRoKEAwgPGQwEPSvgTt470c7wUUC5g1hGsVNExF17ALBYignS5vjzXycr1+qoJpQgPhIESwKdQYj4WawpGWRJ07L1Ywx0McCShjjm816EnoWiWoGXj9aUzbm49nJAmaJuEOjs4wPigfupLMLaRvUw1F1s94gcyyXC0mUy4IlY26kRLQ9fOwLEqSlG1XHmpIQdkbMYrZEDdACeVaLThEU9hl/bVlBFWRFWgEiEh96JFQaysRjHQLWquhZ8iQISrIEDrI1vYwKTEMSHJ+IJKin/Vj+HRuotFoOZonzNSga0nFIs10teioHuKs4URuPHncYKeYFjNCMrIyyvfz5zhkuNo11qKOrmIGdWrWusIUJsJlN2+8AmOjETIUMYPgWISJHTZFWDbptJIkJnbatZgrn14zQHQilObd7p6eHqqHpXSjw81gHbofbl4RlzphSiCwr6X1UX/lxgNIxYYiQTzSVHiUTeNcenGt4gS0DiLowRL3+HK8j08rONH6D0MeZvd4RRRKIV6fxMocWyv6/aLshaAQCgnmitRt5QFFIUumBKMLorQqSJCUcJ1lUtBQkjzrlRnWL0My6eNk0sPJMil6kz6SliEEEIqixPn/T0K52VKPQ8mAqCC1XrA672xpXc5mI40rDdQTae/WozorZemsDLjVjKlyntVSR/3qEK22tUQCN6uGa4JvUBXMTALPXwgCLK0XjoSobiKYnBS8uKSnYauuKlJ2ai0wayRbBQNl41BVsWFSIEPpdFNh9uqCwfuX28St4S7cGfUfnU/XRGVaJTu1UFnjTRxASCuUdQJRkp5yW1rXBXz9sMzr7awnhVvBN9cphRqDHEWrJEevnJQNlzK7JcqApp4x8G2bilk3sITeBTV8niFDV1VorJpcXVt6TqUySBBVONWEXYACV4QpdtjStTTL+H21HJgULsk+StdPtS2pEFtxydJbvFa7x3/kBx32j+53Hgf9zmNFTieoAw/QVeD+acUrS4pnVJgKgpQiyJSpdGj429WAVBfMpgXFKJrtuPzSYLShIhIbqt6UyH78wvuOGXlvDHvXgXvHXhnKiunpeU8OYP7VnlJ65rxyxgM8tI/2eTkvWAOqrvaW0PeUzPksejRypLZvZuzQ4Y3KHmHxwi+KToDyMSens9RR0u90zGneNvzQL9WVFW+cCVVvpudXb94+6wcEB141qtZXtoiLh+6bd1ZrX7nurdPHM5cqwYdvBs4MHO49/OgBx7vb9HD4JHni1z0j2oblzoHv7m7ed/qu9EiiR9m59sW51NPhg+e9vh1DrxQf2XWS/+rtZ6NocNGn51fMmT29cscnP71DPx+pClXX73rteN/uDaWDr8MFQe3x0ppTjoFlG7fsgacv7H/i9mU181liuGt415P1I86DS6KPffDMsSVtXrj7ji/LoyIa3jd4seLl7ZcvLvwosq4rVkb/vO/H7tlLG587+2f38iMjOx+kv//6m0Nlf/xVN/dcze+nNn1+ofG9/U95FpzrXbvl20sHZh1tmze6ln8DaQkDTP0RAAA=";
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

                            EbayProduct ebayProduct = new EbayProduct(price, itemWebUrl);
                            //create product to send to the listener
                            listener.onProductReceived(ebayProduct);
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
        void onProductReceived(EbayProduct ebayProduct);
        void onFetchError();
    }
}

class EbayProduct{
    private Double price;
    private String url;

    public EbayProduct(Double price, String url) {
        this.price = price;
        this.url = url;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


