package com.example.healthmaster;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthmaster.model.Task;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;

public class ConfirmTask extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Task task = (Task) getIntent().getExtras().get("task");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_task);
        TextView name = (TextView) findViewById(R.id.confirm_name);
        ImageView img = (ImageView) findViewById(R.id.confirm_img);
        TextView desc = (TextView) findViewById(R.id.confirm_desc);
        Button yesButt = (Button) findViewById(R.id.confirm_yes_butt);
        Button noButt = (Button) findViewById(R.id.button);
        if (task.getContent() != null) {
            img.setImageBitmap(BitmapFactory.decodeByteArray(task.getContent(), 0, task.getContent().length));
        }

        name.setText(task.getName());
        desc.setText(task.getDescription());
        yesButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ParentSession.getInstance(getApplicationContext()).setTaskStatus(task.getId(), true);
                    ConfirmTask.this.finish();
                } catch (JSONException | MqttException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        noButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ParentSession.getInstance(getApplicationContext()).setTaskStatus(task.getId(), false);
                    ConfirmTask.this.finish();
                } catch (JSONException | MqttException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
