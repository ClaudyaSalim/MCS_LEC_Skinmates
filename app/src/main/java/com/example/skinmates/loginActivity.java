package com.example.skinmates;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class loginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void onHomeClick(View view) {
        Intent intent = new Intent(loginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void onRegisterClick(View view) {
        Intent intent = new Intent(loginActivity.this, registerActivity.class);
        startActivity(intent);
        finish();
    }
}