package com.example.findu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SinglePostActivity extends AppCompatActivity {
    //Post section
    Post currentPost;
    TextView singlePost_name, singlePost_notes, singlePost_age, singlePost_gender;
    ImageView singlePost_photo;
    String post_id;

    // firebase
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    // comment section
    Button addCommentButton;
    RecyclerView recyclerView_comment;
    CommentAdapter commentAdapter;
    ArrayList<Comment> comments;
    EditText editText_comment;

    String comment_userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        // post section
        singlePost_name = findViewById(R.id.textView_singleName);
        singlePost_notes = findViewById(R.id.textView_singleNotes);
        singlePost_photo = findViewById(R.id.imageView_singlePostPhoto);
        singlePost_age = findViewById(R.id.textView_age);
        singlePost_gender = findViewById(R.id.textView_gender);

        // data carried from main activity
        Intent intent = getIntent();
        singlePost_name.setText(intent.getStringExtra("name"));
        post_id = intent.getStringExtra("post_id");
        comment_userName = intent.getStringExtra("userName");


        // firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // retrieve data from firebase for the single post
        DocumentReference postRef = db.collection("posts").document(post_id);
        Log.v("post_id", post_id);
        postRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentPost = documentSnapshot.toObject(Post.class);

                // set data to post section
                singlePost_notes.setText("Notes: " + currentPost.getNote());
                singlePost_age.setText("Age: " + currentPost.getAge());
            }
        });



        // comment section
        addCommentButton = findViewById(R.id.button_addComment);
        editText_comment = findViewById(R.id.editText_comment);
        recyclerView_comment = findViewById(R.id.recyclerview_comment);


        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference commentReference = firebaseDatabase.getReference("comments").child(post_id).push();
                String comment_content = editText_comment.getText().toString();
                String comment_userId = firebaseUser.getUid();
                Log.d("currentuid", firebaseUser.getUid());

                Comment comment = new Comment(comment_content, comment_userId, comment_userName);
                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"comment added", Toast.LENGTH_SHORT).show();
                        editText_comment.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"fail to add comment", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        initRecyclerViewComment();
    }

    private void initRecyclerViewComment() {
        recyclerView_comment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference("comments").child(post_id);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments = new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    comments.add(comment) ;
                }
                commentAdapter = new CommentAdapter(getApplicationContext(),comments);
                recyclerView_comment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}