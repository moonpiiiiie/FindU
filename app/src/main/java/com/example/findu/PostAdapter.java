package com.example.findu;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements View.OnClickListener{


    private LayoutInflater layoutInflater;
    private List<Post> posts;
    private OnPostListener onPostListener;
    private Context context;
    FirebaseFirestore db;
    final String TAG = "Post Adapater";
    private UserPosts activity;

    PostAdapter(Context context, List<Post> posts, OnPostListener onPostListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.posts = posts;
        this.onPostListener = onPostListener;
    }

    PostAdapter(Context context, List<Post> posts, OnPostListener onPostListener, UserPosts activity) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.posts = posts;
        this.onPostListener = onPostListener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.post_view, viewGroup, false);
        return new ViewHolder(view, onPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post = posts.get(i);
        viewHolder.name.setText(post.getName());
        viewHolder.age.setText(String.valueOf(post.getAge()));
        viewHolder.note.setText(post.getNote());
        viewHolder.category.setText(post.getCategory());
        viewHolder.gender.setText(" "+post.getGender());
        viewHolder.setPhoto(post.getImage());

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, age, gender, note, category;
        ImageView photo;
        OnPostListener onPostListener;

        public ViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);
            name = itemView.findViewById(R.id.textView_name);
            age = itemView.findViewById(R.id.textView_age);
            gender = itemView.findViewById(R.id.textView_gender);
            category = itemView.findViewById(R.id.textView_category);
//
            note = itemView.findViewById(R.id.textView_note);
            this.onPostListener = onPostListener;
            itemView.setOnClickListener(this);
        }


        public void setPhoto(String urlPost) {
            photo = itemView.findViewById(R.id.imageView_post);
            Glide.with(context).load(urlPost).apply(new RequestOptions().override(150, 150)).centerCrop().into(photo);
        }

        @Override
        public void onClick(View view) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }
    public interface OnPostListener {
        void onPostClick(int position);
    }

    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position) {
        Post post = posts.get(position);
        db = FirebaseFirestore.getInstance();
        db.collection("posts").document(post.getPost_id()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "deleted");
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "failed to delete");
                                    }
                                });
        posts.remove(position);
        notifyItemRemoved(position);
    }
}
