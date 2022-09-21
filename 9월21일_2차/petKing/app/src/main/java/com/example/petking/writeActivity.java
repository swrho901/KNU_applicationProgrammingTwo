package com.example.petking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class writeActivity extends AppCompatActivity {

    Button btn_add;
    EditText writeTitle;
    EditText writeContents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        //메인 화면으로 전환
        ImageButton btnmanager=(ImageButton)findViewById(R.id.btnmanager);
        btnmanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outIntent=new Intent(getApplicationContext(), MainActivity.class);
                setResult(RESULT_OK,outIntent);
                finish();
            }
        });

        // 제목 가져오기
        writeTitle=(EditText)findViewById(R.id.writeTitle);

        // 내용 가져오기
        writeContents=(EditText)findViewById(R.id.writeContents);

        // 등록
        btn_add= (Button)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(writeActivity.this, "클릭", Toast.LENGTH_SHORT).show();
            }
        });

    }

}

