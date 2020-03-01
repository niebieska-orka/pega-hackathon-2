package com.example.helathhero.ui.my_tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.helathhero.ChildSession;
import com.example.helathhero.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MyTasksViewModel extends ViewModel {

    private MutableLiveData<List<String>> mList;

    public MyTasksViewModel() {
        mList = new MutableLiveData<>();
        List<String> list = new ArrayList<>();
        for(Task task : ChildSession.getInstance(null).getTasks()) {
            list.add(task.getName());
        }
        mList.setValue(list);
    }

    public LiveData<List<String>> getList() {
        return mList;
    }
}