package com.example.arbitragetracker.statistics;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arbitragetracker.ProductDatabase;
import com.example.arbitragetracker.R;
import com.example.arbitragetracker.settings.CurrencyUtil;

/**
 * This class represents the StatisticsFragment which contains the VP of stats
 * @author Tej Baidwan
 */
public class StatisticsFragment extends Fragment {

    //Database object
    private ProductDatabase productDatabase;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    //Parameters
    private int mParam1;
    private int mParam2;
    private String mParam3;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3 Parameter 3
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(int param1, int param2, String param3, ProductDatabase database) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        fragment.productDatabase = database;
        return fragment;
    }

    /**
     * Bundling the information to the next page
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    /**
     * Method which displays information when the view is loaded
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        //Nodes on the screen
        TextView statsTitle = view.findViewById(R.id.statTitle);
        ImageView statsImage = view.findViewById(R.id.statsImage);
        TextView statsValue = view.findViewById(R.id.statsValue);


        //Setting the values of the nodes to the bundled information
        if (mParam1 != 0 && mParam2 != 0) {
            statsTitle.setText(mParam1);
            statsImage.setImageResource(mParam2);
            statsValue.setText("Result: " + mParam3);
        }else{
            statsTitle.setText(R.string.noItems);
            statsImage.setImageResource(mParam2);
            statsValue.setText(R.string.noItemValue);
        }

        return view;
    }
}