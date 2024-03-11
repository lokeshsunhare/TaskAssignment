
package com.example.taskproject.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class User implements Serializable {
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private Integer id;
    @SerializedName("email")
    @ColumnInfo(name = "email")
    private String email;
    @SerializedName("first_name")
    @ColumnInfo(name = "first_name")
    private String firstName;
    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    private String lastName;
    @SerializedName("avatar")
    @ColumnInfo(name = "avatar")
    private String avatar;

    public User() {

    }

    public User(Integer id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
