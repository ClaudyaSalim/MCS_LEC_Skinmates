package com.example.skinmates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView username;
    Button loginBtn;
    String userId;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        loginBtn = findViewById(R.id.btn_login);

        userId = getIntent().getStringExtra("User ID");
        Log.e("Main", userId);
        getUserById(userId);

        loginBtn.setOnClickListener(e->{
            Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(toLogin);
        });

    }


    // dari clau
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
//                        Log.e("User object", String.valueOf(document.getData()));
//                        Log.e("User ID", user.getId());
//                        if(user.getId()!=null){
//
//                        }
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