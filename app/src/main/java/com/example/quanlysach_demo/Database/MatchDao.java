package com.example.quanlysach_demo.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MatchDao {

    @Query("SELECT * from match_table WHERE owneduser = :uid ORDER BY matchid ASC")
    List<Match> getMatchByOwner(Integer uid);

    @Query("SELECT * from match_table WHERE wishuser = :uid ORDER BY matchid ASC")
    List<Match> getMatchByWisher(Integer uid);

    @Query("SELECT * from match_table WHERE wishuser = :wishid AND owneduser = :ownedid ORDER BY matchid ASC")
    List<Match> checkMatch(Integer ownedid, Integer wishid);

//    @Query("SELECT owneduser, wishuser, bookid from book_table WHERE wishuser = :uid AND owneduser IS NOT NULL")
//    List<Match> getMatchByUserId(Integer uid);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Match match);

    @Query("DELETE FROM match_table")
    void deleteTable();
}
