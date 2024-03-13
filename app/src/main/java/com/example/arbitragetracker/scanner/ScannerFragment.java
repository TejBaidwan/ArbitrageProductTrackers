package com.example.arbitragetracker.scanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arbitragetracker.Product;
import com.example.arbitragetracker.ProductDatabase;
import com.example.arbitragetracker.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScannerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView prodName,prodDesc,prodPrice;
    ImageView productImg;
    String barcodeValue = null;

    ProductDatabase db;
    ProductAPI productAPI;
    Product productOnScreen;

    public ScannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScannerFragment newInstance(String param1, String param2) {
        ScannerFragment fragment = new ScannerFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        Button scanBtn = view.findViewById(R.id.scanButton);
        Button addBtn = view.findViewById(R.id.addButton);
        TextView scannerOutput = view.findViewById(R.id.scannerOutput);
        EditText manualEditText = view.findViewById(R.id.manualEnterEditText);
        Button manualEnterButton = view.findViewById(R.id.manuallyEnterBtn);

        prodName = view.findViewById(R.id.productTitle);
        prodDesc = view.findViewById(R.id.productDescription);
        prodPrice = view.findViewById(R.id.productPrice);
        productImg = view.findViewById(R.id.productImg);

        productAPI = new ProductAPI(getContext());
        db = ProductDatabase.getInstance(getContext());

        //Starts scan with camera
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleScan(scannerOutput);
            }
        });

        //Searches for products manually with a code
        manualEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String barcode = manualEditText.getText().toString();
                if (barcode.length() == 12){
                    getProduct(barcode);
                }else Toast.makeText(requireContext(), "Barcode must be 12 digits in length!", Toast.LENGTH_SHORT).show();
            }
        });

        //Adds products to the database
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productOnScreen != null){
                    db.addProduct(productOnScreen);
                    Toast.makeText(requireContext(), "Product Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    //get product information from the barcode and handle errors
    private void getProduct(String barcode){
        productAPI.getProduct(barcode, new ProductAPI.ProductListener() {
            @Override
            public void onProductReceived(Product product) {setProductValues(product);}
            @Override
            public void onFetchError() {Toast.makeText(requireContext(), "Error fetching product", Toast.LENGTH_SHORT).show();}
            @Override
            public void onInvalidBarcode() {Toast.makeText(requireContext(), "Invalid barcode", Toast.LENGTH_SHORT).show();}
        });
    }

    //set the retrieved product data to the ui
    private void setProductValues(Product product){
        prodName.setText(product.getName());
        prodDesc.setText(product.getDescription());
        prodPrice.setText(String.valueOf(product.getPrice()));
        Log.d("TAG", "URL" + product.getImgUrl());
        if (productImg!=null){
            Picasso.get().load(product.getImgUrl()).into(productImg);
        }
        productOnScreen = product;
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
    private void handleScan(TextView scannerOutput) {
        startScan().addOnSuccessListener(new OnSuccessListener<Barcode>() {
            @Override
            public void onSuccess(Barcode barcode) {
                // Handle successful scan
                barcodeValue = barcode.getRawValue();
                scannerOutput.setText("Code: " + barcodeValue);

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