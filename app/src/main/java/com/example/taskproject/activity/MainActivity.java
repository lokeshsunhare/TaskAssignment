package com.example.taskproject.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskproject.R;
import com.example.taskproject.adapter.UserListAdapter;
import com.example.taskproject.databinding.ActivityMainBinding;
import com.example.taskproject.model.User;
import com.example.taskproject.utils.NetworkCheckActivity;
import com.example.taskproject.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private UserListAdapter adapter;
    private int currentPage = 1;
    private int start = 0;
    private UserViewModel viewModel;
    private boolean isLoadingLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar.toolbar);
        binding.toolbar.tvTitle.setText(getResources().getString(R.string.app_name));

        binding.userRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.userRecycler.setHasFixedSize(true);
        binding.userRecycler.addItemDecoration(new DividerItemDecoration(binding.userRecycler.getContext()
                , LinearLayoutManager.VERTICAL));

        adapter = new UserListAdapter(MainActivity.this, new ArrayList<>());
        binding.userRecycler.setAdapter(adapter);

        setLoadMoreRecyclerView();

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.init(this);

        getUsersList(currentPage, start);

        viewModel.getUserLiveData().observe(this, this::setUserAdapter);

    }

    private void loadMoreUsers() {
        isLoadingLoadMore = true;
        currentPage++;
        start++;
        int limit = 6;
        start = start + limit;
        getUsersList(currentPage, start);
    }

    private void getUsersList(int currentPage, int start) {
        binding.progressCircular.setVisibility(View.VISIBLE);
        if (NetworkCheckActivity.isNetworkConnected(this)) {
            viewModel.getUserFromServer(currentPage);
        } else {
            viewModel.getUserFromLocalDB(start, 6);
        }
    }

    private void setUserAdapter(List<User> userList) {
        binding.progressCircular.setVisibility(View.GONE);
        adapter.addUsers(userList);
        isLoadingLoadMore = false;
    }

    private boolean isLoading() {
        return isLoadingLoadMore;
    }

    private boolean isLastPage() {
        return adapter.getItemCount() >= 12;
    }

    private int getPageSize() {
        return 6;
    }

    private void setLoadMoreRecyclerView() {
        binding.userRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading() && !isLastPage()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= getPageSize()) {
                        loadMoreUsers();
                    }
                }
            }
        });
    }

}