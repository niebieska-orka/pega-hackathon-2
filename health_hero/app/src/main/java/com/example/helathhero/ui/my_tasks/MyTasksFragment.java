package com.example.helathhero.ui.my_tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.helathhero.R;

import java.util.ArrayList;
import java.util.List;

public class MyTasksFragment extends Fragment {

    private MyTasksViewModel myTasksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myTasksViewModel =
                ViewModelProviders.of(this).get(MyTasksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_tasks, container, false);

        final ListView listView = root.findViewById(R.id.tasks_list);

        myTasksViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> list) {
                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, list);

                listView.setAdapter(arrayAdapter);
            }
        });
        return root;
    }
}
