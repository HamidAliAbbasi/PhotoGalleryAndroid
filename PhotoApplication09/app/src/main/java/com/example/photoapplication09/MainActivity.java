package com.example.photoapplication09;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.photoapplication09.model.Photo;
import com.example.photoapplication09.model.handleFiles;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button addAlbumButton;
    Button RemoveAlbumButton;
    Button tagSearch;
    ListView albums;

    public static HashMap<String, HashSet<Photo>> albumsList = new HashMap<String, HashSet<Photo>>();
    private static final String ALBUM_FILE_NAME = "albums.txt";
    public static HashSet<Photo> currentAlbum = null;
    private final Context context = this;
    private static final int PICK_FROM_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tagSearch = findViewById(R.id.searchTags);
        handleFiles.readFile(ALBUM_FILE_NAME, MainActivity.this);
        addAlbumDialog();

        albums.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int i, long l){
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                }
                currentAlbum = albumsList.get(albums.getItemAtPosition(i).toString());
                Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                intent.putExtra("AlbumName", albums.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });

        tagSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, TagsSearchActivity.class);
                startActivity(intent);
            }
        });


    }



    public void refreshAlbums(){
        List<String> listOfAlbums = new ArrayList<String>(albumsList.keySet());
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listOfAlbums);
        albums.setAdapter(arrayAdapter);
    }

    public void addAlbumDialog(){
        addAlbumButton = findViewById(R.id.add_album);
        albums=(ListView)findViewById(R.id.albums);
        addAlbumButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialogs, null);
                final EditText albumNameUser = (EditText) mView.findViewById(R.id.editAlbumName);
                Button mAddAlbum = (Button) mView.findViewById(R.id.AddAlbum);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mAddAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        if(!albumNameUser.getText().toString().isEmpty() && !albumsList.containsKey(albumNameUser.getText().toString())){
                            addAlbum(albumNameUser, mView, dialog);
                        } else {
                            Toast.makeText(MainActivity.this, "Error Creating Album", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        refreshAlbums();
    }

    public void addAlbum(EditText albumNameUser, View mView, AlertDialog dialog){
        albumsList.put(albumNameUser.getText().toString(), new HashSet<Photo>());
        List<String> albumNames = new ArrayList<>(albumsList.keySet());
        refreshAlbums();
        FileOutputStream fos = null;
        try {
            handleFiles.writeToFile(ALBUM_FILE_NAME, albumNames, MainActivity.this);
            Toast.makeText(MainActivity.this, "New Album Created", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mView.setVisibility(View.GONE);
        dialog.dismiss();
    }

    public void removeAlbum(View view) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Remove Album");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textInput = input.getText().toString();
                if (textInput.isEmpty() ||  !albumsList.containsKey(textInput)) {
                    System.out.println("Empty");
                    handleFiles.displayAlert(context, "Error", "Invalid album name");
                } else {
                    try {
                        String albumFileName = String.format(handleFiles.ALBUM_PATH_FORMAT, textInput);
                        handleFiles.deleteFile(context, albumFileName);
                        albumsList.remove(textInput);
                        List<String> albumNames = new ArrayList<>(albumsList.keySet());
                        refreshAlbums();
                        handleFiles.writeToFile(ALBUM_FILE_NAME, albumNames, MainActivity.this);
//                        handleFiles.displayAlert(context, "Confirm", "Please reopen album to see image");
                    } catch (Exception e) {
                        String msg = "Error writing to file";
                        throw new RuntimeException(msg, e);
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
