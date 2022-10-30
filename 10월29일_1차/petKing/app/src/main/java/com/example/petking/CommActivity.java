package com.example.petking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Date;

public class CommActivity extends AppCompatActivity {

    public String title = "";
    public String cont = "";
    public String user_id = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);

        ImageButton btnBack = findViewById(R.id.btn2);
        Button btnUpload = findViewById(R.id.btn_add2);


        Intent secondIntent = getIntent();
        ;
        user_id = secondIntent.getStringExtra("user_id");


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCommunity();
                Intent outIntent=new Intent(getApplicationContext(), MainActivity.class);
                setResult(RESULT_OK,outIntent);
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outIntent=new Intent(getApplicationContext(), MainActivity.class);
                setResult(RESULT_OK,outIntent);
                finish();
            }
        });


    }
    public void uploadCommunity(){
        // 여기 id 받아오는거 구현, community class 에 추가한 후 밑에도 넣어야함

        EditText tvTitle = findViewById(R.id.writeTitle2);
        EditText tvCont = findViewById(R.id.writeContents2);
        title = tvTitle.getText().toString();
        cont = tvCont.getText().toString();

        Community community = new Community(title, cont, 0, user_id);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Date now = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time1 = sdf1.format(now);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        mDatabase.child("community") .child(time1).setValue(community);
    }

}
