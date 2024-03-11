package com.example.taskproject;

import static org.junit.Assert.assertEquals;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.taskproject.database.LocalUserDatabase;
import com.example.taskproject.database.dao.UserDao;
import com.example.taskproject.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryTest {

    private UserDao userDao;
    private LocalUserDatabase userDatabase;

    @Before
    public void setUp() {
        userDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), LocalUserDatabase.class)
                .allowMainThreadQueries() // Allow Room operations on main thread for tests only
                .build();
        userDao = userDatabase.userDao();
    }

    @After
    public void tearDown() {
        userDatabase.close();
    }

    @Test
    public void testInsertUser() throws Exception {
        User user = new User(1, "John Doe", "john.doe@example.com", "Doe");
        List<User> users = new ArrayList<>();
        users.add(user);
        // Insert user into the database
        userDao.insertAllUser(users);

        // Verify user is inserted correctly
        List<User> userLocals = userDao.getUsers(0,6);
        assertEquals(1, userLocals.size());
        User insertedUser = userLocals.get(0);
        assertEquals(user.getId(), insertedUser.getId());
        assertEquals(user.getFirstName() +" "+user.getLastName(), insertedUser.getFirstName() + " " + insertedUser.getLastName());
        assertEquals(user.getEmail(), insertedUser.getEmail()); // Assuming separate first/last name fields in UserLocal
    }
}
