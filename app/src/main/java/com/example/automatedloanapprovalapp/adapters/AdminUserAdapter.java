package com.example.automatedloanapprovalapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.User;

import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.ViewHolder> {
    private List<User> userList;
    private OnEditClickListener onEditClickListener;
    private OnDeleteClickListener onDeleteClickListener;
    private OnDetailsClickListener onDetailsClickListener;

    public AdminUserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.usernameTextView.setText(user.getUsername());
        holder.emailTextView.setText(user.getEmail());
        holder.roleTextView.setText(user.getRole());
        // Edit Button Click Listener
        holder.editImageView.setOnClickListener(view -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(holder.getAdapterPosition());
            }
        });

        // Delete Button Click Listener
        holder.deleteImageView.setOnClickListener(view -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(holder.getAdapterPosition());
            }
        });

        // Details Button Click Listener
        holder.userDetails.setOnClickListener(view -> {
            if (onDetailsClickListener != null) {
                onDetailsClickListener.onDetailsClick(holder.getAdapterPosition());
            }
        });

        if (!user.getRole().equals("customer")){
            holder.userDetails.setVisibility(View.GONE);
            holder.moreDetailsArrow.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView emailTextView;
        TextView roleTextView;
        TextView userDetails;
        ImageView editImageView, deleteImageView, moreDetailsArrow;

        ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
            editImageView = itemView.findViewById(R.id.editImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            userDetails = itemView.findViewById(R.id.more_details_txt);
            moreDetailsArrow = itemView.findViewById(R.id.more_details_arrow);


        }
    }

    public interface OnEditClickListener {
        void onEditClick(int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnDetailsClickListener {
        void onDetailsClick(int position);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void setOnDetailsClickListener(OnDetailsClickListener listener) {
        this.onDetailsClickListener = listener;
    }
}
