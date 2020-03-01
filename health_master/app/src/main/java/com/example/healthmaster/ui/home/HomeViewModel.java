package com.example.healthmaster.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hello!\nClick the '+' button to create new task!");

    }

    public LiveData<String> getText() {
        return mText;
    }
}