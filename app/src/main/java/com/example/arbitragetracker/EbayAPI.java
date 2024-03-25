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
    private final String token = "v^1.1#i^1#f^0#r^0#I^3#p^1#t^H4sIAAAAAAAAAOVYe2wURRjv9UVaKD6oFBqjZStGgd3bx+09lt7Btb3aCr0evfKMPPZ2Z9uFfWVntuUigVK1JsYUAwFDQKmY0EQSo5IqRkElGoyJkRhEY3xhhJgo8ZEIBoy6u3eUayWA9BKbeP9c5ptvvvn9fvN9M7ND9paWzelv7r9Q4ZlUONhL9hZ6PNRksqy0ZO7UosLqkgIyx8Ez2HtPb3Ff0fd1kFcVg2sH0NA1CGo2qooGOdcYxixT43QeypDTeBVADglcMtq6mKMJkjNMHemCrmA1LY1hLCCxkpRiAiHJzwRpIWRbtcsxO/QwxgdIkeFplhVpyu8TUnY/hBZo0SDiNRTGaJL24SSD02wH5eN8LMewRNDvW4XVLAMmlHXNdiFILOLC5dyxZg7Wa0PlIQQmsoNgkZZoU7It2tIYi3fUeXNiRbI6JBGPLDi61aCLoGYZr1jg2tNA15tLWoIAIMS8kcwMo4Ny0ctgbgK+KzUdDFCsJFL+ACADdCqYFymbdFPl0bVxOBZZxCXXlQMaklH6eoraaqTWAwFlW3E7REtjjfO3xOIVWZKBGcZi9dGV0UQCi8S6eS1h6hYeNVMyMvlOPNHeiAs0TYqhIJnCU0EmCFIMlZ0oEy0r85iZGnRNlB3RYE1cR/XARg3GasPkaGM7tWltZlRCDqJcP/9lDdnAKmdRM6tooS7NWVeg2kLUuM3rr8DIaIRMOWUhMBJhbIcrkV02hiGL2NhONxez6bMRhrEuhAzO6+3p6SF6GEI3O700SVLeFa2Lk0IXUHnM9nVqPeMvX38ALrtUBGCPhDKH0oaNZaOdqzYArROL+EI07fNndR8NKzLW+g9DDmfv6IrIV4UwJMuQZFAMBQCgQ4G8VEgkm6ReBwdI8Wlc5c0NABkKLwBcsPPMUoEpi3YsiWaCEsBFf0jCfSFJwlOs6McpCQASgFRKCAX/T4Vyo6meBIIJUF5yPW953hVPLGLSsaalJuqJtXfrzTojpKm0RIaW01AJ+ZYLnfXLo5SyJB4L32g1XJV8gyLbynTY8+dDAKfW8ydCsw4REMdFLynoBkjoiiykJ9YCM6aY4E2UTgJFsQ3jIhk1jJb87NV5o/cvt4mb452/M+o/Op+uygo6KTuxWDnjoR2AN2TCOYEIQVe9Tq3rvH39cMxrXdTj4i3bN9cJxdommWEri5krJ+HSJWC3QJgA6pZp37aJNucG1qFvAJp9niFTVxRgLhtfXjv1rKoW4lMKmGiFnYcEl/kJdthSAYpiAoEQw46Ll+AepWsn2paUj624+IGbvFZ7R3/kRwrcH9XnOUb2eY4WejxkHTmbqiVnlRYtLS6aUg1lBAiZlwgod2r2t6sJiA0gbfCyWTit4Nf9O5sbqmNtu+Y83JE+sed4wZScN4bB1eSMkVeGsiJqcs6TA3nnlZ4S6paqCtpHMjRL+Xwsw64ia6/0FlPTiytP3vHU0Xsjzy169IkFr529L7LvrfLGs2TFiJPHU1JQ3OcpYJ/c8pNv5fppR4KH67+qvG32i4ermLr9J/EPP110vvSXNbs/UM55BowmbEXjweb+HcE1LwU8C+Obj9Gnhtfu9c648PwQP/z6bwcWnPqzkqiosvYcoIZ+H3hs9fvPsvrXb1R9Mjz7CFa7a96p0jN/MT/u+Pnl2Dufl797962RYOWh+dt6/PFXhlRDqZ+7k3j6ru2HDoWf6Ws9v/SPwZg6MFN9EF6y3tz2yJbd0oVzW9u+27zpcbb9o237P95tbt+HfzOwxrt3XdHpt8uP9s4o7b60buHWkveEz8qru8/MYy5KM4f7uyfdP98z7/jtQ1+W/TC19YS0rp56YfrFWuzb2jLi1S8OwsWbds16KHF6KLOWfwMx2I9L/REAAA==";

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


