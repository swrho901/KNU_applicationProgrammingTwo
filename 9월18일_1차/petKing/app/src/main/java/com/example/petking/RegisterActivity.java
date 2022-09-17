package com.example.petking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            finish();
            return;
        }

        Button btnRegister = findViewById(R.id.reg_submit);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

       // TextView textViewSwitchToLogin - findViewById(R.id.)

        Button btnRegToLog = findViewById(R.id.reg_regtoLog);
        btnRegToLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToLogin();
            }
        });

    }

    private void registerUser(){
        EditText etName = findViewById(R.id.reg_name);
        EditText etEmail = findViewById(R.id.reg_email);
        EditText etAge = findViewById(R.id.reg_age);
        EditText etPwd = findViewById(R.id.reg_pwd);
        EditText etSex = findViewById(R.id.reg_sex);

        String Name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String pwd = etPwd.getText().toString();
        String age = etAge.getText().toString();
        String sex = etSex.getText().toString();

        if(Name.isEmpty() || email.isEmpty() || pwd.isEmpty() || age.isEmpty() || sex.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(Name, email, age, sex);
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            showMainActivity();
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "Authentication failed",
                            Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    private void showMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void switchToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}