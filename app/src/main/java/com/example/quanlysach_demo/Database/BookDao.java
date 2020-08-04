package com.example.quanlysach_demo.Database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.example.quanlysach_demo.Database.BookRepository.tempMatch;

@Dao
public interface BookDao {

    @Query("SELECT * from book_table WHERE bookid = :bi LIMIT 1")
    List<Book> getBook(Integer bi);

    @Query("SELECT * from book_table")
    List<Book> getAllBook();

    @Query("SELECT * from book_table WHERE bookname = :bn")
    List<Book> getBookByName(String bn);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Book book);

    @Query("SELECT owneduser, wishuser, bookid from book_table WHERE wishuser = :uid AND owneduser IS NOT NULL")
    List<tempMatch> getMatchByUserId(Integer uid);
}
