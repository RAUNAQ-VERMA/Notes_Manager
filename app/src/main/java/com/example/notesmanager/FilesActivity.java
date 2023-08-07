package com.example.notesmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.example.notesmanager.adapters.FilesAdapter_RecyclerView;
import com.example.notesmanager.database.Main_DataBaseHelper;
import com.example.notesmanager.models.Folder;

import java.io.File;
import java.util.ArrayList;

public class FilesActivity extends AppCompatActivity {

    ArrayList<Folder> folders;
    int recyclerViewPosition;

    Main_DataBaseHelper db;

    TextView title;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        getSupportActionBar().hide();

        title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.files_rv);

        db = new Main_DataBaseHelper(this);

        folders = db.get();

        recyclerViewPosition = getIntent().getIntExtra("code", 0);

        title.setText(folders.get(recyclerViewPosition).getFolderName());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        FilesAdapter_RecyclerView adapter = new FilesAdapter_RecyclerView(new FilesAdapter_RecyclerView.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                ArrayList<String> path = new ArrayList<>(folders.get(recyclerViewPosition).getPath());
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/example.pdf");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(FilesActivity.this, BuildConfig.APPLICATION_ID + ".provider",new File(path.get(position)));
                intent.setDataAndType(uri,"application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(int position, View v) {
                db.deleteFile(folders.get(recyclerViewPosition).getFolderName(),recyclerViewPosition,position);
                finish();
                startActivity(getIntent());
            }

        },folders.get(recyclerViewPosition).getName());
        recyclerView.setAdapter(adapter);
    }
}