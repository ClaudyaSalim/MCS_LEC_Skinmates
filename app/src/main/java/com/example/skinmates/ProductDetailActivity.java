package com.example.skinmates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skinmates.model.Product;
import com.example.skinmates.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String productId;
    Product product;
    TextView productName, brand, productRating, productDesc;
    ImageView productImg;
    ImageButton seeReviews;
    ExtendedFloatingActionButton reviewBtn;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productId = getIntent().getStringExtra("Product ID");

        // appbar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Product Detail");
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);

        productName = findViewById(R.id.product_name);
        productImg = findViewById(R.id.product_img);
        brand = findViewById(R.id.product_brand);
        productRating = findViewById(R.id.product_rating);
        productDesc = findViewById(R.id.product_desc);
        seeReviews = findViewById(R.id.see_reviews_btn);
        reviewBtn = findViewById(R.id.review_btn);

        seeReviews.setOnClickListener(e->{
            // intent to review list class
//            Intent toCreate = new Intent(this, CreateReviewActivity.class);
//            toCreate.putExtra("Product ID", productId);
//            startActivity(toCreate);
        });

        reviewBtn.setOnClickListener(e->{
            Intent toCreate = new Intent(this, CreateReviewActivity.class);
            toCreate.putExtra("Product ID", productId);
            startActivity(toCreate);
        });

        getProductbByID(productId);

    }

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
                                setValues(product);
                            } else {
                                Log.d("Skinmates", "No such document");
                            }
                        } else {
                            Log.d("Skinmates", "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void setValues(Product product){
        productName.setText(product.getName());
        Picasso.get().load(product.getImageLink()).error(R.drawable.logo_skinmates).into(productImg);
        brand.setText(product.getBrand());
        productRating.setText(String.valueOf(product.getRating()));
        productDesc.setText(product.getDescription());
    }

    // back arrow
    public boolean onOptionsItemSelected(MenuItem item){
        Intent toGames = new Intent(this, MainActivity.class);
        startActivityForResult(toGames, 0);
        finish();
        return true;
    }
}