package com.example.petking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentActivity extends AppCompatActivity {

    TextView detail_title_text, detail_title_text2, detail_content_text, detail_address_text, detail_money_text;

    TextView detail_nego_text, detail_id_text, detail_mojip_text, detail_typeofcontext_text;

    ImageView imgOfAni;

    String title, content, address, id, typeOfContext, currentStat;
    int money, negotiation;

    Button chatMoveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        chatMoveBtn = findViewById(R.id.chatMoveBtn);
        imgOfAni = findViewById(R.id.imgOfAni);

        detail_title_text = findViewById(R.id.detail_title_text);
        detail_title_text2 = findViewById(R.id.detail_title_text2);
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

        Bitmap animalhello;// = (Bitmap) intent.getParcelableExtra("image");

        /*byte[] arr = getIntent().getByteArrayExtra("image");
        animalhello = BitmapFactory.decodeByteArray(arr, 0, arr.length);


        imgOfAni.setImageBitmap(animalhello);*/

        id = intent.getExtras().getString("user_id");
        typeOfContext = intent.getExtras().getString("typeOfContext");


        detail_title_text.setText(title);
        detail_title_text2.setText(title);
        detail_content_text.setText(content);
        detail_address_text.setText(address.substring(5)); // 대한민국 짤림
        detail_money_text.setText(String.valueOf(money) + "원");
        detail_id_text.setText(id);
        detail_typeofcontext_text.setText(typeOfContext);
        if(negotiation == 0){
            detail_nego_text.setText("가격제안 불가");
        }
        else
            detail_nego_text.setText("가격제안 가능");

        chatMoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ContentActivity.this, ChatActivity.class);
                it.putExtra("email", id);
                startActivity(it);

            }
        });

    }
}