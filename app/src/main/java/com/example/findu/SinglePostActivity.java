package com.example.findu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SinglePostActivity extends AppCompatActivity {
    TextView singlePost_name, singlePost_notes;
    ImageView singlePost_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        singlePost_name = findViewById(R.id.textView_singleName);
        singlePost_notes = findViewById(R.id.textView_singleNotes);
        singlePost_photo = findViewById(R.id.imageView_singlePostPhoto);

        Intent intent = getIntent();
        singlePost_name.setText(intent.getStringExtra("name"));
    }
}