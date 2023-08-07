package com.example.notesmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.notesmanager.models.Folder;
import com.example.notesmanager.models.Notes;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper
 {
    public static final String NOTES_TABLE = "Notes_Table";
    public static final String NOTES_ID = "ID";
    public static final String NOTES_TAG = "Notes_Tag";
    public static final String NOTES_PATH = "Notes_Path";

    public DataBaseHelper(@Nullable Context context)
    {
        super(context, "Notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + NOTES_TABLE + "(" + NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NOTES_TAG + " TEXT,"  + NOTES_PATH + " TEXT)";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
    public boolean add(Notes notes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTES_TAG, notes.getTag());
        cv.put(NOTES_PATH, notes.getPath());

        long insert = db.insert(NOTES_TABLE, null, cv);
        db.close();
        return insert != -1;
    }
     public List<String> getName(){
        List<String> name = new ArrayList<>();
        String query = "SELECT * FROM " + NOTES_TABLE;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                name.add(cursor.getString(1));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return name;
     }
     public List<String> getPath(){
         List<String> path = new ArrayList<>();
         String query = "SELECT * FROM " + NOTES_TABLE;
         SQLiteDatabase database = this.getReadableDatabase();
         Cursor cursor = database.rawQuery(query,null);
         if(cursor.moveToFirst()){
             do{
                 path.add(cursor.getString(2));
             }
             while(cursor.moveToNext());
         }
         cursor.close();
         database.close();
         return path;
     }
     public ArrayList<Notes> getNotes(){
         ArrayList<Notes> notes = new ArrayList<>();
         String query = "SELECT * FROM " + NOTES_TABLE;
         SQLiteDatabase database = this.getReadableDatabase();
         Cursor cursor = database.rawQuery(query,null);
         if(cursor.moveToFirst()){
             do{
                 notes.add(new Notes(cursor.getString(2),cursor.getString(1)));
             }
             while(cursor.moveToNext());
         }
         cursor.close();
         database.close();
         return notes;
     }
     public void deleteNotes(String fileTag){
         SQLiteDatabase db = this.getWritableDatabase();
         db.delete(NOTES_TABLE, NOTES_TAG + "=?", new String[]{fileTag});
     }
     public void clear(){
         SQLiteDatabase db = this.getWritableDatabase();
         String query = "DELETE FROM "+NOTES_TABLE;
         db.execSQL(query);
     }
}
