package com.example.findu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {
    private EditText profileName;
    private Button saveProfileName;
    private Button cancelProfileName;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileName = findViewById(R.id.editProfileName);
        saveProfileName = findViewById(R.id.btnSaveEditProfile);
        cancelProfileName = findViewById(R.id.btnCancelEditProfile);

        auth = FirebaseAuth.getInstance();

        saveProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtName = profileName.getText().toString();
                user = auth.getCurrentUser();

                if (!txtName.isEmpty()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(txtName)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditProfileActivity.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(EditProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        cancelProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}