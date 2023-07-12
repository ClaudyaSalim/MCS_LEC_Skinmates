package com.example.skinmates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.skinmates.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText emailEt, passwordEt;
    Button loginBtn, regisBtn;
    ArrayList<User>userArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.emailFormField);
        passwordEt = findViewById(R.id.passwordFormField);

        regisBtn = findViewById(R.id.registerPageButton);
        loginBtn = findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(e->{
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();
            getUserByEmail(email, password, userArrayList);
        });

        regisBtn.setOnClickListener(e->{
            onRegisterClick();
        });

    }
    public void onHomeClick(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void onRegisterClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHomeClick(User user) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserID", user.getId());
        editor.apply();
        startActivity(intent);
        finish();
    }

    public void onRegisterClick() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void getUserByEmail(String email, String password, ArrayList<User> userArrayList){

        db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){
                                Toast.makeText(LoginActivity.this, "Email or password is incorrect!", Toast.LENGTH_SHORT).show();
                               return;
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                user.setId(document.getId());
                                Log.e("Document", user.getId());
                                userArrayList.add(user);
                            }
                            User user = userArrayList.get(0);
                            Log.e("User to home", user.getFirstName());
                            onHomeClick(user);
                            Toast.makeText(LoginActivity.this, "You're logged in!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Skinmates", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

}