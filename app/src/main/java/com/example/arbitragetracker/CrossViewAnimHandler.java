package com.example.arbitragetracker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * This class represents the CrossViewAnimHandler which will handle animation data bewteen fragments
 */
public class CrossViewAnimHandler extends ViewModel {

    //Property
    private MutableLiveData<Boolean> animationsEnabled = new MutableLiveData<>();

    //Getter and Setter
    public LiveData<Boolean> getAnimationsEnabled() {
        return animationsEnabled;
    }

    public void setAnimationsEnabled(boolean enabled) {
        animationsEnabled.setValue(enabled);
    }
}
