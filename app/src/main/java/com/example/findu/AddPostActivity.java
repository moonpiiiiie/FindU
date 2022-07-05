package com.example.findu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class AddPostActivity extends AppCompatActivity {
    private EditText editText_name;
    private EditText editText_age;
    private EditText editText_notes;
    private Spinner spinner_gender;
    private AddPost.PostDialogListener postDialogListener;
    private Button button_save;
    private Button button_cancel;

    private Button button_uploadPhoto;

    private ImageView post_photo;
    ActivityResultLauncher<String> selectPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        ActivityResultLauncher<Intent> galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            post_photo.setImageURI(data.getData());
                        }
                    }
                });

        // post photo
        post_photo = findViewById(R.id.post_photo);
        button_uploadPhoto = findViewById(R.id.button_uploadPhoto);
        button_uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryResultLauncher.launch(gallery);




//                boolean pick = true;
//                if (pick) {
//                    if (!checkCameraPermission()) {
//                        requestCameraPermission();
//                    } else
//                    {
//                        pickImage();
//                    }
//                } else {
//                    if (!checkGalleryPermission()) {
//                        requestGalleryPermission();
//                    } else
//                    {
//                        pickImage();
//                    }
//                }
            }
        });

        // post text
        editText_name = findViewById(R.id.edittext_name);
        editText_age = findViewById(R.id.edittext_age);
        editText_notes = findViewById(R.id.edittext_notes);
        spinner_gender = findViewById(R.id.spinner_gender);

        // TODO SAVE BUTTON
        // cancel button
        button_cancel = findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        ArrayAdapter<CharSequence> gender_adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(gender_adapter);

    }

//    private void pickImage() {
//        return;
//    }
//
//    private void requestGalleryPermission() {
//        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
//    }
//
//    private boolean checkGalleryPermission() {
//        boolean galleryPer = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//        return galleryPer;
//    }
//
//    private void requestCameraPermission() {
//        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
//    }
//
//    private boolean checkCameraPermission() {
//        boolean cameraPer = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
//        boolean galleryPer = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//        return cameraPer&&galleryPer;
//    }
}