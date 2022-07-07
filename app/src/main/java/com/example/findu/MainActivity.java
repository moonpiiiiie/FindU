package com.example.findu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddPost.PostDialogListener {
    BottomNavigationView bottomNav;
    private FirebaseFirestore firestore;

    RecyclerView recyclerView;
    PostAdapter postAdapter;

    Spinner spinner_gender;
    FloatingActionButton fab;
    private Query query;
    private ListenerRegistration listenerRegistration;

    //test
    ArrayList<Post> posts;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recyclerView_post);
        firebaseAuth = FirebaseAuth.getInstance();



        posts = new ArrayList<>();
        /**
          // test post data
        posts.add(new Post("Cheng Xue", 29, "San Jose"));
        posts.add(new Post("Emma Xue", 29, "San Jose"));
        posts.add(new Post("Jikun Li", 17, "San Jose"));
        posts.add(new Post("David Li", 13,  "San Jose"));
        posts.add(new Post("Joyce Xu", 18,  "San Jose"));
        posts.add(new Post("Jinru Xu", 12,  "San Jose"));
        posts.add(new Post("Mingyue Wang", 16,  "San Jose")); **/
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(MainActivity.this, posts);
        recyclerView.setAdapter(postAdapter);

        fab = findViewById(R.id.Button_addPost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openAddPostDialog(view);
                startActivity(new Intent(view.getContext(), AddPostActivity.class));

            }
        });
        if(firebaseAuth.getCurrentUser()!= null){
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isBottom = !recyclerView.canScrollVertically(1);
                    if(isBottom)
                        Toast.makeText(MainActivity.this, "Reached Bottom", Toast.LENGTH_SHORT).show();
                }
            });

            query = firestore.collection("Posts").orderBy("time" , Query.Direction.DESCENDING);
            listenerRegistration = query.addSnapshotListener(MainActivity.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (DocumentChange doc: value.getDocumentChanges()){
                        if(doc.getType()==DocumentChange.Type.ADDED){
                            Post post = doc.getDocument().toObject(Post.class);
                            posts.add(post);
                            postAdapter.notifyDataSetChanged();
                        }else{
                            postAdapter.notifyDataSetChanged();
                        }
                    }
                    listenerRegistration.remove();

                }
            });

        }
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

    //@Override
    public void applyTexts(String name, int age, String notes,String gender, String imageUrl, String userId) {
        Post temp = new Post(name, age, notes,gender, imageUrl, userId);
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

    @Override
    public void applyTexts(String name, int age, String notes) {

    }
}