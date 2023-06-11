package com.example.skinmates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    Button loginBtn, regisBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regisBtn = findViewById(R.id.register_button);
        loginBtn = findViewById(R.id.login_button);

        // mau dihubungin biar bisa coba register
        regisBtn.setOnClickListener(e->{
            onHomeClick();
        });

        loginBtn.setOnClickListener(e->{
            onLoginClick();
        });

    }

    // test - clau
    public void onHomeClick() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginClick() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}