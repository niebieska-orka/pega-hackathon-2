package com.example.healthmaster;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ConfirmTask extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_task);
        TextView name = (TextView)findViewById(R.id.confirm_name);
        ImageView img = (ImageView) findViewById(R.id.confirm_img);
        TextView desc = (TextView)findViewById(R.id.confirm_desc);

        name.setText(new String("testowy tekst taska testowego"));
        desc.setText(new String("testowy desc testowego taska testowego desca"));

    }
}
