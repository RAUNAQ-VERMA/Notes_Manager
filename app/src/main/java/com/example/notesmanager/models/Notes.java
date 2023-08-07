package com.example.notesmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Notes {

    String path;
    String tag;
    String topic;
    boolean isSelected = false;


    public Notes( String path, String tag, String topic) {
        this.path = path;
        this.tag = tag;
        this.topic = topic;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Notes(String path, String tag) {
        this.path = path;
        this.tag = tag;
    }

}
