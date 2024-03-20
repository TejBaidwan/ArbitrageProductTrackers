package com.example.arbitragetracker.statistics;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.arbitragetracker.ProductDatabase;
import com.example.arbitragetracker.R;

public class CustomViewPagerAdapter extends FragmentStateAdapter {
    private ProductDatabase productDatabase;

    public CustomViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ProductDatabase productDatabase) {
        super(fragmentActivity);
        this.productDatabase = productDatabase;
    }

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
                return StatisticsFragment.newInstance(R.string.app_name, R.drawable.appiconimage, 404, productDatabase);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
