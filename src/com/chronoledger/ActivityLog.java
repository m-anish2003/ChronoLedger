package com.chronoledger;

public class ActivityLog {
    private String activity;
    private String timestamp;
    private String hash;
    private String prevHash;

    public ActivityLog(String activity, String timestamp, String hash, String prevHash) {
        this.activity = activity;
        this.timestamp = timestamp;
        this.hash = hash;
        this.prevHash = prevHash;
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
}
