package com.example.android.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String mActivityName;
    private TextView mStatusView;
    private TextView mStatusAllView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }

    public void capturePicture(View v)
    {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    public void goToMenuHelp(View v)
    {
        Intent intent = new Intent(MainActivity.this, Help.class);
        startActivity(intent);
    }

    public void goToMenuAbout(View v)
    {
        Intent intent = new Intent(MainActivity.this, About.class);
        startActivity(intent);
    }

    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void finishActivityMainActivity(View v) {
        MainActivity.this.finish();
    }
}
