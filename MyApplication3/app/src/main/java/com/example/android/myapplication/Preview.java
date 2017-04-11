package com.example.android.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Muhamad Ichwan on 11/16/2016.
 */

public class Preview extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview);
    }

    public void backToCapture(View v)
    {
        Intent intent = new Intent(Preview.this, Capture.class);
        startActivity(intent);
    }

    public void finishCapture(View v) {
        Preview.this.finish();
    }
}
