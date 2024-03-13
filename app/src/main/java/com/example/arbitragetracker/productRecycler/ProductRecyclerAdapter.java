package com.example.arbitragetracker.productRecycler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arbitragetracker.MainActivity;
import com.example.arbitragetracker.Product;
import com.example.arbitragetracker.ProductDatabase;
import com.example.arbitragetracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.CustomViewHolder> {
    private ArrayList<Product> products;
    private Context context;
    ProductDatabase db;
    NavController navController;

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

        //handle single product delete
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete selected product from database
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete Product")
                        .setMessage("ARE YOU SURE YOU WANT TO DELETE '" + product.getName() +"' FROM YOUR INVENTORY?")
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //delete selected product from the db
                                db = ProductDatabase.getInstance(view.getContext());
                                db.deleteProduct(product.getId());
                                db.close();

                                //Reload the recycler
                                navController = Navigation.findNavController((Activity) view.getContext(), R.id.nav_host_fragment_content_main);
                                navController.navigate(R.id.nav_recycler);
                            }
                        }).setNegativeButton("NO", null).show();
            }
        });
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
        protected ImageView delete;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.recyclerName);
            this.description = itemView.findViewById(R.id.recyclerDescription);
            this.price = itemView.findViewById(R.id.recyclerPrice);
            this.image = itemView.findViewById(R.id.recyclerImage);
            this.delete = itemView.findViewById(R.id.deleteBtn);

        }
    }
}
