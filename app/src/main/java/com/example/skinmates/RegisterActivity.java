package com.example.skinmates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText firstNameEt, lastNameEt, emailEt, passwordEt, confirmPassEt;
    Button loginBtn, regisBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameEt = findViewById(R.id.ETFirstName);
        lastNameEt = findViewById(R.id.ETLastName);
        emailEt = findViewById(R.id.ETemail);
        passwordEt = findViewById(R.id.ETpassword);
        // confirm pass belum ada

        regisBtn = findViewById(R.id.regisButton);
        loginBtn = findViewById(R.id.loginButton);


        // mau dihubungin biar bisa coba register
        regisBtn.setOnClickListener(e->{
            // validasi belum
            String firstName = firstNameEt.getText().toString();
            String lastName = lastNameEt.getText().toString();
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();
            registerUser(firstName, lastName, email, password);
//            onHomeClick();
        });

        loginBtn.setOnClickListener(e->{
            onLoginClick();
        });

    }

    // dari clau
    public void onHomeClick() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginClick() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    // masukin user ke firebase
    public void registerUser(String firstName, String lastName, String email, String password){

        User user = new User(firstName, lastName, email, password);

        db.collection("users").add(user);

    }
}