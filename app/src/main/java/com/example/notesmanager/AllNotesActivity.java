package com.example.notesmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesmanager.adapters.AllNotesAdapter_RecyclerView;
import com.example.notesmanager.database.DataBaseHelper;
import com.example.notesmanager.models.Notes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.util.ArrayList;

public class AllNotesActivity extends AppCompatActivity {


    Toolbar toolbar;
    FloatingActionButton nextButton;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    AllNotesAdapter_RecyclerView adapter;
    DataBaseHelper databaseHelper;
    ImageView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);
        getSupportActionBar().hide();

        toolbar = findViewById(R.id.toolbar2);
        nextButton = findViewById(R.id.next2);
        refresh = findViewById(R.id.refresh);

        databaseHelper = new DataBaseHelper(this);
        ArrayList<Notes> dataset = new ArrayList<>();
        ArrayList<String> name = (ArrayList<String>) databaseHelper.getName();
        ArrayList<String> path = (ArrayList<String>) databaseHelper.getPath();
        for(int i = 0; i<name.size();i++){
            dataset.add(new Notes(path.get(i),name.get(i)));
        }


        //Next Button------------------------------------------------------------------------------------------------------------------------
        nextButton.setVisibility(View.INVISIBLE);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllNotesActivity.this,CreateFolderActivity.class);

                ArrayList<String> selectedName = new ArrayList<>();
                ArrayList<String> selectedPath = new ArrayList<>();

                for(int i = 0; i<dataset.size();i++){
                    if(dataset.get(i).isSelected()){
                        selectedName.add(dataset.get(i).getTag());
                        selectedPath.add(dataset.get(i).getPath());
                    }
                }
                intent.putStringArrayListExtra("Name", selectedName);
                intent.putStringArrayListExtra("Path", selectedPath);
                try {
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(AllNotesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("mylog",e.getMessage());
                }
            }
        });

        //Top Navigation bar------------------------------------------------------------------------------------------------------
        bottomNavigationView = findViewById(R.id.bottomNavigation2);
        bottomNavigationView.setSelectedItemId(R.id.allNotes);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.myNotes:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.allNotes:
                        return true;
                }
                return false;
            }
        });

        //Recycler View----------------------------------------------------------------------------------------------
        recyclerView = findViewById(R.id.recyclerView2);
        adapter = new AllNotesAdapter_RecyclerView(new AllNotesAdapter_RecyclerView.ClickListener() {

            @Override
            public void onItemClick(int position, View v) {
                ArrayList<String> path = new ArrayList<>(databaseHelper.getPath());
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/example.pdf");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(AllNotesActivity.this, BuildConfig.APPLICATION_ID + ".provider",new File(path.get(position)));
                intent.setDataAndType(uri,"application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                nextButton.setVisibility(View.VISIBLE);
            }
        }, dataset);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllNotesActivity.this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        //--------------------------------------------------------------------------------------------------------------------------

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.clear();
                ArrayList<File> myPdfs = fetchPDF(Environment.getExternalStorageDirectory());
                for(int i = 0; i<myPdfs.size(); i++){
                    Notes notes = new Notes(myPdfs.get(i).getAbsolutePath(),myPdfs.get(i).getName());
                    databaseHelper.add(notes);
                }
            }
        });
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