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
    private final String token = "v^1.1#i^1#I^3#r^0#f^0#p^1#t^H4sIAAAAAAAAAOVYfWwTZRhv94XIgCAEYRItx4hh2Ot7H722l7Wx2zo2hK3QfbAtgHfX97qT6129e7utEZJlifj5jxIHOIQJogjROLM/BIkJJIT4QSLGj0RQhjFCQE0EgiZq9L22jG4SQNbEJTZNm3ve533e3+/3Ps/7caC3ZGrF5rrNv063TykY7AW9BXY7NQ1MLSleOqOwoKzYBnIc7IO95b1FfYXnK00hrib41dBM6JoJHT1xVTP5tNFPJA2N1wVTMXlNiEOTRxIfCa5cwdMk4BOGjnRJVwlHfY2fYNw042U9PiAyLAA+iK3atZhNup/wyCzDUIJEsTQjUrIbt5tmEtZrJhI05CdoQLNOgL90E+Xm3RzPcqTbDdoJRws0TEXXsAsJiEAaLp/ua+RgvTlUwTShgXAQIlAfrI00ButrQg1Nla6cWIGsDhEkoKQ59qlaj0JHi6Am4c2HMdPefCQpSdA0CVcgM8LYoHzwGpg7gJ+WWqREhgMAcoLkdXNROS9S1upGXEA3x2FZlKhTTrvyUEMKSt1KUayG+BiUUPapAYeor3FYf6uSgqrICjT8RKgq2BYMh4lAqEvQwoaedAYNUUGGEHOGV9c4JZoGUZ8XiE7Ry3ihyFDZgTLRsjKPG6la16KKJZrpaNBRFcSo4Xht2BxtsFOj1mgEZWQhyvXzXNOQdbdbk5qZxSTq1Kx5hXEshCP9eOsZGO2NkKGISQRHI4xvSEvkJ4REQokS4xvTuZhNnx7TT3QilOBdru7ubrKbIXUj5qIBoFxrVq6ISJ0wLhDY16r1jL9y6w5OJU1FwmWM/XmUSmAsPThXMQAtRgRYH02zXFb3sbAC463/MORwdo2tiHxViMBRtJfmJK8o41+RykeFBLJJ6rJwQFFIOeOCsQGihCpI0CnhPEvGoaFEecYt46VQhs4o55OdrE+WnaI7yjkpGUIAoShKPu//qVBuN9UjUDIgykuu5y3POxvCjzCpUG2zgbpDq7v0Op2RUlRKBr5W2lR9bKsUq2oNUuqqhpD/dqvhhuSrVQUr04THz4cAVq3nT4Q63UQwOiF6EUlPwLCuKlJqck0wY0TDgoFSEaiq2DAhksFEoj4/a3Xe6P3LZeLOeOdvj/qP9qcbsjKtlJ1crKz+Jg4gJBTS2oFISY+7rFrXBXz8sMzr06gnxFvBJ9dJxRqTzLBVopkjJ5mmS5pdEmlAU08a+LRNNlonsCZ9A9TwfoYMXVWh0TKxvLbqOR5PIkFU4WQr7DwkuCJMss2W8lA08ODr48R4SemtdP1kW5LysRQXLbvDY7Vr7CU/YEt/qD77UdBn/6DAbgeVYDG1CCwsKWwuKiwtMxUESUWQSVOJafjuakByA0wlBMUomG27tPuluuqyUGN/xRNNqU8HjttKc94xDK4F80bfMkwtpKblvHIAC663FFMz751Os4AFNOV2cyzXDhZdby2i5hbN2XT3SPnRy+KhoV1nlpSd9PS77UOPg+mjTnZ7sa2oz25zLFhy/6yL0aPOvrsemvfH8Q5q5vwz4VNrX2dPP2fM//3d5KEHHz64deGe9pbXWvuvbh84lmJfjWnN22tadrKlHQz3zMe1Zc++8Ev/xo2HS0a4F0sPSt++PK1tzwX558++ky6uvbKNfo8/eP5A1+FvHt106cTe4aHlT74xePGB2dVk5YKaZb/tWH7sy9m7nl/cRjL7ZpR/vWrdJ3N2254eObXufT1QsebIqb+2vnMSlA889f25YPesncPc1aVn/f3c5bZ7Rk5sD1RdPntkf/OMfW9t8VWfnff8lLlf2YaWHOgp6OjwvFmy7Qh/ZX9xhW0hsch2YbjxUOVH9s/P/STcd/rDLT/s2Pvn8BfMK2/v+nEglpnLvwECQsHj/REAAA==";
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


