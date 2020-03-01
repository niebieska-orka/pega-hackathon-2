package com.example.healthmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.healthmaster.model.Task;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class TemplatesList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.templates_list);
        final ListView tempList = (ListView) findViewById(R.id.templates_list);

        List<String> list = new ArrayList<>();
        for (Task task : ParentSession.getInstance(getApplicationContext()).getTaskTemplates()) {
            list.add(task.getName());
        }

        tempList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), CreateTaskFromTemplate.class);
                Bundle options = new Bundle();
                options.putString("task_id", ParentSession.getInstance(view.getContext()).getTaskTemplates().get(position).getId());
                i.putExtras(options);
                startActivity(i);
            }
        });



        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, R.layout.list_view_whatever, list);

        tempList.setAdapter(arrayAdapter);
    }
}
