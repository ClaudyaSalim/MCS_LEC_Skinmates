package com.example.skinmates.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinmates.ProductDetailActivity;
import com.example.skinmates.R;
import com.example.skinmates.model.Product;
import com.example.skinmates.model.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView brand, productName, productRating, reviewsQty;
        // jumlah reviews otw

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.product_pic);
            brand = itemView.findViewById(R.id.product_brand);
            productName = itemView.findViewById(R.id.product_name);
            productRating = itemView.findViewById(R.id.product_rating);
            reviewsQty = itemView.findViewById(R.id.qty_reviews);

            itemView.setOnClickListener(e->{
                Intent toDetail = new Intent(context, ProductDetailActivity.class);
                toDetail.putExtra("Product ID", products.get(getAdapterPosition()).getId());
                context.startActivity(toDetail);
            });

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.card_product, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Product product = products.get(position);
        Picasso.get().load(product.getImageLink()).error(R.drawable.logo_skinmates).into(holder.image);
        holder.brand.setText(product.getBrand());
        holder.productName.setText(product.getName());
        holder.productRating.setText(String.valueOf(product.getRating()));
        getReviewsByProductID(db, product.getId(), holder, position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setFilter(ArrayList<Product> filterModel) {
        products = new ArrayList<>();
        products.addAll(filterModel);
        notifyDataSetChanged();
    }

    public void getReviewsByProductID(FirebaseFirestore db, String productId, ViewHolder holder, int position){
        ArrayList reviews = new ArrayList<>();
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
                            holder.reviewsQty.setText(String.valueOf(reviews.size()) + " reviews");
                        } else {
                            Log.d("Skinmates", "get failed with ", task.getException());
                        }
                    }
                });
    }
}
