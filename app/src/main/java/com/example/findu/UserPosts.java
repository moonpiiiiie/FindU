package com.example.findu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserPosts extends AppCompatActivity implements PostAdapter.OnPostListener {
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    ArrayList<Post> userPosts;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    String userName;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userPosts.clear();
                fetchPosts();
                pullToRefresh.setRefreshing(false);
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                userName = user.getUsername();
            }
        });

        recyclerView = findViewById(R.id.recyclerView_userPost);
        userPosts = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(UserPosts.this, userPosts, this);
        recyclerView.setAdapter(postAdapter);
        fetchPosts();

        backButton = findViewById(R.id.imageView_backbutton_userPost);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                        userPosts.add(document.toObject(Post.class));
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
        Intent intent = new Intent(this, SinglePostActivity.class);
        String tmpName = userPosts.get(position).getName();
        String tmpNotes = userPosts.get(position).getNote();
        String tmpid = userPosts.get(position).getPost_id();
        intent.putExtra("name", tmpName);
        intent.putExtra("note", tmpNotes);
        intent.putExtra("post_id", tmpid);
        intent.putExtra("userName", userName);
        startActivity(intent);
    }
}