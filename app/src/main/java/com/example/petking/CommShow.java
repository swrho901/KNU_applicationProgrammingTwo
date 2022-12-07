package com.example.petking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CommShow extends AppCompatActivity {

    TextView comm_show_title;
    TextView comm_show_title_2;
    TextView comm_show_content;
    TextView comm_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_show);

        comm_show_title_2 = (TextView) findViewById(R.id.comm_show_title_2);
        comm_show_title = (TextView) findViewById(R.id.comm_show_title);
        comm_show_content = findViewById(R.id.comm_show_content);
        comm_id = findViewById(R.id.comm_show_id);
        Intent intent = getIntent();

        String title = intent.getExtras().getString("comm_title");
        String content = intent.getExtras().getString("comm_content");
        String id = intent.getExtras().getString("comm_id");


        comm_show_title_2.setText(title);
        comm_show_title.setText(title);
        comm_show_content.setText(content);
        comm_id.setText(id);

    }

}