package com.example.taskproject.repositoy;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.taskproject.api.RestApI;
import com.example.taskproject.database.LocalUserDatabase;
import com.example.taskproject.database.dao.UserDao;
import com.example.taskproject.model.User;
import com.example.taskproject.model.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserDao userDao;
    private static UserRepository instance;
    private final MutableLiveData<List<User>> userLiveData = new MutableLiveData<>();

    public static synchronized UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context);
        }
        return instance;
    }

    private UserRepository(Context context) {
        userDao = LocalUserDatabase.getInstance(context).userDao();
    }

    private void insertToLocalDB(List<User> list) {
        userDao.insertAllUser(list);
    }

    public LiveData<List<User>> getUserLiveData() {
        return userLiveData;
    }

    public void getUserFromLocalDB(int start, int limit) {
        userLiveData.postValue(userDao.getUsers(start, limit));
    }

    public void getUserFromServer(int page) {

        Call<UserResponse> call = RestApI.getInstance().getRetrofitApi().getUsers(page);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                try {
                    if (response.body() != null) {
                        UserResponse res = response.body();
                        userLiveData.postValue(res.getData());
                        insertToLocalDB(res.getData());
                    }
                } catch (Exception e) {
                    Log.d(TAG, "onResponse: " + e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private static final String TAG = "UserRepository";

}
