package com.example.findu;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    private LayoutInflater layoutInflater;
    private List<Post> posts;

    PostAdapter(Context context, List<Post> posts) {
        this.layoutInflater = LayoutInflater.from(context);
        this.posts = posts;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.post_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post = posts.get(i);
        viewHolder.name.setText(post.getName());
        viewHolder.age.setText(String.valueOf(post.getAge()));


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, gender;
        ImageView postImage;
        View postView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            postView = itemView;
            name = itemView.findViewById(R.id.textView_name);
            age = itemView.findViewById(R.id.textView_age);
            gender = itemView.findViewById(R.id.textView_gender);
        }
        public void setPostImage(String urlPost){
            //postImage = postView.findViewById(R.id.post)
        }
    }

}
