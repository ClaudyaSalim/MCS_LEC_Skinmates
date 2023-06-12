package com.example.skinmates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.skinmates.adapter.ProductAdapter;
import com.example.skinmates.model.Product;
import com.example.skinmates.model.User;
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
        getAllProduct();

        // search
        SearchView searchView = findViewById(R.id.search_bar);
        search(searchView);

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
        String url = "https://makeup-api.herokuapp.com/api/v1/products.json?rating_greater_than=4.2&rating_less_than=4.5";
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
                                double rating;
                                brand = productJson.getString("brand");
                                name = productJson.getString("name");
                                image = productJson.getString("image_link");
                                desc = productJson.getString("description");
                                rating = productJson.getDouble("rating");

                                Product product = new Product(brand, name, image, desc, rating);
                                products.add(product);
                                insertProduct(product);
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

    // masukin produk ke firebase
    public void insertProduct(Product product){

        db.collection("products").add(product);

    }

    public void getAllProduct(){
        db.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().isEmpty()){
                        setValues();
                        return;
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Product product = document.toObject(Product.class);
                        product.setId(document.getId());
                        Log.e("Document", product.getId());
                        products.add(product);
                    }
                    setProductRv();
                } else {
                    Log.e("Skinmates", "Error getting documents.", task.getException());
                }
            }
        });
    }

    public void search(SearchView searchView){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Product> productFilter = new ArrayList<>();
                for(Product product:products){
                    String nama = product.getName().toLowerCase();
                    if (nama.contains(newText)){
                        productFilter.add(product);
                    }
                }
                productAdapter.setFilter(productFilter);
                return true;
            }

        });

    }
}