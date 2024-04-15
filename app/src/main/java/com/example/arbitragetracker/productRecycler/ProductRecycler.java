package com.example.arbitragetracker.productRecycler;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.arbitragetracker.Product;
import com.example.arbitragetracker.ProductDatabase;
import com.example.arbitragetracker.ProductDetailsFragment;
import com.example.arbitragetracker.R;
import com.example.arbitragetracker.scanner.ProductAPI;
import com.example.arbitragetracker.settings.SettingsFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.ArrayList;

/**
 * This class represents the Product RecylcerView which has all of the products weve stored
 * @author Evan Proulx
 */
public class ProductRecycler extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProductDatabase db;
    String barcodeValue = null;
    ProductAPI productAPI;

    public ProductRecycler() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductRecycler.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductRecycler newInstance(String param1, String param2) {
        ProductRecycler fragment = new ProductRecycler();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_recycler, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.productRecycler);
        SwitchCompat toggleSwitch = view.findViewById(R.id.filter);
        TextView recyclerIsEmpty = view.findViewById(R.id.inventoryEmptyTextView);
        FloatingActionButton productFab = view.findViewById(R.id.productFab);

        productAPI = new ProductAPI(getContext());
        db = ProductDatabase.getInstance(getContext());

       if (db.getAllProducts().isEmpty() ){
            recyclerIsEmpty.setVisibility(View.VISIBLE);
        }

        //Disables animation if set in preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean animDisabled = preferences.getBoolean("anim_disable", false);
            if (animDisabled) {
                Log.d("tag", "RECYCLER ANIM OFF");
                recyclerView.setLayoutAnimation(null);
            }

        ProductRecyclerAdapter adapter = new ProductRecyclerAdapter(db.getAllProducts(), getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleSwitch.setText(b ? "SOLD LISTINGS" : "ACTIVE LISTINGS");
                adapter.filterItems(b);
            }
        });

        //Run barcode scanner
        productFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {handleScan();}
        });

        //Searches for products manually with a barcode
        productFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
                builder.setTitle("Manually Enter Barcode");

                //Add custom dialog to the dialog to get the editText
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, null);
                builder.setView(dialogView);
                EditText editText = dialogView.findViewById(R.id.customDialogEditText);

                //check if the code is valid.If it is get product
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String barcode = editText.getText().toString();
                        if (barcode.length() == 12){
                            getProduct(barcode);
                        }else Toast.makeText(requireContext(), "Barcode must be 12 digits in length!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.show();
                return false;
            }
        });

        return view;
    }



    //////////////////////////////////////////////
    //CODE FOR SCANNER
    //////////////////////////////////////////////

    //get product information from the barcode and handle errors
    private void getProduct(String barcode){
        productAPI.getProduct(barcode, new ProductAPI.ProductListener() {
            @Override
            public void onProductReceived(Product product) {navigateToProductScreen(product);}
            @Override
            public void onFetchError() {Toast.makeText(requireContext(), "Error fetching product", Toast.LENGTH_SHORT).show();}
            @Override
            public void onInvalidBarcode() {Toast.makeText(requireContext(), "Invalid barcode", Toast.LENGTH_SHORT).show();}
        });
    }

    //once a product is scanned navigate to the product detail screen with its information
    private void navigateToProductScreen(Product product){
        Bundle extra = new Bundle();
        extra.putInt(ProductDetailsFragment.ACTION_TYPE, ProductDetailsFragment.ADD);
        extra.putParcelable(ProductDetailsFragment.PRODUCT, product);
        Navigation.findNavController(getView())
                .navigate(R.id.productDetailsFragment, extra);

    }


    private Task<Barcode> startScan() {
        //Sets settings for the scanner
        //Only scans QR and barcodes
        GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
                .build();

        //runs the scanner
        GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(getContext());
        return scanner.startScan();
    }


    //This allows for the output to be returned
    private void handleScan() {
        startScan().addOnSuccessListener(new OnSuccessListener<Barcode>() {
            @Override
            public void onSuccess(Barcode barcode) {
                // Handle successful scan
                barcodeValue = barcode.getRawValue();

                //get the product data after scanning
                if (barcodeValue != null){
                    getProduct(barcodeValue);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle scan failure
                Log.d("Scanner Failed", e.getLocalizedMessage());
                Toast.makeText(getContext(), "The scanner failed. Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}