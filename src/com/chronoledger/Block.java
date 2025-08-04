package com.chronoledger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Block {
    private String activity;
    private String timestamp;
    private String hash;
    private String prevHash;

    

    public Block(String activity, String prevHash) {
        this.activity = activity;
        this.timestamp = getCurrentTime();
        this.prevHash = prevHash;
        this.hash = Utils.applySHA256(activity + timestamp + prevHash);
    }

    public Block(String timestamp, String activity, String prevHash, String hash) {
        this.activity = activity;
    this.timestamp = timestamp;
    
    this.prevHash = prevHash;
    this.hash = hash;
}

    public String getActivity() {
        return activity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    private String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
