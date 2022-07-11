package com.example.findu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    TextView txtSignIn;
    EditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    ProgressBar progressBar;
    Button btnSignUp;
    String txtName, txtEmail, txtPassword, txtConfirmPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        txtSignIn = findViewById(R.id.txtSignIn);
        edtName = findViewById(R.id.edtSignUpName);
        edtEmail = findViewById(R.id.edtSignUpEmail);
        edtPassword = findViewById(R.id.edtSignUpPassword);
        edtConfirmPassword = findViewById(R.id.edtSignUpConfirmPassword);
        progressBar = findViewById(R.id.signUpProgressBar);
        btnSignUp = findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, EmailPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtName = edtName.getText().toString();
                txtEmail = edtEmail.getText().toString();
                txtPassword = edtPassword.getText().toString();
                txtConfirmPassword = edtConfirmPassword.getText().toString();

                if (!TextUtils.isEmpty(txtEmail)) {
                    if (txtEmail.matches(emailPattern)) {
                        if (!TextUtils.isEmpty(txtPassword)) {
                            if (!TextUtils.isEmpty(txtConfirmPassword)) {
                                if (txtConfirmPassword.equals(txtPassword)) {
                                    SignUpUser();
                                } else {
                                    edtConfirmPassword.setError("Confirm Password and Password should be same");
                                }
                            } else {
                                edtConfirmPassword.setError("Confirm Password field can't be empty");
                            }
                        } else {
                            edtPassword.setError("Password field can't be empty");
                        }
                    } else {
                        edtEmail.setError("Enter a valid Email Address");
                    }
                } else {
                    edtEmail.setError("Email field can't be empty");
                }
            }
        });
    }

    private void SignUpUser() {
        progressBar.setVisibility(View.VISIBLE);
        btnSignUp.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                } else {
                    userId = mAuth.getCurrentUser().getUid();
                    DocumentReference documentRef = db.collection("users").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("username", txtName);
                    user.put("email", txtEmail);
                    documentRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Log.d("signupUser", "on success, userName" + userId);
                        }
                    });
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });



    }

}