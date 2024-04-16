package com.example.arbitragetracker.statistics;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

/**
 * A ViewPager2 PageTransformer that applies a depth transformation to the pages.
 * This transformer scales and translates pages based on their position in the ViewPager2.
 * @author Tej Baidwan
 */
public class DepthPageTransformer implements ViewPager2.PageTransformer {

        //Minimum scale factor
        private static final float MIN_SCALE = 0.6f;

        /**
         * Applies the depth transformation to the given ViewPager page.
         *
         * @param page      The page .
         * @param position  The position of the page relative to the current page (<0 is the left, >0 is the right)
         */
        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageWidth = page.getWidth();

            if (position < -1) {
                page.setAlpha(0f);
            } else if (position <= 0) {
                page.setAlpha(1f);
                page.setTranslationX(0f);
                page.setScaleX(1f);
                page.setScaleY(1f);
            } else if (position <= 1) {
                page.setAlpha(1 - position);
                page.setTranslationX(pageWidth * -position);
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            } else {
                page.setAlpha(0f);
            }
        }
}
