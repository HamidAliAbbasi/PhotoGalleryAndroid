package com.example.photoapplication09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.photoapplication09.model.Photo;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    public final Context context = this;
    static List<Photo> photos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        updateList();
    }

    private void updateList() {
        photos = TagsSearchActivity.finalList;
        TableLayout entryList = findViewById(R.id.searchList);
        entryList.invalidate();
        entryList.removeAllViews();

        int i = 0;
        for (Photo p : photos) {
            TableRow row = new TableRow(this);
            row.setClickable(true);
            row.setId(i++);

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
}
