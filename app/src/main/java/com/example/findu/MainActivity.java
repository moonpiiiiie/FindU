package com.example.findu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PostAdapter.OnPostListener {
    BottomNavigationView bottomNav;

    RecyclerView recyclerView;
    PostAdapter postAdapter;

    Spinner spinner_gender;
    FloatingActionButton addPost;

    ArrayList<Post> posts;

    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                fetchPosts();
                pullToRefresh.setRefreshing(false);
            }
        });

        recyclerView = findViewById(R.id.recyclerView_post);

        firebaseAuth = FirebaseAuth.getInstance();

        // test post data
        posts = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(MainActivity.this, posts, this);
        recyclerView.setAdapter(postAdapter);
        db = FirebaseFirestore.getInstance();

        fetchPosts();
//        EventChangeListener();

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
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.post_nav_button:
                    return true;
                case R.id.profile_nav_button:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
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
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                posts.add(dc.getDocument().toObject(Post.class));
                            }
                            postAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void fetchPosts() {
        CollectionReference postRef = db.collection("posts");
        String TAG = "FirestoreApi";
        postRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot x = task.getResult();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        posts.add(document.toObject(Post.class));
                        postAdapter.notifyDataSetChanged();
//                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }


    @Override
    public void onPostClick(int position) {
        Log.d("PostActivity", "onPostClicked");
        Intent intent = new Intent(this, SinglePostActivity.class);
        String tmpName = posts.get(position).getName();
        String tmpNotes = posts.get(position).getNote();
        intent.putExtra("name", tmpName);
        intent.putExtra("note", tmpNotes);
        startActivity(intent);
    }
}