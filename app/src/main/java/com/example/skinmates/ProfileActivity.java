package com.example.skinmates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.skinmates.adapter.ReviewAdapter;
import com.example.skinmates.model.Review;
import com.example.skinmates.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView username, email, qty;
    String userId;
    User user;
    ArrayList<Review>reviews;
    ReviewAdapter reviewAdapter;
    RecyclerView reviewRv;
    Button signout;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        qty = findViewById(R.id.qtyreview);
        signout = findViewById(R.id.signoutbtn);
        text = findViewById(R.id.text);
        reviewRv = findViewById(R.id.review_rv_product);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);

        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        userId = sharedPreferences.getString("UserID", null);
        Log.e("Main", userId);
        getUserById(userId);
        getReviewsByUserID(userId);

        signout.setOnClickListener(e->{
            onOutClick();
        });

    }
    public void onBackkClick(View view) {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onOutClick() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent toHome = new Intent(this, MainActivity.class);
        startActivityForResult(toHome, 0);
        finish();
        return true;
    }

    public void getUserById(String id){
        db.collection("users").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists() && !document.getData().isEmpty()) {
                                user = document.toObject(User.class);
                                user.setId(document.getId());
                                username.setText(user.getFirstName() + " " + user.getLastName() + "!");
                                email.setText(user.getEmail());

                            } else {
                                Log.d("Skinmates", "No such document");
                            }
                        } else {
                            Log.d("Skinmates", "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void getReviewsByUserID(String userId){
        reviews = new ArrayList<>();
        db.collection("reviews")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Review review = document.toObject(Review.class);
                                reviews.add(review);
                            }
                            qty.setText(String.valueOf(reviews.size()));
                            setReviewsRv();
                        } else {
                            Log.d("Skinmates", "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void setReviewsRv(){
        reviewAdapter = new ReviewAdapter(this, reviews, "Product Reviews");
        reviewRv.setAdapter(reviewAdapter);
        reviewRv.setLayoutManager(new LinearLayoutManager(this));
        if(reviewAdapter.getItemCount()!=0){
            text.setText("");
        }
    }
}