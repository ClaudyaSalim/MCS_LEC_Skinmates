package com.example.skinmates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreateReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);
    }

    public void onBackClick(View view) {
        Intent intent = new Intent(CreateReviewActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}