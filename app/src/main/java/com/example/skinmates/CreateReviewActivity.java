package com.example.skinmates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class CreateReviewActivity extends AppCompatActivity {

    String productId, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);

        productId = getIntent().getStringExtra("Product ID");
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        userId = sharedPreferences.getString("UserID", null);



    }

    public void onBackClick(View view) {
        Intent intent = new Intent(CreateReviewActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // back arrow
    public boolean onOptionsItemSelected(MenuItem item){
        Intent toGames = new Intent(this, MainActivity.class);
        startActivityForResult(toGames, 0);
        finish();
        return true;
    }
}