package com.example.helathhero;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_task);
        TextView header = (TextView)findViewById(R.id.task_header);
        TextView completing_xp = (TextView)findViewById(R.id.task_completing_xp);
        TextView xp_to_next_lvl = (TextView)findViewById(R.id.task_to_next_lvl);
        TextView description = (TextView)findViewById(R.id.task_description);

        header.setText(new String("Recarrotnation"));
        completing_xp.setText(new String("200"));
        xp_to_next_lvl.setText(new String("200"));
        description.setText(new String("Three carrots a day keep all the goblins away. You will need to eat them all to protect Honneywood"));

    }
}
