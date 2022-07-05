package com.example.findu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private String[] items;
    private LayoutInflater inflater;
    Context context;

    public ProfileAdapter(Context context, String[] items) {
        inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.profile_item, parent, false);
        return new ProfileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        holder.label.setText(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {

        TextView label;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.profile_label);
        }
    }
}
