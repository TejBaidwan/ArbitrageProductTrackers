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
    private final String token = "v^1.1#i^1#p^1#r^0#I^3#f^0#t^H4sIAAAAAAAAAOVYe2wURRjvtdcij2JUUqA15lhKMMjuzT7usevdJUd7pVXoHVx52Eiafcy2a/d2l5052lPEphoskcQmBiQgSSVgDIh/+AgJIQYwsTGRQNQo+gchwRBCkfgAm4hRd6+lXCsBpJfYxPvnMt98883v95vvm5kd0FMxfcnWxq3DlZ5ppQM9oKfU46FngukV5U/MLiutLi8BBQ6egZ7aHm9v2aUIEjO6JayGyDINBH3dGd1AQt4YJbK2IZgi0pBgiBmIBCwL6fjKFQJDAcGyTWzKpk74muqjhApohQ8EaUmRwjwrq47VuBmzxYwSLB2SGI4HjMSxDBtgnX6EsrDJQFg0cJRgAMORgCPpYAvNCmxYYHiKYUEr4VsLbaSZhuNCASKWhyvkx9oFWO8MVUQI2tgJQsSa4g3pZLypPtHcEvEXxIqN6pDGIs6i8a06U4G+taKehXeeBuW9hXRWliFChD82MsP4oEL8Jpj7gJ+XOhxQIB0SaTVMMwzDS0WRssG0MyK+Mw7XoimkmncVoIE1nLuboo4a0nNQxqOtZidEU73P/VuVFXVN1aAdJRLL4s/EUykiltgkGinbzJJxW9KwLbaTqdX1pMwwQOHDQCKlMBuGEkuPTjQSbVTmCTPVmYaiuaIhX7OJl0EHNZyoDVOgjeOUNJJ2XMUuokK/wE0NGa7VXdSRVcziDsNdV5hxhPDlm3dfgbHRGNualMVwLMLEjrxEUUK0LE0hJnbmc3E0fbpRlOjA2BL8/q6uLqqLpUy73c8AQPvXr1yRljtgRiQcX7fWR/y1uw8gtTwVGTojkSbgnOVg6XZy1QFgtBMxjnfECI7qPh5WbKL1H4YCzv7xFVGsCgmE6IDMKTwnh0NBAItRILHRHPW7MKAk5siMaHdCbOmiDEnZSbNsBtqaIrABlWHDKiSVIK+SHK+qpBRQgiStQggglCSZD/+f6uReMz0NZRvioqR60dK8ozn1NJtLNKyxcVdi9Saz0WTlHJ1TAb+OQTrPrZPbl62L0/qq5kT0XovhtuTrdM1RpsWZf+rVeqOJMFQmRS8tmxZMmbom56bWArO2khJtnEtDXXcMkyIZt6ym4mzVRaP3L7eJ++NdvCPqPzqebssKuSk7tVi545ETQLQ0yj2BKNnM+E231kXn9uGa2/KoJ8Vbcy6uU4q1Q3KEraaM3Dgp06VLoU0yZUNkZm3nsk0l3QtYi9kJDec8w7ap69BeO7m8dus5k8liUdLhVCvsIiS4Jk6xw5YO0SwT4kKAnxQvOX+Utk21LakoW7G34f5u1f7x3/ixkvyP7vWcBL2eT0o9HhABi+iFYEFF2Rpv2axqpGFIaaJKIa3dcD5dbUh1wpwlanbpIyW/7NvRWFedSO5c8kJL7syewZJZBU8MAxvAvLFHhull9MyCFwfw6K2ecvrBuZUMBzg6SLNsmOFbwcJbvV66yjtn+MDvXVqwdrDFe3Xo4MEFTwYp+jNQOebk8ZSXeHs9JW/W+F68euzZ3e9GQjVqbP37Z/wPR/YNLZ65dMFbi68d6z93aPPQ6fOZVvLg4LmL3VXX9v8c3L/7cNvG5Dd7248kH7seOTlUEX25duhP/Y9p/TUXrG+53lOAWXT4DfZ4cv73s4OH5h3/dOevZ+TtJ/q2na2ftY25cH7D0l073qnlqlErV7V7/kc1yfcqOn/4q+7A6cTen3o3/pa8zH9w4kNrRrP0Y8eVLx7q3Lw5fGpQefWVxgNxYfmNSqq6L7moNvL5vIu9h57aniB6toDw0YGvviPO9h3OXFOzb8/p3/ZSzYy2aTfkPWVefnjvldcvfX20dfnAnsuRqueDR66/Vl5hP/7x3C1f9u0KHnugf2Qt/waLVyq0/BEAAA==";
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


