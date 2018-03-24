package com.example.phil.myapplication.repository;

import com.example.phil.myapplication.repository.impl.BlackListRepositoryMemoryImpl;

import java.util.List;

/**
 * Created by habibokanla on 23/03/2018.
 */

public abstract class BlacklistRepository {


    public interface BlackListChangeListener {
        void onListReceived(List<BlackListEntry> blackListEntries);
    }

    public abstract void addBlackListChangeObserver(BlackListChangeListener listChangeListener);

    public abstract void removeBlackListChangeObserver(BlackListChangeListener listChangeListener);

    public abstract void addBlackListEntry(BlackListEntry entry);

    public abstract void removeBlackListEntry(int index);

    public abstract boolean containsNumber(String phoneNumber);

    public abstract void changeBlackListEntryMessage(int index, String newValue);

    private static BlacklistRepository INSTANCE;

    public static BlacklistRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BlackListRepositoryMemoryImpl();
        }
        return INSTANCE;
    }

}
