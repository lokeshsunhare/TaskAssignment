package com.example.taskproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskproject.R;
import com.example.taskproject.databinding.RvUserListItemBinding;
import com.example.taskproject.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserHolder> {

    private Context context;
    private List<User> userList;

    public UserListAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_user_list_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User user = userList.get(holder.getAdapterPosition());
        holder.binding.tvEmail.setText(user.getEmail());
        holder.binding.tvName.setText(user.getFirstName() + " " + user.getLastName());
        Picasso.with(context).load(user.getAvatar()).into(holder.binding.avatarImage);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserHolder extends RecyclerView.ViewHolder {
        private final RvUserListItemBinding binding;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            binding = RvUserListItemBinding.bind(itemView);
        }
    }

    public void addUsers(List<User> newUsers) {
        userList.addAll(newUsers);
        notifyDataSetChanged();
    }
}
