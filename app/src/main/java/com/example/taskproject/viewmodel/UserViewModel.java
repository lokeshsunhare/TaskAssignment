package com.example.taskproject.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.taskproject.model.User;
import com.example.taskproject.repositoy.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository;

    public UserViewModel() {

    }

    public void init(Context context) {
        userRepository = UserRepository.getInstance(context);

    }

    public LiveData<List<User>> getUserLiveData() {
        return userRepository.getUserLiveData();
    }

    public void getUserFromLocalDB(int start, int limit) {
        userRepository.getUserFromLocalDB(start, limit);
    }

    public void getUserFromServer(int page) {
        userRepository.getUserFromServer(page);
    }

}
