package com.example.skinmates;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn, regisBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        regisBtn = findViewById(R.id.register_button);
        loginBtn = findViewById(R.id.login_button);

        // mau dihubungin biar bisa coba login
        loginBtn.setOnClickListener(e->{
            onHomeClick();
        });

        regisBtn.setOnClickListener(e->{
            onRegisterClick();
        });

    }
    public void onHomeClick(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
//    test
    public void onRegisterClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    // test - clau
    public void onHomeClick() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onRegisterClick() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}