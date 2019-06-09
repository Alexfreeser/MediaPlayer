package com.example.mediaplayer;

import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity {

    private final String ROOT_PATH = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.getPath();
    ArrayList<String> fileList;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        listView = findViewById(R.id.listView);
        findSongs(ROOT_PATH);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songPath = adapter.getItem(position);
                Intent intent = new Intent(SongListActivity.this, MainActivity.class);
                intent.putExtra("path", songPath);
                startActivity(intent);
            }
        });
    }

    ArrayList<String> findSongs(String rootPath) {
        fileList = new ArrayList<>();
        try{
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : files) {
                if (file.isDirectory()) {
                    if (findSongs(file.getAbsolutePath()) != null) {
                        fileList.addAll(findSongs(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    fileList.add(file.getAbsolutePath());
                }
            }
            return fileList;
        }catch(Exception e){
            return null;
        }
    }
}
