package com.example.findu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddPost.PostDialogListener {
    BottomNavigationView bottomNav;

    RecyclerView recyclerView;
    PostAdapter postAdapter;

    Spinner spinner_gender;
    FloatingActionButton addPost;

    //test
    ArrayList<Post> posts;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView_post);

        firebaseAuth = FirebaseAuth.getInstance();

        // test post data
        posts = new ArrayList<>();
        posts.add(new Post("Cheng Xue", 29, "San Jose"));
        posts.add(new Post("Emma Xue", 29, "San Jose"));
        posts.add(new Post("Jikun Li", 29, "San Jose"));
        posts.add(new Post("David Li", 29,  "San Jose"));
        posts.add(new Post("Joyce Xu", 29,  "San Jose"));
        posts.add(new Post("Jinru Xu", 29,  "San Jose"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(this, posts);
        recyclerView.setAdapter(postAdapter);

        addPost = findViewById(R.id.Button_addPost);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddPostDialog(view);
            }
        });
//        //spinner widget
//        spinner_gender = findViewById(R.id.spinner_gender);
//        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_gender.setAdapter(spinnerAdapter);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.post_nav_button);
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.map_nav_button:
                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.post_nav_button:
                    return true;
                case R.id.profile_nav_button:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }

    @Override
    public void applyTexts(String name, int age, String notes) {
        Post temp = new Post(name, age, notes);
        posts.add(temp);
        postAdapter.notifyItemInserted(posts.size()-1);
        Toast.makeText(MainActivity.this, "post added successfully", Toast.LENGTH_SHORT).show();
    }

    public void openAddPostDialog(View view) {
        AddPost postDialog = new AddPost();
        postDialog.show(getSupportFragmentManager(), "add post");
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        // Check to see if the user is currently signed in.
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, EmailPasswordActivity.class));
            finish();
        }
    }
}