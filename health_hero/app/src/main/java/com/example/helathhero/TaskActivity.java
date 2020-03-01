package com.example.helathhero;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.helathhero.model.Status;
import com.example.helathhero.model.Task;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;

public class TaskActivity extends Activity {

    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskId = getIntent().getExtras().getString("task_id");
        final Task task = ChildSession.getInstance(getApplicationContext()).getTask(taskId);
        setContentView(R.layout.fragment_task);
        TextView header = (TextView) findViewById(R.id.task_header);
        TextView completing_xp = (TextView) findViewById(R.id.task_completing_xp);
        TextView xp_to_next_lvl = (TextView) findViewById(R.id.task_to_next_lvl);
        TextView description = (TextView) findViewById(R.id.task_description);
        Button completeTaskButton = (Button) findViewById(R.id.button);
        completeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 1337);
                task.setStatus(Status.TO_CONFIRM);
                finish();
            }
        });

        header.setText(task.getName());
        completing_xp.setText(String.valueOf(task.getXp()));
        xp_to_next_lvl.setText(new String("100"));
        description.setText(task.getDescription());

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1337) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            try {
                ChildSession.getInstance(getApplicationContext()).progressTask(taskId, stream.toByteArray());
            } catch (JSONException | MqttException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
