package com.example.taskproject.api;

import com.example.taskproject.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkService {
    @GET("users")
    Call<UserResponse> getUsers(@Query("page") int page);
}
