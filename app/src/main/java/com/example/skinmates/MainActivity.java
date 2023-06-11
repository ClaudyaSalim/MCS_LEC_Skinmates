package com.example.skinmates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView username, loadingMsg;
    Button loginBtn;
    String userId;
    User user;
    ArrayList<Product>products = new ArrayList<>();
    RecyclerView recyclerView;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        loginBtn = findViewById(R.id.btn_login);
        loadingMsg = findViewById(R.id.loading_msg);

        userId = getIntent().getStringExtra("User ID");
        Log.e("Main", userId);
        getUserById(userId);
        setValues();

        loginBtn.setOnClickListener(e->{
            Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(toLogin);
        });

    }


    // liat yg login siapa
    public void getUserById(String id){
        ArrayList<User> users = new ArrayList<>();
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
                        username.setText(user.getFirstName());
                    } else {
                        Log.d("Skinmates", "No such document");
                    }
                } else {
                    Log.d("Skinmates", "get failed with ", task.getException());
                }
            }
        });
    }


    // tampilin produk
    public void setValues(){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://makeup-api.herokuapp.com/api/v1/products.json?price_greater_than=50.0";
        JsonArrayRequest request = new JsonArrayRequest(
                url,

                new Response.Listener<JSONArray>() { // callback / semua data keterima

                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++){
                            // Parse
                            try{

                                JSONObject productJson = response.getJSONObject(i);
                                String brand, name, image, desc;
                                brand = productJson.getString("brand");
                                name = productJson.getString("name");
                                image = productJson.getString("image_link");
                                desc = productJson.getString("description");

                                Product product = new Product(brand, name, image, desc, 0);
                                products.add(product);
                                Log.i("ASD", "Product added");

                            } catch (JSONException e) {
                                Log.e("ASD", "onResponse: Parse Error");
                                e.printStackTrace();
                            }
                        }
                        // olah response nya setelah dapet
                        setProductRv();
                    }
                },

                new Response.ErrorListener() { // kalau error
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ASD", "Error while fetching data");
                        error.printStackTrace();
                    }
                }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }

    public void setProductRv(){
        loadingMsg.setText("");
        recyclerView = findViewById(R.id.product_rv);
        productAdapter = new ProductAdapter(this, products);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Log.i("Item count", String.valueOf(productAdapter.getItemCount()));
    }
}