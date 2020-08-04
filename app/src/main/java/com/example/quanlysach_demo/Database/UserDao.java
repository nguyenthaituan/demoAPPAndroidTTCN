package com.example.quanlysach_demo.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * from user_table ORDER BY userid ASC")
    List<User> getAllUser();

    @Query("SELECT * from user_table WHERE username = :un")
    List<User> auth(String un);

    @Query("SELECT * from user_table WHERE userid = :uid")
    List<User> getUserById(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);
}
