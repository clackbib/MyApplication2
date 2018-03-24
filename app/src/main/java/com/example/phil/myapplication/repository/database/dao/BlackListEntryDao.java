package com.example.phil.myapplication.repository.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.phil.myapplication.repository.BlackListEntry;

import java.util.List;

/**
 * Created by habibokanla on 24/03/2018.
 */

@Dao
public interface BlackListEntryDao {

    @Query("SELECT * FROM BlackListEntry")
    LiveData<List<BlackListEntry>> getBlackListEntries();

    @Query("DELETE FROM BlackListEntry WHERE id = :index")
    void delete(long index);

    @Query("UPDATE BlacklistEntry SET replyMessage = :newMessage where id = :index")
    void updateMessage(long index, String newMessage);

    @Insert
    void insert(BlackListEntry entry);

    @Query("SELECT id FROM BlacklistEntry WHERE phoneNumber = :number")
    List<Long> getNumberMatches(String number);
}
