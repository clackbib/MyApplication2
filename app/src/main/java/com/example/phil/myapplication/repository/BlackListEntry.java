package com.example.phil.myapplication.repository;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by habibokanla on 23/03/2018.
 */

@Entity
public class BlackListEntry {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String phoneNumber;
    public String contactName;
    public String replyMessage;

    public BlackListEntry(String phoneNumber, String contactName, String replyMessage) {
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
        this.replyMessage = replyMessage;
    }
}
