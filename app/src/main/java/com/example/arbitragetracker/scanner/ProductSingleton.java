package com.example.arbitragetracker.scanner;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ProductSingleton {
    public static ProductSingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    private ProductSingleton(Context context){
        this.context = context;
    }

    public static ProductSingleton getInstance(Context context){
        if(instance == null){
            instance = new ProductSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

}
