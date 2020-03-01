package com.example.healthmaster;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.healthmaster.model.Status;
import com.example.healthmaster.model.Task;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateCustomTask extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        Button create = (Button) findViewById(R.id.create_task);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.task_name);
                EditText desc = (EditText) findViewById(R.id.task_desc);
                EditText deadline = (EditText) findViewById(R.id.task_deadline);
                EditText xp = (EditText) (EditText) findViewById(R.id.task_xp);

                Task task = new Task(String.valueOf(deadline.getText()) + (String.valueOf(name.getText()) ), Timestamp.valueOf(deadline.getText().toString()+" 00:00:01"), name.getText().toString(),
                        desc.getText().toString(), Integer.parseInt(xp.getText().toString()));
                task.setStatus(Status.TO_DO);
                task.setId(String.valueOf(System.nanoTime()));

                try {
                    ParentSession.getInstance(getApplicationContext()).addTaskForChild(task);
                } catch (MqttException | JSONException e) {
                    throw new RuntimeException(e);
                }
                finish();
            }
        });


    }
}
