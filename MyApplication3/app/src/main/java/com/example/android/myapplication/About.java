package com.example.android.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Muhamad Ichwan on 11/13/2016.
 */

public class About extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }

    public void backToMain(View v)
    {
        Intent intent = new Intent(About.this, MainActivity.class);
        startActivity(intent);
    }

    public void finishCapture(View v) {
        About.this.finish();
    }
}
