package com.example.arbitragetracker.productRecycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arbitragetracker.Product;
import com.example.arbitragetracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.CustomViewHolder> {
    private ArrayList<Product> products;
    private Context context;

    public ProductRecyclerAdapter( ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Product product = products.get(position);
        holder.name.setText(product.getName());
        holder.description.setText(product.getDescription());
        holder.price.setText(String.valueOf(product.getPrice()));
        Picasso.get().load(product.getImgUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if (products != null){
            return products.size();
        }return 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView name;
        protected TextView description;
        protected TextView price;
        protected ImageView image;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.recyclerName);
            this.description = itemView.findViewById(R.id.recyclerDescription);
            this.price = itemView.findViewById(R.id.recyclerPrice);
            this.image = itemView.findViewById(R.id.recyclerImage);

        }
    }
}
