package com.example.arbitragetracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arbitragetracker.scanner.ProductAPI;
import com.example.arbitragetracker.settings.CurrencyUtil;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static final String PRODUCT = "product";
    public static final int DISPLAY = 1;
    public static final int ADD = 2;
    public static final String ACTION_TYPE = "action_type";
    Product product;
    EbayAPI ebayAPI;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailsFragment newInstance(String param1, String param2) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        ImageView detailImage = view.findViewById(R.id.detailImage);
        TextView detailName = view.findViewById(R.id.detailName);
        TextView detailDescription = view.findViewById(R.id.detailDescription);
        TextView detailPrice = view.findViewById(R.id.detailPrice);
        ImageView detailStatBtn = view.findViewById(R.id.detailStatBtn);
        ImageView detailEditBtn = view.findViewById(R.id.detailEditBtn);
        Button addToInventoryBtn = view.findViewById(R.id.addToInvBtn);

        ebayAPI = new EbayAPI(getContext());
        product = getArguments().getParcelable(PRODUCT);

        //only displays the addtoinventory button if it has just been scanned
        //The button will not display if the productDetails fragment has been reached through the inventory screen
        if (getArguments().getInt(ACTION_TYPE) == DISPLAY){
            addToInventoryBtn.setVisibility(View.GONE);
        }

        Picasso.get().load(product.getImgUrl()).into(detailImage);
        detailName.setText(product.getName());
        detailDescription.setText(product.getDescription());
        //Display converted price with selected currency symbol
        String selectedCurrency = CurrencyUtil.getSelectedCurrency(getContext());
        String priceWithSymbol = CurrencyUtil.formatPriceWithCurrencySymbol(product.getPrice(), selectedCurrency);
        detailPrice.setText(priceWithSymbol);

        //Add product to the database then navigate to the inventory
        addToInventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product != null){
                    ProductDatabase db = ProductDatabase.getInstance(getContext());
                    db.addProduct(product);
                    Toast.makeText(requireContext(), "Product Added", Toast.LENGTH_SHORT).show();

                    Navigation.findNavController(view)
                            .navigate(R.id.nav_recycler);
                }
            }
        });
        detailEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEbayData(product);
            }
        });

        return view;
    }

    //Retrieves product information from ebay
    private void getEbayData(Product product){
        ebayAPI.getProductData(product.getName(), new EbayAPI.EbayListener() {
            @Override
            public void onProductReceived(String url, Double price) {
                Log.d("tag", "Ebay Price: " + url);
                Log.d("tag", "Ebay url: " + price);
            }
            @Override
            public void onFetchError() {Toast.makeText(requireContext(), "Unable to get product information from Ebay", Toast.LENGTH_SHORT).show();}
        });
    }
}