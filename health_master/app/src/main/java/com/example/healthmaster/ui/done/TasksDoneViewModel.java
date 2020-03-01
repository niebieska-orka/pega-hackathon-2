package com.example.healthmaster.ui.done;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthmaster.ParentSession;
import com.example.healthmaster.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksDoneViewModel extends ViewModel {

    private MutableLiveData<List<String>> mList;

    public TasksDoneViewModel() {
        mList = new MutableLiveData<>();
        List<String> list = new ArrayList<>();
        for (Task task : ParentSession.getInstance(null).getDoneTasks()) {
            list.add(task.getName());
        }
        mList.setValue(list);
    }

    public LiveData<List<String>> getList() {
        return mList;
    }
}