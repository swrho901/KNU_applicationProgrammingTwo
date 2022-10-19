package com.example.petking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ContentActivity extends AppCompatActivity {

    TextView detail_title_text, detail_content_text, detail_address_text, detail_money_text;

    TextView detail_nego_text, detail_id_text, detail_mojip_text, detail_typeofcontext_text;

    String title, content, address, id, typeOfContext, currentStat;
    int money, negotiation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        detail_title_text = findViewById(R.id.detail_title_text);
        detail_content_text = findViewById(R.id.detail_content_text);
        detail_address_text = findViewById(R.id.detail_address_text);
        detail_money_text = findViewById(R.id.detail_money_text);
        detail_typeofcontext_text = findViewById(R.id.detail_typeofcontext_text);

        detail_nego_text = findViewById(R.id.detail_nego_text);
        detail_id_text = findViewById(R.id.detail_id_text);
        detail_mojip_text = findViewById(R.id.detail_mojip_text);

        //메인엑티비티에서 받아온 데이터
        Intent intent = getIntent();

        title = intent.getExtras().getString("title");
        content = intent.getExtras().getString("content");
        address = intent.getExtras().getString("address");
        money = intent.getExtras().getInt("money");
        negotiation = intent.getExtras().getInt("negotiation");
        currentStat = intent.getExtras().getString("currentStat");


        id = intent.getExtras().getString("user_id");
        typeOfContext = intent.getExtras().getString("typeOfContext");


        detail_title_text.setText(title);
        detail_content_text.setText(content);
        detail_address_text.setText(address.substring(5)); // 대한민국 짤림
        detail_money_text.setText(String.valueOf(money) + " KRW");
        detail_id_text.setText(id + " 님이 작성하신 글입니다");
        detail_typeofcontext_text.setText(typeOfContext);
        if(negotiation == 0){
            detail_nego_text.setText("네고 불가능");
        }
        else
            detail_nego_text.setText("네고 가능");

    }
}