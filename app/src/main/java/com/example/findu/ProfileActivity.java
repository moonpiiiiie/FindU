package com.example.findu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;
    RecyclerView profileRecyclerView;
    Button btnSignOut;
    Button btnEditProfile;
    Button btnChangePassword;
    TextView userName;
    TextView userEmail;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String name;
    String email;
    ImageView imageName, imageEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        userName = findViewById(R.id.displayName);
        userEmail = findViewById(R.id.displayEmail);
        imageName = findViewById(R.id.imageName);
        imageEmail = findViewById(R.id.imageEmail);
        btnSignOut = findViewById(R.id.btnSigOut);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);


        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                name = profile.getDisplayName();
                email = profile.getEmail();
                userName.setText(name);
                userEmail.setText(email);
            }
        }


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                signOutUser();


            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });



        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.profile_nav_button);
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.map_nav_button:
                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.post_nav_button:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.profile_nav_button:
                    return true;
            }
            return false;
        });
       /**
        *
        profileRecyclerView = findViewById(R.id.recyclerview_profile);

        String[] items = {"username", "settings"};
        ProfileAdapter profileAdapter = new ProfileAdapter(this, items);
        profileRecyclerView.setAdapter(profileAdapter);
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
        **/

//    public void addOne(View v) {
//        EditText label = findViewById(R.id.add_label);
//        String x = label.getText().toString();
//        Map<String, Object> d = new HashMap<>();
//        d.put("testKey", x);
//        FirestoreAPI.writePost(d);
//    }
    }

    private void signOutUser() {
        //Intent mainActivity = new Intent(ProfileActivity.this, MainActivity.class);
        //mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //startActivity(mainActivity);
        Intent intent = new Intent(ProfileActivity.this, EmailPasswordActivity.class);
        startActivity(intent);
        finish();
    }
}