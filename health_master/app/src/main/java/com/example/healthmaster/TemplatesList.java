package com.example.healthmaster;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.healthmaster.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TemplatesList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.templates_list);
        ListView tempList = (ListView) findViewById(R.id.templates_list);

        List<String> list = new ArrayList<>();
        for (Task task : ParentSession.getInstance(getApplicationContext()).getTaskTemplates()) {
            list.add(task.getName());
        }

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, R.layout.list_view_whatever, list);

        tempList.setAdapter(arrayAdapter);
    }
}
