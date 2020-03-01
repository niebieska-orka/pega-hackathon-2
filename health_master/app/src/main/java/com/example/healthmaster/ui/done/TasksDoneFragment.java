package com.example.healthmaster.ui.done;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.healthmaster.ConfirmTask;
import com.example.healthmaster.ParentSession;
import com.example.healthmaster.R;

import java.util.List;

public class TasksDoneFragment extends Fragment {

    private TasksDoneViewModel tasksDoneViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksDoneViewModel =
                ViewModelProviders.of(this).get(TasksDoneViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks_done, container, false);
        final ListView listView = root.findViewById(R.id.tdone_list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ConfirmTask.class);
                i.putExtra("task", ParentSession.getInstance(getContext()).getDoneTasks().get(position));
                startActivity(i);
            }
        });
        tasksDoneViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> list) {
                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<>(getActivity(), R.layout.list_view_whatever, list);

                listView.setAdapter(arrayAdapter);
            }
        });
        return root;
    }
}
