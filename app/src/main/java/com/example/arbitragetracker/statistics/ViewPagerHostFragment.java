package com.example.arbitragetracker.statistics;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arbitragetracker.ProductDatabase;
import com.example.arbitragetracker.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * This class represents the ViewPagerHost which displays the statistic content
 * @author Tej Baidwan
 */
public class ViewPagerHostFragment extends Fragment {

    //ViewPager2 object
    ViewPager2 viewPager2;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Parameters
    private String mParam1;
    private String mParam2;

    public ViewPagerHostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPagerHostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPagerHostFragment newInstance(String param1, String param2) {
        ViewPagerHostFragment fragment = new ViewPagerHostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Budnling the information onCreate
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * The method which creates the viewpager host and populates it using the custom viewpager adapter
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager_host, container, false);

        //Database instance
        ProductDatabase productDatabase = new ProductDatabase(getContext());

        //Finding ViewPager2 and assigning the adapter and page transformer
        viewPager2 = view.findViewById(R.id.vpHost);
        viewPager2.setAdapter(new CustomViewPagerAdapter(getActivity(), productDatabase));
        viewPager2.setPageTransformer(new DepthPageTransformer());

        return view;
    }

    /**
     * This method contains code which creates the TabLayout and sets the values
     * @param view - The view we are on
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabLayout = view.findViewById(R.id.gallery);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) ->
                tab.setText(("\u25CF"))).attach();
    }
}