package com.example.photoapplication09;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.photoapplication09.model.Photo;
import com.example.photoapplication09.model.handleFiles;

import java.util.ArrayList;
import java.util.List;

public class EditTagsActivity extends AppCompatActivity {
    List<Photo> photos;
    int i;
    String album;
    public final Context context = this;

    ListView persons;
    ListView locations;
    Button addPersonButton;
    Button addLocationButton;
    Button removePersonButton;
    Button removeLocationButton;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tags);

    persons =(ListView)findViewById(R.id.persons);
    locations =(ListView)findViewById(R.id.locations);
    addPersonButton = (Button)findViewById(R.id.addPersonButton);
    addLocationButton = (Button)findViewById(R.id.addLocationButton);
    removePersonButton = (Button)findViewById(R.id.removePersonButton);
    removeLocationButton = (Button)findViewById(R.id.removeLocationButton);


    photos = AlbumActivity.photos;
    Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

        i = bundle.getInt("index");
        album = bundle.getString("album");
        refreshAlbums();
    }

        addPersonButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditTagsActivity.this);
            final View mView = getLayoutInflater().inflate(R.layout.dialogs, null);
            final EditText albumNameUser = (EditText) mView.findViewById(R.id.editAlbumName);
            Button mAddAlbum = (Button) mView.findViewById(R.id.AddAlbum);

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            mAddAlbum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    if(!albumNameUser.getText().toString().isEmpty() && !AlbumActivity.photos.get(i).getTags().get("person").contains(albumNameUser.getText().toString())){
                        AlbumActivity.photos.get(i).addTag("person", albumNameUser.getText().toString());
                        dialog.dismiss();

                    } else {
                        Toast.makeText(EditTagsActivity.this, "Error Creating", Toast.LENGTH_SHORT).show();
                    }
                    refreshAlbums();
                }
            });
            refreshAlbums();
        }
    });

        addLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditTagsActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialogs, null);
                final EditText albumNameUser = (EditText) mView.findViewById(R.id.editAlbumName);
                Button mAddAlbum = (Button) mView.findViewById(R.id.AddAlbum);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mAddAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        if(!albumNameUser.getText().toString().isEmpty() && !AlbumActivity.photos.get(i).getTags().get("location").contains(albumNameUser.getText().toString())){
                            AlbumActivity.photos.get(i).addTag("location", albumNameUser.getText().toString());
                            dialog.dismiss();

                        } else {
                            Toast.makeText(EditTagsActivity.this, "Error Creating", Toast.LENGTH_SHORT).show();
                        }
                        refreshAlbums();
                    }
                });
                refreshAlbums();

            }
        });

        removeLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditTagsActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.remove_dialogs, null);
                final EditText albumNameUser = (EditText) mView.findViewById(R.id.removeAlbumName);
                Button mAddAlbum = (Button) mView.findViewById(R.id.removeAlbum);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mAddAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        if(!albumNameUser.getText().toString().isEmpty() && AlbumActivity.photos.get(i).getTags().get("location").contains(albumNameUser.getText().toString())){
                            AlbumActivity.photos.get(i).deleteTagValue("location", albumNameUser.getText().toString());
                            dialog.dismiss();

                        } else {
                            Toast.makeText(EditTagsActivity.this, "Error Creating", Toast.LENGTH_SHORT).show();
                        }
                        refreshAlbums();
                    }
                });
                refreshAlbums();

            }
        });

        removePersonButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditTagsActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.remove_dialogs, null);
                final EditText albumNameUser = (EditText) mView.findViewById(R.id.removeAlbumName);
                Button mAddAlbum = (Button) mView.findViewById(R.id.removeAlbum);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mAddAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        if(!albumNameUser.getText().toString().isEmpty() && AlbumActivity.photos.get(i).getTags().get("person").contains(albumNameUser.getText().toString())){
                            AlbumActivity.photos.get(i).deleteTagValue("person", albumNameUser.getText().toString());
                            dialog.dismiss();

                        } else {
                            Toast.makeText(EditTagsActivity.this, "Error Creating", Toast.LENGTH_SHORT).show();
                        }
                        refreshAlbums();
                    }
                });
                refreshAlbums();

            }
        });






}

    private void refreshAlbums(){
        List<String> listOfPersons = new ArrayList<String>(AlbumActivity.photos.get(i).getTags().get("person"));
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listOfPersons);
        persons.setAdapter(arrayAdapter);

        List<String> listOfLocations = new ArrayList<String>(AlbumActivity.photos.get(i).getTags().get("location"));
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listOfLocations);
        locations.setAdapter(arrayAdapter2);

        String albumFileName = String.format(handleFiles.ALBUM_PATH_FORMAT, album);
        handleFiles.writeSerializedObjectToFile(context, AlbumActivity.photos, albumFileName);

    }

    private void AddPersonView(View view){

    }
}
