package com.example.phil.myapplication.repository.impl;

import com.example.phil.myapplication.repository.BlackListEntry;
import com.example.phil.myapplication.repository.BlacklistRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by habibokanla on 23/03/2018.
 */

public class BlackListRepositoryMemoryImpl extends BlacklistRepository {

    private List<BlackListEntry> blackListEntries;
    private List<BlackListChangeListener> listeners;

    public BlackListRepositoryMemoryImpl() {
        blackListEntries = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    @Override
    public void addBlackListChangeObserver(BlackListChangeListener listChangeListener) {
        listeners.add(listChangeListener);
        listChangeListener.onListReceived(blackListEntries);
    }

    @Override
    public void removeBlackListChangeObserver(BlackListChangeListener listChangeListener) {
        listeners.remove(listChangeListener);
    }

    @Override
    public void addBlackListEntry(BlackListEntry entry) {
        blackListEntries.add(entry);
        updateAllObservers();
    }

    @Override
    public void removeBlackListEntry(int index) {
        blackListEntries.remove(index);
        updateAllObservers();
    }

    @Override
    public boolean containsNumber(String phoneNumber) {
        for (BlackListEntry entry : blackListEntries) {
            if (phoneNumber.equals(entry.phoneNumber)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void changeBlackListEntryMessage(int index, String newValue) {
        blackListEntries.get(index).replyMessage = newValue;
        updateAllObservers();
    }


    private void updateAllObservers() {
        for (BlackListChangeListener l : listeners) {
            l.onListReceived(blackListEntries);
        }
    }
}
