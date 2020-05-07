package com.example.photoapplication09;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddPhotoActivity extends AppCompatActivity {
    TextView addPhotoLabel;
    EditText photoPathEntry;
    Button addPhotoButton;

    private static final String ADD_PHOTO_LABEL_FORMAT = "Add photo to your %s album";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
    }

    public void addPhotoToAlbum(View view) {
        System.out.println(addPhotoButton.getText());

    }
}
