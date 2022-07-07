package com.example.findu;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import com.bumptech.glide.Glide;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    private LayoutInflater layoutInflater;
    private List<Post> posts;
    private Activity context;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    PostAdapter(Activity context, List<Post> posts) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.posts = posts;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        View view = layoutInflater.inflate(R.layout.post_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post = posts.get(i);
        viewHolder.setPostImage(post.getPostImageUrl());
        viewHolder.setGender(post.getGender());
        viewHolder.setAge(post.getAge());
        viewHolder.setNote(post.getNotes());
        // add date?

        //viewHolder.name.setText(post.getName());
        //viewHolder.age.setText(String.valueOf(post.getAge()));
        //long milliseconds = post.getTime().getTime();

        String userId = post.getUserId();
        firestore.collection("posts").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String name = task.getResult().getString("name");
                    String image = task.getResult().getString("image");
                    String gender = task.getResult().getString("gender");
                    String note = task.getResult().getString("note");
                    String age =  task.getResult().getString("age");
                }else{
                    Toast.makeText(context,task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });





    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView age;
        TextView gender;
        TextView note;
        ImageView postImage;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //View v = LayoutInflater.from(context).inflate(R.layout.each_post , parent , false);
            firestore = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();
            //return new PostViewHolder(v);

            //super(itemView);
            view = itemView;
            //name = itemView.findViewById(R.id.textView_name);
           // age = view.findViewById(R.id.textView_age);
            //gender = itemView.findViewById(R.id.textView_gender);
        }
        public void setPostImage(String urlPost){
            postImage = view.findViewById(R.id.imageView_post);
            Glide.with(context).load(urlPost).into(postImage);

        }
        public void setName(String personName){
            name = view.findViewById(R.id.textView_name);
            name.setText(personName);
        }

        public void setAge(Integer personAge){
            age = view.findViewById(R.id.textView_age);
            age.setText(personAge);
        }

        public void setGender(String personGender){
            gender = view.findViewById(R.id.textView_gender);
            gender.setText(personGender);
        }
        public void setNote(String personNote){
            note = view.findViewById(R.id.textView_note);
            note.setText(personNote);
        }




    }

}
