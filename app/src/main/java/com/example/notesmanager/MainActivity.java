package com.example.notesmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesmanager.adapters.MyNotesAdapter_RecyclerView;
import com.example.notesmanager.database.DataBaseHelper;
import com.example.notesmanager.database.Main_DataBaseHelper;
import com.example.notesmanager.models.Folder;
import com.example.notesmanager.models.Notes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Folder> folders;

    Main_DataBaseHelper mainDb;
    BottomNavigationView bottomNavigationView;
    DataBaseHelper dataBaseHelper;
    RecyclerView recyclerView;
    MyNotesAdapter_RecyclerView adapter;
    FloatingActionButton addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addButton = findViewById(R.id.add_btn);
        dataBaseHelper = new DataBaseHelper(this);
        mainDb = new Main_DataBaseHelper(this);

        //Folder Work---------------------------------------------------------------------------------------------------
        folders = new ArrayList<>();
        Main_DataBaseHelper db = new Main_DataBaseHelper(MainActivity.this);
        folders = db.get();
        //---------------------------------------------------------------------------------------------------------

        //Top Navigation --------------------------------------------------------------------------------------------
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.myNotes);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.myNotes:
                        return true;
                    case R.id.allNotes:
                        startActivity(new Intent(getApplicationContext(), AllNotesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        getSupportActionBar().hide();
        //----------------------------------------------------------------------------------------------------------

        //Permissions-----------------------------------------------------------------------------------------------------------
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if(dataBaseHelper.getNotes().isEmpty()){
                ArrayList<File> myPdfs = fetchPDF(Environment.getExternalStorageDirectory());
                for(int i = 0; i<myPdfs.size(); i++){
                    Notes notes = new Notes(myPdfs.get(i).getAbsolutePath(),myPdfs.get(i).getName());
                    dataBaseHelper.add(notes);
                    }
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

        // Adding new files----------------------------------------------------------------------------------
        List<String> fileNames = dataBaseHelper.getName();
        ArrayList<File> newFiles = fetchPDF(Environment.getExternalStorageDirectory());
        for(int i = 0; i < newFiles.size();i++ ){
            if(!fileNames.contains(newFiles.get(i).getName())){
                Notes notes = new Notes(newFiles.get(i).getAbsolutePath(),newFiles.get(i).getName());
                dataBaseHelper.add(notes);
            }
        }

        //---------------------------------------------------------------------------------------------------------------------------

        //Recycler View------------------------------------------------------------------------
        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        adapter =new MyNotesAdapter_RecyclerView(new MyNotesAdapter_RecyclerView.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(MainActivity.this,FilesActivity.class);
                intent.putExtra("code",position);
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(int position, View v) {
                mainDb.deleteFolder(folders.get(position).getFolderName());
                finish();
                startActivity(getIntent());
            }
        },folders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //-----------------------------------------------------------------
        //Add Button-----------------------------------------------------
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AllNotesActivity.class));
            }
        });
        //---------------------------------------------------------------
    }
    public static ArrayList<File> fetchPDF(File file){
    ArrayList<File> arrayList = new ArrayList<>();
    File[] pdfs = file.listFiles();
    if(pdfs != null){
        for(File myFile :pdfs){
            if(myFile.isDirectory()&&!myFile.isHidden()){
                arrayList.addAll(fetchPDF(myFile));
            }
            else{
                if(myFile.getName().endsWith(".pdf")){
                    arrayList.add(myFile);
                }
            }
        }
    }
    return arrayList;
}
}