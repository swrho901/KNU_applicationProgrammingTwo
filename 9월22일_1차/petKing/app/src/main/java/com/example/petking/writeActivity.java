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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class writeActivity extends AppCompatActivity {

    Button btn_add;
    EditText writeTitle;
    EditText writeContents;
    Spinner spinner;
    private FirebaseAuth mAuth;
    public String user_ad;

    private DatabaseReference mDatabase;


    public writeActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);



/*        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            finish();
            return;
        }*/ // if 이거 있으면 글쓰기 눌렀을 때 실행 안됨
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

        // 스피너 값 가져오기
        spinner = (Spinner)findViewById(R.id.spinner);

        // 등록
        btn_add= (Button)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(writeActivity.this, "클릭", Toast.LENGTH_SHORT).show();
                uploadContents();
            }
        });

    }

    private void uploadContents() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String title = writeTitle.getText().toString();
        String cont = writeContents.getText().toString();
        String spinner_text = spinner.getSelectedItem().toString();
        //String user_address;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    user_ad = user.address;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Context context = new Context(title, spinner_text, user_ad, cont, spinner_text, 100, 0);
        mDatabase.child("context") .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(context);

    }


}

