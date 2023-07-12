package com.example.skinmates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.skinmates.model.Product;
import com.example.skinmates.model.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class CreateReviewActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String productId, userId;
    TextView productName;
    EditText ratingEt, descEt;
    Button postBtn;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Review This");
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);

        productId = getIntent().getStringExtra("Product ID");
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        userId = sharedPreferences.getString("UserID", null);

        productName = findViewById(R.id.product_name);
        ratingEt = findViewById(R.id.editTextRating);
        descEt = findViewById(R.id.editTextReview);
        postBtn = findViewById(R.id.postreviewbtn);

        getProductbByID(productId);

        postBtn.setOnClickListener(e->{
            if(product==null){
                Toast.makeText(this, "Please wait a while!", Toast.LENGTH_SHORT).show();
                return;
            }
            String rate = ratingEt.getText().toString();
            String desc = descEt.getText().toString();
            if(rate.equals("") || desc.equals("")){
                Toast.makeText(this, "Field should not be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            Double rating = Double.parseDouble(rate);
            if(rating>5.0){
                Toast.makeText(this, "Rating should be from 0 to 5", Toast.LENGTH_SHORT).show();
                return;
            }
            Review review = new Review(productId, userId, desc, rating);
            insertReview(review);
            Intent intent = new Intent(CreateReviewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        });

    }

    // ambil dari firebase
    public void getProductbByID(String id){
        ArrayList<Product> products = new ArrayList<>();
        db.collection("products").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists() && !document.getData().isEmpty()) {
                                product = document.toObject(Product.class);
                                product.setId(document.getId());
                                productName.setText(product.getName());
                            } else {
                                Log.d("Skinmates", "No such document");
                            }
                        } else {
                            Log.d("Skinmates", "get failed with ", task.getException());
                        }
                    }
                });
    }

    // masukin ke firebase
    public void insertReview(Review review){
        db.collection("reviews").add(review);
    }

    // back arrow
    public boolean onOptionsItemSelected(MenuItem item){
        Intent toDetail = new Intent(this, ProductDetailActivity.class);
        toDetail.putExtra("Product ID", productId);
        startActivityForResult(toDetail, 0);
        finish();
        return true;
    }
}