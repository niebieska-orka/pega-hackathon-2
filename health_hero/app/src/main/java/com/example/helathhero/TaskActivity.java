package com.example.helathhero;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_task);
        TextView hed = (TextView)findViewById(R.id.task_header);
        TextView des = (TextView)findViewById(R.id.task_description);

        hed.setText(new String("ala"));
        des.setText(new String("ola"));

    }
}
