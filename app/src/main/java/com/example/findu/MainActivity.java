package com.example.findu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PostAdapter.OnPostListener{
    BottomNavigationView bottomNav;

    RecyclerView recyclerView;
    PostAdapter postAdapter;

    Spinner spinner_gender;
    FloatingActionButton addPost;

    //test
    ArrayList<Post> posts;

    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recyclerView_post);

        firebaseAuth = FirebaseAuth.getInstance();

        // test post data
        posts = new ArrayList<>();
//        posts.add(new Post("Cheng Xue", 29, "San Jose"));
//        posts.add(new Post("Emma Xue", 29, "San Jose"));
//        posts.add(new Post("Jikun Li", 17, "San Jose"));
//        posts.add(new Post("David Li", 13,  "San Jose"));
//        posts.add(new Post("Joyce Xu", 18,  "San Jose"));
//        posts.add(new Post("Jinru Xu", 12,  "San Jose"));
//        posts.add(new Post("Mingyue Wang", 16,  "San Jose"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(MainActivity.this, posts, this);
        recyclerView.setAdapter(postAdapter);


        db = FirebaseFirestore.getInstance();
        EventChangeListener();

        addPost = findViewById(R.id.Button_addPost);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddPostActivity.class));
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

    private void EventChangeListener() {
        db.collection("posts").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore retrieve data error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc: value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                posts.add(dc.getDocument().toObject(Post.class));
                                Log.d("doc", dc.getDocument().toString());
                            }
                            postAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

//    @Override
//    public void applyTexts(String name, int age, String notes) {
//        Post temp = new Post(name, age, notes);
//        posts.add(temp);
//        postAdapter.notifyItemInserted(posts.size()-1);
//        Toast.makeText(MainActivity.this, "post added successfully in PostActivity", Toast.LENGTH_SHORT).show();
//    }


    @Override
    public void onPostClick(int position) {
        Log.d("PostActivity", "onPostClicked");
        Intent intent = new Intent(this, SinglePostActivity.class);
        String tmpName = posts.get(position).getName();
        String tmpNotes = posts.get(position).getNotes();
        intent.putExtra("name", tmpName);
        intent.putExtra("note", tmpNotes);
        startActivity(intent);
    }
}