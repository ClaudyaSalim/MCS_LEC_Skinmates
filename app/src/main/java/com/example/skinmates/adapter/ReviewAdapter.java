package com.example.skinmates.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinmates.R;
import com.example.skinmates.model.Product;
import com.example.skinmates.model.Review;
import com.example.skinmates.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Review>reviews;
    private String layout;

    public ReviewAdapter(Context context, ArrayList<Review> reviews, String layout) {
        this.context = context;
        this.reviews = reviews;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.card_review, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(layout.equals("User Reviews")){
            getUserByID(db, reviews.get(position).getUserId(), holder, position);
        } else if (layout.equals("Product Reviews")) {
            getProductByID(db, reviews.get(position).getProductId(), holder, position);
        }

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView heading, rating, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            heading = itemView.findViewById(R.id.heading);
            rating = itemView.findViewById(R.id.rating);
            description = itemView.findViewById(R.id.description);

        }
    }

    public void getUserByID(FirebaseFirestore db, String id, ViewHolder holder, int position){
        db.collection("users").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists() && !document.getData().isEmpty()) {
                                User user = document.toObject(User.class);
                                String username = user.getFirstName() + " " + user.getLastName();
                                holder.heading.setText(username);
                                holder.rating.setText(String.valueOf(reviews.get(position).getRating()));
                                holder.description.setText(reviews.get(position).getDescription());
                            } else {
                                Log.d("Skinmates", "No such document");
                            }
                        } else {
                            Log.d("Skinmates", "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void getProductByID(FirebaseFirestore db, String id, ViewHolder holder, int position){
        db.collection("products").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists() && !document.getData().isEmpty()) {
                                Product product = document.toObject(Product.class);
                                product.setId(document.getId());
                                String productName = product.getName();
                                holder.heading.setText(productName);
                                holder.rating.setText(String.valueOf(reviews.get(position).getRating()));
                                holder.description.setText(reviews.get(position).getDescription());
                            } else {
                                Log.d("Skinmates", "No such document");
                            }
                        } else {
                            Log.d("Skinmates", "get failed with ", task.getException());
                        }
                    }
                });
    }

}
