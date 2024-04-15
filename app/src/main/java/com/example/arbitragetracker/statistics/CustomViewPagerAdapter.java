package com.example.arbitragetracker.statistics;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.arbitragetracker.ProductDatabase;
import com.example.arbitragetracker.R;
import com.example.arbitragetracker.settings.CurrencyUtil;

/**
 * This class represents the ViewPager adapter which populates the ViewPager found in the Stats Fragment
 */
public class CustomViewPagerAdapter extends FragmentStateAdapter {
    //Database object
    private ProductDatabase productDatabase;

    //Constructor
    public CustomViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ProductDatabase productDatabase) {
        super(fragmentActivity);
        this.productDatabase = productDatabase;
    }

    /**
     * This method gets the position the app is in in the ViewPager and populates it with parameters via newInstance()
     * @param position - The position in the ViewPager
     * @return Fragment
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return StatisticsFragment.newInstance(R.string.statOne, R.drawable.appiconimage, productDatabase.getTotalItemCount(), productDatabase);
            case 1:
                return StatisticsFragment.newInstance(R.string.statTwo, R.drawable.appiconimage, productDatabase.getTotalPrice(), productDatabase);
            case 2:
                return StatisticsFragment.newInstance(R.string.statThree, R.drawable.appiconimage, productDatabase.getHighestPrice(), productDatabase);
            case 3:
                return StatisticsFragment.newInstance(R.string.statFour, R.drawable.appiconimage, productDatabase.getAveragePrice(), productDatabase);
            default:
                return StatisticsFragment.newInstance(R.string.noItems, R.drawable.appiconimage, R.string.noItemValue, productDatabase);
        }
    }

    /**
     * This method gets the items in this ViewPager Adapter
     * @return int
     */
    @Override
    public int getItemCount() {
        return 4;
    }
}
