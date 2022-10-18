package com.example.petking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ContentActivity extends AppCompatActivity {

    TextView detail_title_text, detail_content_text, detail_address_text, detail_money_text;

    String title, content, address, money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        detail_title_text = findViewById(R.id.detail_title_text);
        detail_content_text = findViewById(R.id.detail_content_text);
        detail_address_text = findViewById(R.id.detail_address_text);
        detail_money_text = findViewById(R.id.detail_money_text);

        //메인엑티비티에서 받아온 데이터
        Intent intent = getIntent();

        title = intent.getExtras().getString("title");
        content = intent.getExtras().getString("content");
        address = intent.getExtras().getString("address");
        money = intent.getExtras().getString("money");

        detail_title_text.setText(title);
        detail_content_text.setText(content);
        detail_address_text.setText(address);
        detail_money_text.setText(money);
    }
}
