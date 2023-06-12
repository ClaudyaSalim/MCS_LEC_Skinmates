package com.example.skinmates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.skinmates.adapter.ReviewAdapter;
import com.example.skinmates.model.Review;
import com.example.skinmates.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReviewListActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView reviewRv;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviews;
    String productId;
    ExtendedFloatingActionButton reviewBtn;
    ActionBar actionBar;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        productId = getIntent().getStringExtra("Product ID");
        getReviewsByProductID(productId);

        // appbar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Product Detail");
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);

        text = findViewById(R.id.text);
        reviewRv = findViewById(R.id.review_rv_product);
        reviewBtn = findViewById(R.id.review_btn);

        reviewBtn.setOnClickListener(e->{
            Intent toCreate = new Intent(this, CreateReviewActivity.class);
            toCreate.putExtra("Product ID", productId);
            startActivity(toCreate);
        });

    }

    public void getReviewsByProductID(String productId){
        reviews = new ArrayList<>();
        db.collection("reviews")
                .whereEqualTo("productId", productId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Review review = document.toObject(Review.class);
                                reviews.add(review);
                            }
                            setReviewsRv();
                        } else {
                            Log.d("Skinmates", "get failed with ", task.getException());
                        }
                    }
                });
    }


    public void setReviewsRv(){
        reviewAdapter = new ReviewAdapter(this, reviews, "User Reviews");
        reviewRv.setAdapter(reviewAdapter);
        reviewRv.setLayoutManager(new LinearLayoutManager(this));
        if(reviewAdapter.getItemCount()!=0){
            text.setText("");
        }
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