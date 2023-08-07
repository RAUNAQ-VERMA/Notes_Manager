package com.example.notesmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.notesmanager.models.Folder;

import java.util.ArrayList;
import java.util.Arrays;

public class Main_DataBaseHelper extends SQLiteOpenHelper {

    public static final String FOLDER_TABLE = "Folder_Table";
    public static final String FOLDER_ID = "ID";
    public static final String FOLDER_NAME = "Folder_Name";
    public static final String FILE_NAME = "File_Name";
    public static final String FILE_PATH = "File_Path";

    public Main_DataBaseHelper(@Nullable Context context) {
        super(context, "Folder.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query =  "CREATE TABLE " + FOLDER_TABLE + "(" + FOLDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + FOLDER_NAME + " TEXT," +FILE_NAME +" TEXT, "  + FILE_PATH + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void add(Folder folder){
        ArrayList<String> fileName = new ArrayList<>(folder.getName());
        ArrayList<String> filePath = new ArrayList<>(folder.getPath());

        StringBuilder str1 = new StringBuilder();
        StringBuilder str2 = new StringBuilder();

        for(int i = 0;i<fileName.size();i++){
            str1.append(fileName.get(i)).append("@");
            str2.append(filePath.get(i)).append("@");
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FOLDER_NAME, folder.getFolderName());
        cv.put(FILE_NAME, String.valueOf(str1));
        cv.put(FILE_PATH, String.valueOf(str2));
        db.insert(FOLDER_TABLE,null,cv);
        db.close();
    }

    public ArrayList<Folder> get(){
        String query = "SELECT * FROM " + FOLDER_TABLE;
        String folderName ,fileName,filePath;
        ArrayList<Folder> folder = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
               folderName = cursor.getString(1);
               fileName =  cursor.getString(2);
               filePath = cursor.getString(3);

               String[] filenames = fileName.split("@");
               String[] filepaths = filePath.split("@");

               folder.add(new Folder(folderName, new ArrayList<>(Arrays.asList(filenames)), new ArrayList<>(Arrays.asList(filepaths))));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return folder;
    }
    public ArrayList<ArrayList<String>> getPaths(){
        String query = "SELECT * FROM " + FOLDER_TABLE;
        String filePath;
        ArrayList<ArrayList<String>> path = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                filePath = cursor.getString(3);
                String[] filepaths = filePath.split("@");
                path.add(new ArrayList<String>(Arrays.asList(filepaths)));
            }
            while(cursor.moveToNext());
        }
        return path;
    }
    public ArrayList<ArrayList<String>> getName(){
        String query = "SELECT * FROM " + FOLDER_TABLE;
        String fileName;
        ArrayList<ArrayList<String>> name = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                fileName = cursor.getString(2);
                String[] filename = fileName.split("@");
                name.add(new ArrayList<String>(Arrays.asList(filename)));
            }
            while(cursor.moveToNext());
        }
        return name;
    }
    public void deleteFile(String folderName, int folderPosition, int filePosition){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        ArrayList<String> fileName = getName().get(folderPosition);
        ArrayList<String> filePath = getPaths().get(folderPosition);
        StringBuilder str1 = new StringBuilder();//File Name
        StringBuilder str2 = new StringBuilder();//File Path
        for(int i = 0;i<fileName.size();i++){
           if(i!=filePosition){
               str1.append(fileName.get(i)).append("@");
               str2.append(filePath.get(i)).append("@");
           }
        }
        cv.put(FOLDER_NAME,folderName);
        cv.put(FILE_NAME, String.valueOf(str1));
        cv.put(FILE_PATH, String.valueOf(str2));
        db.update(FOLDER_TABLE, cv ,FOLDER_NAME + "=?", new String[]{folderName});

        if(str1.toString()==""&&str2.toString()==""){
            db.delete(FOLDER_TABLE, FOLDER_NAME + "=?", new String[]{folderName});
        }
        db.close();
    }
    public void deleteFolder(String folderName){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FOLDER_TABLE,FOLDER_NAME+"=?",new String[]{folderName});
        db.close();
    }
}
