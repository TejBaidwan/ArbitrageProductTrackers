package com.example.arbitragetracker.scanner;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * This is the ProductSingleton class which represents a singleton design for the Product objects
 * @author Tej Baidwan
 */
public class ProductSingleton {

    //Properties
    public static ProductSingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    private ProductSingleton(Context context){
        this.context = context;
    }

    //getInstance
    public static ProductSingleton getInstance(Context context){
        if(instance == null){
            instance = new ProductSingleton(context);
        }
        return instance;
    }

    //RequestQueue
    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

}
