package com.example.android.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Muhamad Ichwan on 11/13/2016.
 */

public class Help extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
    }

    public void backToMain(View v)
    {
        Intent intent = new Intent(Help.this, MainActivity.class);
        startActivity(intent);
    }

    public void finishCapture(View v) {
        Help.this.finish();
    }
}
