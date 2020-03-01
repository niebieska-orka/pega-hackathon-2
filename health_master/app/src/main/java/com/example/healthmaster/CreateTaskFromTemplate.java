package com.example.healthmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthmaster.model.Status;
import com.example.healthmaster.model.Task;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;

import java.sql.Timestamp;

public class CreateTaskFromTemplate extends Activity
{
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        super.onCreate(savedInstanceState);
        taskId = getIntent().getExtras().getString("task_id");
        Task task = ParentSession.getInstance(getApplicationContext()).getTaskTemplate(taskId);
        setContentView(R.layout.create_task);

        EditText name = (EditText) findViewById(R.id.task_name);
        EditText desc = (EditText) findViewById(R.id.task_desc);
        EditText deadline = (EditText) findViewById(R.id.task_deadline);
        EditText xp = (EditText) (EditText) findViewById(R.id.task_xp);

        name.setText(new String(task.getName()));
        desc.setText(new String(task.getDescription()));
        deadline.setText(new String(String.valueOf(task.getDeadline())).split(" ")[0]);
        xp.setText(new String(String.valueOf(task.getXp())));



        Button create = (Button) findViewById(R.id.create_task);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.task_name);
                EditText desc = (EditText) findViewById(R.id.task_desc);
                EditText deadline = (EditText) findViewById(R.id.task_deadline);
                EditText xp = (EditText) (EditText) findViewById(R.id.task_xp);

                Task task = new Task(Timestamp.valueOf(deadline.getText().toString()+" 00:00:01"), name.getText().toString(),
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
