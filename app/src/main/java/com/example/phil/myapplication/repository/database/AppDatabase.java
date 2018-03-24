package com.example.phil.myapplication.repository.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.phil.myapplication.repository.BlackListEntry;
import com.example.phil.myapplication.repository.database.dao.BlackListEntryDao;

/**
 * Created by habibokanla on 24/03/2018.
 */
@Database(entities = {BlackListEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BlackListEntryDao getBlaclistentryDao();
}
