package com.keshava.cloudbrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splashcrn extends AppCompatActivity {
    private static  int splashTimeOut=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashcrn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splashcrn.this,login.class);
                startActivity(i);
                finish();
            }
        },splashTimeOut);

    }
}