package com.example.helathhero.ui.my_tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyTasksViewModel extends ViewModel {

    private MutableLiveData<List<String>> mList;

    public MyTasksViewModel() {
        mList = new MutableLiveData<>();
        List<String> list = new ArrayList<>();
        list.add(new String("banany"));
        list.add(new String("japka"));
        mList.setValue(list);
    }

    public LiveData<List<String>> getList() {
        return mList;
    }
}