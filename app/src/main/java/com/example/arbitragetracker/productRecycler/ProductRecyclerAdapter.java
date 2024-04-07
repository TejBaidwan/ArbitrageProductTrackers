package com.example.arbitragetracker.productRecycler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.arbitragetracker.ProductDetailsFragment;
import com.example.arbitragetracker.R;
import com.example.arbitragetracker.settings.CurrencyUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.CustomViewHolder> {
    private ArrayList<Product> products;
    private ArrayList<Product> originalProducts;
    private Context context;
    ProductDatabase db;
    NavController navController;

    public ProductRecyclerAdapter( ArrayList<Product> products, Context context) {
        this.products = products;
        this.originalProducts = new ArrayList<>(products);
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
        //Display price with selected currency symbol
        String selectedCurrency = CurrencyUtil.getSelectedCurrency(context);
        String priceWithSymbol = CurrencyUtil.formatPriceWithCurrencySymbol(product.getPrice(), selectedCurrency);
        holder.price.setText(priceWithSymbol);
        //If product doesn't have an image provide a default
        if (product.getImgUrl() != null){
            Picasso.get().load(product.getImgUrl()).into(holder.image);
        } else{Picasso.get().load(R.drawable.placeholder_image).into(holder.image);}

        holder.checkBox.setChecked(product.isSold() == 1); // Set checked state based on sold status

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update sold status of the product in the database
                updateSoldStatus(product.getId(), isChecked ? 1 : 0); // Convert boolean isChecked to integer
                // Update the product's sold status locally
                product.setSold(isChecked ? 1 : 0); // Convert boolean isChecked to integer
            }
        });

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

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView name;
        protected TextView description;
        protected TextView price;
        protected ImageView image;
        protected ImageView delete;
        protected CheckBox checkBox;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.recyclerName);
            this.description = itemView.findViewById(R.id.recyclerDescription);
            this.price = itemView.findViewById(R.id.recyclerPrice);
            this.image = itemView.findViewById(R.id.recyclerImage);
            this.delete = itemView.findViewById(R.id.deleteBtn);
            this.checkBox = itemView.findViewById(R.id.listingStatus);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Product product = products.get(position);
            Bundle extra = new Bundle();
            extra.putInt(ProductDetailsFragment.ACTION_TYPE, ProductDetailsFragment.DISPLAY);
            extra.putParcelable(ProductDetailsFragment.PRODUCT, product);
            Navigation.findNavController(view)
                    .navigate(R.id.productDetailsFragment, extra);
        }
    }

    private void updateSoldStatus(int productId, int sold) {
        // Get an instance of your database
        db = ProductDatabase.getInstance(context);
        Log.d("SoldStatus", "Before Update: " + getProductStatus(productId));

//        // Get the product from the database
//        Product product = db.getProduct(productId);
//
//        // Update the sold status of the product
//        if (product != null) {
//            product.setSold(sold);
//            // Update the product in the database
//            db.updateProduct(product);
//        }

        db.updateProductStatus(productId, sold);

        Log.d("SoldStatus", "After Update: " + getProductStatus(productId));

        // Close the database connection
        db.close();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    private int getProductStatus(int productId) {
        db = ProductDatabase.getInstance(context);
        Product product = db.getProduct(productId);
        db.close();
        return product != null ? product.isSold() : -1; // Return -1 if product not found
    }


    public void filterItems(boolean soldItemsOnly) {
        // Clear the current list only if you want to refresh it every time
        Log.d("FilterItems", "Filtering items. SoldItemsOnly: " + soldItemsOnly);
        products.clear();
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : originalProducts) {
            // Check if the product's sold status matches the filtering condition
            boolean shouldAdd = soldItemsOnly ? product.isSold() == 1 : product.isSold() == 0;

            // Add the product to the filtered list if it matches the condition
            if (shouldAdd) {
                filteredProducts.add(product);
                Log.d("FilterItems", "Added product: " + product.getName() + ", Sold status: " + product.isSold());
            }
        }

        products.addAll(filteredProducts);

        notifyDataSetChanged(); // Notify adapter of dataset change
    }

}
