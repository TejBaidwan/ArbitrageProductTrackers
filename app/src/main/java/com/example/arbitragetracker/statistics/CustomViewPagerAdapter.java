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
 * @author Tej Baidwan
 */
public class CustomViewPagerAdapter extends FragmentStateAdapter {
    //Database object
    private ProductDatabase productDatabase;
    private Context context;

    //Constructor
    public CustomViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ProductDatabase productDatabase, Context context) {
        super(fragmentActivity);
        this.productDatabase = productDatabase;
        this.context = context;
    }

    /**
     * This method gets the position the app is in in the ViewPager and populates it with parameters via newInstance()
     * @param position - The position in the ViewPager
     * @return Fragment
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String selectedCurrency = CurrencyUtil.getSelectedCurrency(context);
        switch (position) {
            case 0:
                return StatisticsFragment.newInstance(R.string.statOne, R.drawable.appiconimage, String.valueOf(productDatabase.getTotalItemCount()), productDatabase);
            case 1:
                String totalWithSymbol = CurrencyUtil.formatPriceWithCurrencySymbol(productDatabase.getTotalPrice(), selectedCurrency);
                return StatisticsFragment.newInstance(R.string.statTwo, R.drawable.appiconimage, totalWithSymbol, productDatabase);
            case 2:
                String highestPriceWithSymbol = CurrencyUtil.formatPriceWithCurrencySymbol(productDatabase.getHighestPrice(), selectedCurrency);
                return StatisticsFragment.newInstance(R.string.statThree, R.drawable.appiconimage, highestPriceWithSymbol, productDatabase);
            case 3:
                String lowestPriceWithSymbol = CurrencyUtil.formatPriceWithCurrencySymbol(productDatabase.getLowestPrice(), selectedCurrency);
                return StatisticsFragment.newInstance(R.string.statFour, R.drawable.appiconimage, lowestPriceWithSymbol, productDatabase);
            case 4:
                String averagePriceWithSymbol = CurrencyUtil.formatPriceWithCurrencySymbol(productDatabase.getAveragePrice(), selectedCurrency);
                return StatisticsFragment.newInstance(R.string.statFive, R.drawable.appiconimage, averagePriceWithSymbol, productDatabase);
            case 5:
                return StatisticsFragment.newInstance(R.string.statSix, R.drawable.appiconimage,
                        productDatabase.getNumberOfActiveListings() + "/" + productDatabase.getTotalItemCount(), productDatabase);
            case 6:
                return StatisticsFragment.newInstance(R.string.statSeven, R.drawable.appiconimage,
                        productDatabase.getNumberOfSoldListings() + "/" + productDatabase.getTotalItemCount(), productDatabase);
            default:
                return StatisticsFragment.newInstance(R.string.noItems, R.drawable.appiconimage, String.valueOf(R.string.noItemValue), productDatabase);
        }
    }

    /**
     * This method gets the items in this ViewPager Adapter
     * @return int
     */
    @Override
    public int getItemCount() {
        return 7;
    }
}
