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
    private final String token = "v^1.1#i^1#p^1#I^3#r^0#f^0#t^H4sIAAAAAAAAAOVYbWwURRi+67UFhGoiWLExeC7iD8zuzX5ce7v2Dq/X1p7Q3rV3pdKAuB9zvaV7u5edOdoDNKWJTUD4AQYMErVqkJj6HUFF1ASNSDTE+PFLEaJBlGg0fKQRQ3H3epRrJYD0Ept4fy7zzjvvPM8z7zszO6CvfMbCgaaB4QrntJLBPtBX4nTSM8GM8rJ7bnSVVJU5QIGDc7Dvrr7SftfPtUhMaWmhDaK0oSPo7k1pOhJyRj+RMXXBEJGKBF1MQSRgWYgFm5cIDAWEtGlgQzY0wh2u9xOKzCs1NM3yPBC9NdWWUb8YMm74CZ8iSjTLQB8POC8NGKsfoQwM6wiLOvYTDGA4EnAk7Y0DWuBogeUpnq7pJNxLoYlUQ7dcKEAEcmiF3FizAOqVkYoIQRNbQYhAONgYiwTD9Q0t8VpPQaxAXoYYFnEGjW+FDAW6l4paBl55GpTzFmIZWYYIEZ7A6AzjgwrBi2CuA35OaVqRvKyPk4DI11Rziq8oUjYaZkrEV8ZhW1SFTORcBahjFWevpqilhrQKyjjfarFChOvd9l9rRtTUhApNP9FQF1wWjEaJQMNqUY+aRoYMmpKKTbGLjLbVkzLDAIX3AYmUfKwPSiydn2g0Wl7mCTOFDF1RbdGQu8XAddBCDSdqwxZoYzlF9IgZTGAbUaEfO6Yh12kv6ugqZnBSt9cVpiwh3Lnm1VdgbDTGpiplMByLMLEjJ5GfENNpVSEmduZyMZ8+vchPJDFOCx5PT08P1cNShtnlYQCgPQ82L4nJSZgSCdvXrvWcv3r1AaSaoyJDayRSBZxNW1h6rVy1AOhdRIDjGYarzus+HlZgovUfhgLOnvEVUawKYWtYha1WfLIkcrLXZjH5Cgnkk9Rj44CSmCVTotkNcVoTZUjKVp5lUtBUFYH1JhjWl4CkUs0nSI5PJEjJq1STdAJCAKEkybzv/1Qo15rqMSibEBcn14uV58mW6GI229DYbuKehrbVRpPBylk6mwB8B4M0nuuQu+o6grTW2tLgv9ZquCz5kKZaysSt+YsigF3rRROhyUAYKpOiF5ONNIwamipnp9YCs6YSFU2cjUFNswyTIhlMp8NF2quLRe9fbhPXx7uIZ9R/cz5dlhWyU3ZqsbLHIyuAmFYp+wSiZCPlMexaF63rh21emUM9Kd6qdXOdUqwtkqNsVWX0ykkZNl0KrZYpEyIjY1q3bSpi38DiRjfUrfMMm4amQXPp5PLarudUKoNFSYNTrbCLkOCqOMUOW7qGZmnOy/D8pHjJuaN05VTbkoqyFZc2Xt+12jP+Gz/gyP3ofucB0O/8oMTpBLVgAT0f3Fnuai91zapCKoaUKiYopHbp1rerCalumE2Lqlky23Hq+W1NoaqGyPaFa+PZL3YedMwqeGIYXAHmjj0yzHDRMwteHMDtl3rK6JturWA4wNFeQHM0y3eC+Zd6S+nK0jnbn7r7xCK1ZfDP95cdNoe6g3s6Rr4DFWNOTmeZo7Tf6fBF9m368dC8Rz/3CWcWuJwPHHI9tPDLRw59tTl+gPyBeXPjW6+9vK1z17wyh/bHN8fqjw5d6Ln53ceeDfedrLhvf+X5Ae3ILSsH4LkLzadfPPtcxbq6I6uYOe+N/Lptz9uzy+Xdpxb/PjL9r70j+9Yw88DxYWLubYvqjm5Y3jirMn68+Zezxg0nyQ+f/P7rFxDd+UTV7oFuJrl267Hw+ntPfXxm/ZxpvU8PD3/0Sax+V+3BnzaGdh471x5MZo/7tks71F37labPZlIblzxDEBt2tJ6IfNt+/4pYxcPZvVuH1u3rDocO3/HK6aHOwy+tCD0+fcumzfGDR4/81vTqOyWVve42x5pscjnwvN563vz0jdG1/BtoZ0E4/BEAAA==";
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


