package com.example.helathhero.ui.task;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TaskViewModel extends ViewModel {
    private MutableLiveData<String> mHeader;
    private MutableLiveData<String> mDescription;

    public TaskViewModel() {
        mHeader = new MutableLiveData<>();
        mDescription = new MutableLiveData<>();

        String header = new String("task header");
        String desc = new String("task description");

        mHeader.setValue(header);
        mDescription.setValue(desc);
    }

    public LiveData<String> getHeader() {
        return mHeader;
    }
    public LiveData<String> getDescription() {
        return mDescription;
    }
}

