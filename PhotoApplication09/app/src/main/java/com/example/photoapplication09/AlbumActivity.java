package com.example.photoapplication09;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.photoapplication09.model.Photo;
import com.example.photoapplication09.model.handleFiles;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {
    TextView albumNameUser;

    public static String albumName;
    public final Context context = this;
    Uri currrentImageURI;

    static List<Photo> photos;
    View selected;

    private void updateList() {
        photos = handleFiles.readSerializedObjectFromFile(this, String.format(handleFiles.ALBUM_PATH_FORMAT, albumName));
        TableLayout entryList = findViewById(R.id.entryList);
        entryList.invalidate();
        entryList.removeAllViews();

        int i = 0;
        for (Photo p : photos) {
            TableRow row = new TableRow(this);
            row.setClickable(true);
            row.setId(i++);
            row.setOnClickListener(new View.OnClickListener() {
                private void resetBackgroundColors(int idToSkip, TableLayout table) {
                    int tableSize = table.getChildCount();
                    for (int i = 0; i < tableSize; i++) {
                        if (i != idToSkip) table.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }

                @Override
                public void onClick(View view) {
                    view.setBackgroundColor(Color.LTGRAY);
                    resetBackgroundColors(view.getId(), (TableLayout) view.getParent());
                    selected = view;
                }
            });
            row.setPadding(0, 5, 0, 5);
            TextView photoLocation = new TextView(this);
            photoLocation.setText(p.getLocation());

            ImageView image = new ImageView(this);
            image.setPadding(5, 0, 5, 0);
            image.setImageBitmap(BitmapFactory.decodeFile(p.getLocation()));
            row.addView(image);
            row.addView(photoLocation);
            entryList.addView(row);
        }
        entryList.refreshDrawableState();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        albumNameUser = (TextView) findViewById(R.id.AlbumName);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            albumNameUser.setText(bundle.getString("AlbumName"));
            albumName = albumNameUser.getText().toString();
        }
        updateList();
    }

    public void addPhotoToApp(View view) {
        try {
            if (ActivityCompat.checkSelfPermission(AlbumActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AlbumActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add Photo");
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);


                TableLayout entryList = findViewById(R.id.entryList);
                updateList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removePhotoFromApp(View view) {
        if (selected != null) {
            String albumFileName = String.format(handleFiles.ALBUM_PATH_FORMAT, albumName);
            TableLayout table = (TableLayout) selected.getParent();

            photos.remove(selected.getId());
            table.refreshDrawableState();
            handleFiles.writeSerializedObjectToFile(this, photos, albumFileName);
            updateList();
        }
    }

    public void changeToSlideshowActivity(View view) {
        Intent intent = new Intent(this, SlideshowActivity.class);
        intent.putExtra("album",albumName);
        if(selected != null){
            intent.putExtra("i",selected.getId());
        } else {
            intent.putExtra("i",0);

        }

        startActivity(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            currrentImageURI = data.getData();
            String textInput = getRealPathFromURI(currrentImageURI);
            File file = new File(textInput);
            String albumFileName;
            if (file.isFile()) {
                try {
                    albumFileName = String.format(handleFiles.ALBUM_PATH_FORMAT, albumName);
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MILLISECOND, 0);

                    Photo photo = new Photo(textInput, new Date(file.lastModified()));
                    List<Photo> photosInAlbum = handleFiles.readSerializedObjectFromFile(context, albumFileName);
                    photosInAlbum.add(photo);
                    handleFiles.writeSerializedObjectToFile(context, photosInAlbum, albumFileName);
                } catch (Exception e) {
                    String msg = "Error writing to file";
                    throw new RuntimeException(msg, e);
                }
            } else {
                handleFiles.displayAlert(context, "Error", "Photo not found");
            }


        }
        TableLayout entryList = findViewById(R.id.entryList);
        updateList();
    }

    public String getRealPathFromURI(Uri contentUri) {

        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = this.context.getContentResolver().query( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}
