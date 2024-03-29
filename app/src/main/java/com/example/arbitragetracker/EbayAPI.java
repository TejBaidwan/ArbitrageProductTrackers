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
    private final String token = "v^1.1#i^1#f^0#r^0#p^1#I^3#t^H4sIAAAAAAAAAOVYfWwTZRhf9wFM2AgBFckg5fgwQO56H22vd66N3dq5BrYVWjbBILmPt9ux691571u6RsE5A0YlqKiERIxzGlRQyWKI+IeEYYx/gHEYM6MYY8CQhfgHThjGGPGuLaObBJA1cYn9p7nnfd7n/f1+z/O873tHdk+rXLmzceeVKsf00t5usrvU4aBmkpXTKlZVl5UuqCghCxwcvd1Lu8t7yoZroZBUDX4dgIauQeDsSqoa5LNGP5YyNV4XoAJ5TUgCyCOJjwWb1vA0QfKGqSNd0lXMGQn5MYESZZ+HlmiWo2VJki2rdi1mXPdjHsbn9Umy5OG8CVGkWWscwhSIaBAJGvJjNEm7cZLBaS5OsTzJ8BRLsD5uI+ZsBSZUdM1yIUgskIXLZ+eaBVhvDlWAEJjICoIFIsGGWEswEgo3x2tdBbECeR1iSEApOP6pXpeBs1VQU+Dmy8CsNx9LSRKAEHMFciuMD8oHr4G5A/g5qX2MyLI06xY9EuelyKJI2aCbSQHdHIdtUWQ8kXXlgYYUlLmVopYa4hYgofxTsxUiEnLaf2tTgqokFGD6sXBdcEMwGsUC4a2CFjX1FB40RQWZQjseXRfCJZomZc5HirjoY3xAZKj8QrloeZknrFSva7JiiwadzTqqAxZqMF4bL+8p0MZyatFazGAC2YgK/bgxDT0b7aTmsphCHZqdV5C0hHBmH2+dgbHZCJmKmEJgLMLEgaxEVq4NQ5GxiYPZWsyXTxf0Yx0IGbzLlU6niTRD6Ga7iyZJyvVw05qY1AGSAmb52r2e81duPQFXslQkYM2ECo8yhoWly6pVC4DWjgXcHE27vXndx8MKTLT+w1DA2TW+I4rVISwlAIllEz7W7WFoEhSjQwL5InXZOIAoZPCkYHYCZKiCBHDJqrNUEpiKzDOeBM34EgCXvVwCd3OJBC56ZC9OJQAgARBFifP9nxrldks9BiQToKLUetHqvKM5uprJhBvWmygdXrdVb9QZKUNlEiTXRkOVc7dJ7XVtQUpd2xz232433JB8vapYysSt9YshgN3rxROhUYcIyJOiF5N0A0R1VZEyUyvBjClHBRNlYkBVLcOkSAYNI1Kcvbpo9P7lNnFnvIt3Rv1H59MNWUG7ZKcWK3s+tAIIhkLYJxAh6UmX3eu6YF0/bPPmLOpJ8Vasm+uUYm2RzLFV5NyVk8jSJeBWiTAB1FOmddsmWuwbWFzvBJp1niFTV1Vgtk6uru1+TiZTSBBVMNUauwgFrghT7LClWIpiGYrl2EnxkrJH6eaptiUVYysuf+gOr9Wu8S/5gZLsj+pxnCB7HMdKHQ6yllxGLSEXTytbX142awFUECAUIUFApV2z3l1NQHSCjCEoZunckpG+VxvrF4Rb9q58PJ4ZfO2LklkF3xh6N5Hzx74yVJZRMws+OZA110cqqNn3VtFukqE5iiWtrG8kl1wfLafuKZ/3+otH3GjE2FbzZObD9wJNaJTrcpFVY04OR0VJeY+jpF4cffbjn2c47tO6Xt6w74z74AH56j7q3Nzjr+y+sKNqcf+h84EBrvNC5p3t00eCp79nDi8XjNAD8/r81V1vtAYG/3oqduW3w6u3jJ5qkT6rOTfj8INMpPJz5+knhoaXHr80u+zRU6mTFFH2wlehy73fsY/Ne3rFsu1Df+5Ze/8Hg98O/7ppoOlHnytes3x129Ff6pzHKtJDF+dv23klzfcRh0Jz9l8drur/6O1zu/rvuoAdPbqjUf/pwCPPR1OtAxeDYuaZxbV/dF5edPfA3udGA0Ph/q9/eMtj7H63Knl2xUtY6UnfyCc1p9eMLlzUhy6d/Wb+l+fPfDqH5PdXvzlr6YmFv1e/v+fgql1H9jbkcvk3EeBbwv0RAAA=";
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


