package com.example.android.myapplication;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Muhamad Ichwan on 11/13/2016.
 */

public class Capture extends AppCompatActivity{

    static final int REQUEST_IMAGE_CAPTURE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture);
    }

    public void backToMain(View v)
    {
        Intent intent = new Intent(Capture.this, MainActivity.class);
        startActivity(intent);
    }

    public void goCapture(View v)
    {
        Intent intent = new Intent(Capture.this, Preview.class);
        startActivity(intent);
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);

    }

    public void finishCapture(View v) {
        Capture.this.finish();
    }
}
