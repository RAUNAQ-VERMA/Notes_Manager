package com.example.notesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notesmanager.database.Main_DataBaseHelper;
import com.example.notesmanager.models.Folder;

import java.util.ArrayList;

public class CreateFolderActivity extends AppCompatActivity {

    EditText folderName_et;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);

        folderName_et = findViewById(R.id.folderName);
        nextButton = findViewById(R.id.next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderName = folderName_et.getText().toString().toUpperCase();
                ArrayList<String> name = getIntent().getStringArrayListExtra("Name");
                ArrayList<String> path = getIntent().getStringArrayListExtra("Path");

                Log.d("mylog","23  "+name );
                Main_DataBaseHelper db = new Main_DataBaseHelper(CreateFolderActivity.this);
                db.add(new Folder(folderName,name,path));
                Intent intent = new Intent(CreateFolderActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}