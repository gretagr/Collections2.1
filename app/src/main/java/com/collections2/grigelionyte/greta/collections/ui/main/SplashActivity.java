package com.collections2.grigelionyte.greta.collections.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, CollectionsActivity.class);
        startActivity(intent);
        finish();
    }
}