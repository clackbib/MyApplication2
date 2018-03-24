package com.example.phil.myapplication.repository.impl;

import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;

import com.example.phil.myapplication.repository.BlackListEntry;
import com.example.phil.myapplication.repository.BlacklistRepository;
import com.example.phil.myapplication.repository.database.AppDatabase;
import com.example.phil.myapplication.repository.database.dao.BlackListEntryDao;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by habibokanla on 24/03/2018.
 */

public class BlacklistRepositoryDatabaseImpl extends BlacklistRepository {

    private BlackListEntryDao blackListEntryDao;
    private HashMap<BlackListChangeListener, Observer<List<BlackListEntry>>> map = new HashMap<>();

    //TODO: Elaborate on what's happening here
    public BlacklistRepositoryDatabaseImpl(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "app.db")
                .build();
        blackListEntryDao = db.getBlaclistentryDao();
    }

    @Override
    public void addBlackListChangeObserver(final BlackListChangeListener listChangeListener) {
        Observer<List<BlackListEntry>> observer = new Observer<List<BlackListEntry>>() {
            @Override
            public void onChanged(@Nullable List<BlackListEntry> blackListEntries) {
                listChangeListener.onListReceived(blackListEntries);
            }
        };
        map.put(listChangeListener, observer);
        blackListEntryDao.getBlackListEntries().observeForever(observer);
    }

    @Override
    public void removeBlackListChangeObserver(BlackListChangeListener listChangeListener) {
        Observer<List<BlackListEntry>> observer = map.get(listChangeListener);
        if (observer != null) {
            blackListEntryDao.getBlackListEntries().removeObserver(observer);
        }
    }

    @Override
    public void addBlackListEntry(final BlackListEntry entry) {
        runOnIO(new Runnable() {
            @Override
            public void run() {
                blackListEntryDao.insert(entry);
            }
        });
    }

    @Override
    public void removeBlackListEntry(final int index) {
        runOnIO(new Runnable() {
            @Override
            public void run() {
                blackListEntryDao.delete(index);
            }
        });
    }

    @Override
    public boolean containsNumber(String phoneNumber) {
        //TODO FIX blackListEntryDao.getNumberMatches(phoneNumber).size() > 0;
        return false;
    }

    @Override
    public void changeBlackListEntryMessage(final int index, final String newValue) {
        runOnIO(new Runnable() {
            @Override
            public void run() {
                blackListEntryDao.updateMessage((long) index, newValue);
            }
        });
    }


    private Executor IO_EXECUTOR = Executors.newSingleThreadExecutor();
    private void runOnIO(Runnable runnable) {
        IO_EXECUTOR.execute(runnable);
    }

}
