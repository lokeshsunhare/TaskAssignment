package com.example.taskproject.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.taskproject.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllUser(List<User> userList);

    @Query("Select * from user limit :start,:limit")
    List<User> getUsers(int start, int limit);

}
