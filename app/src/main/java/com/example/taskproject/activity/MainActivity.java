package com.example.taskproject.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskproject.R;
import com.example.taskproject.adapter.UserListAdapter;
import com.example.taskproject.api.RestApI;
import com.example.taskproject.database.LocalUserDatabase;
import com.example.taskproject.databinding.ActivityMainBinding;
import com.example.taskproject.model.User;
import com.example.taskproject.model.UserResponse;
import com.example.taskproject.utils.NetworkCheckActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private UserListAdapter adapter;
    private int currentPage = 1;
    private int start = 0;
    private int limit = 6;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar.toolbar);
        binding.toolbar.tvTitle.setText(getResources().getString(R.string.app_name));

        binding.userRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.userRecycler.setHasFixedSize(true);
        adapter = new UserListAdapter(MainActivity.this, new ArrayList<>());
        binding.userRecycler.setAdapter(adapter);

        binding.userRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                    // Load more data when reaching the end
                    loadMoreUsers();
                }
            }
        });

        if (NetworkCheckActivity.isNetworkConnected(this)) {
            getUserFromServer(currentPage);
        } else {
            getUserFromLocal(start, limit);
        }
    }

    private void getUserFromLocal(int start, int limit) {
        setUserAdapter(LocalUserDatabase.getInstance(this).userDao().getAllUser(start, limit));
    }

    private void loadMoreUsers() {
        currentPage++;
        start++;
        start = start + limit;
        if (NetworkCheckActivity.isNetworkConnected(this)) {
            getUserFromServer(currentPage);
        } else {
            getUserFromLocal(start, limit);
        }
    }

    private void getUserFromServer(int page) {
        binding.progressCircular.setActivated(true);
        binding.progressCircular.setVisibility(View.VISIBLE);
        Call<UserResponse> call = RestApI.getInstance().getRetrofitApi().getUsers(page);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body() != null) {
                    UserResponse res = response.body();
                    List<User> users = res.getData();
                    LocalUserDatabase.getInstance(MainActivity.this).userDao().insertAllUser(users);
                    setUserAdapter(users);
                }
                binding.progressCircular.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                binding.progressCircular.setVisibility(View.GONE);
            }
        });

    }

    private void setUserAdapter(List<User> userList) {
        adapter.addUsers(userList);
    }

    private static final String TAG = "MainActivity";

}