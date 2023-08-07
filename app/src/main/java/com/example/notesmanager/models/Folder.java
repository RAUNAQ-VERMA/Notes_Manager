package com.example.notesmanager.models;

import java.util.ArrayList;

public class Folder {

    String folderName;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    ArrayList<String> name;
    ArrayList <String> path;

    public Folder(String folderName, ArrayList<String> name, ArrayList<String> path) {
        this.folderName = folderName;
        this.name = name;
        this.path = path;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public ArrayList<String> getPath() {
        return path;
    }

    public void setPath(ArrayList<String> path) {
        this.path = path;
    }

}
