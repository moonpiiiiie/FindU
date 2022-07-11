package com.example.findu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
    private EditText editText_name;
    private EditText editText_age;
    private EditText editText_notes;
    private Spinner spinner_gender;
    private Button button_save;
    private Button button_cancel;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private String currentUserId;
    private ImageView post_photo;
    private Uri postImageUri=null;
    RadioButton radioButton_toFind, radioButton_tobeFound;
    String category;

    ProgressBar progressBar_addPost;

    FirebaseFirestore db;

    ActivityResultLauncher<String> selectPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();


        setContentView(R.layout.activity_add_post);

        ActivityResultLauncher<Intent> galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            postImageUri = data.getData();
                            post_photo.setImageURI(postImageUri);
                            //post_photo.setImageURI(data.getData());


                        }
                    }
                });

        // post photo
        post_photo = findViewById(R.id.post_photo);
//        button_uploadPhoto = findViewById(R.id.button_uploadPhoto);

        post_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkGalleryPermission()) {
                    requestGalleryPermission();
                }
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryResultLauncher.launch(gallery);
                //StorageReference postRef = storageReference.child("post_photo").child(FieldValue.serverTimestamp().toString() + ".jpg");
            }
        });


        //radiobutton
        radioButton_toFind = findViewById(R.id.radioButton_tofind);
        radioButton_tobeFound = findViewById(R.id.radioButton_tobefound);

        // progress bar
        progressBar_addPost = findViewById(R.id.progressBar_addPost);
        progressBar_addPost.setVisibility(View.INVISIBLE);
        // post text
        editText_name = findViewById(R.id.edittext_name);
        editText_age = findViewById(R.id.edittext_age);
        editText_notes = findViewById(R.id.edittext_notes);
        spinner_gender = findViewById(R.id.spinner_gender);


        // save button
        // TODO add progress bar
        button_save = findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_addPost.setVisibility(View.VISIBLE);
                String name = editText_name.getText().toString();
                int age = Integer.parseInt(editText_age.getText().toString());
                String note = editText_notes.getText().toString();
                String gender = spinner_gender.getSelectedItem().toString();


                if (!name.isEmpty() && !note.isEmpty() &&postImageUri!=null ) {
                    StorageReference postRef = storageReference.child("post_photo").child(FieldValue.serverTimestamp().toString() + ".jpg");
                    postRef.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                postRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map<String, Object> userPost = new HashMap<String, Object>();
                                        //String name, String image, int age, String gender, String user_id, String note, Timestamp time, String category
                                        Timestamp ts = new Timestamp(new Date());
                                        Post post = new Post(name, uri.toString(), age, gender, currentUserId, note, ts, category);
                                        CollectionReference postRef = db.collection("posts");
                                        String post_id = post.getPost_id();
                                        // customize post document id
                                        postRef.document(post_id).set(post);
//                                        FirestoreAPI.writePost(post);
                                        Toast.makeText(AddPostActivity.this, "Post added successfully!", Toast.LENGTH_SHORT).show();
                                        progressBar_addPost.setVisibility(View.INVISIBLE);
                                        finish();
                                    }
                                });

                            } else {
                                Toast.makeText(AddPostActivity.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                }else{
                    Toast.makeText(AddPostActivity.this, "Please upload the image and write key information", Toast.LENGTH_SHORT).show();
                    }

                }

        });

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


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        category="";
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton_tobefound:
                if(checked)
                    category = "to be found";
                break;
            case R.id.radioButton_tofind:
                if(checked)
                    category = "to find";
                break;
        }
    }


    private void requestGalleryPermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkGalleryPermission() {
        boolean galleryPer = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return galleryPer;
    }

}