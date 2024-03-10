package com.example.taskproject.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.taskproject.database.dao.UserDao;
import com.example.taskproject.model.User;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class LocalUserDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static volatile LocalUserDatabase INSTANCE;

    public static LocalUserDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalUserDatabase.class) {
                if (INSTANCE == null) {
                    final byte[] passphrase = SQLiteDatabase.getBytes("123456".toCharArray());
                    final SupportFactory factory = new SupportFactory(passphrase);
                    INSTANCE = Room.databaseBuilder(context, LocalUserDatabase.class, "User_Database").allowMainThreadQueries().openHelperFactory(factory).build();
                }
            }
        }
        return INSTANCE;
    }
}
