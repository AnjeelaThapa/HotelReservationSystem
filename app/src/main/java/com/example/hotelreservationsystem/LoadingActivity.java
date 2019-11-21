package com.example.hotelreservationsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ProgressBar s = findViewById(R.id.mainSpinner1);
        s.setVisibility(View.VISIBLE);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(LoadingActivity.this,MainActivity.class);
                        LoadingActivity.this.startActivity(mainIntent);
                        LoadingActivity.this.finish();
                    }
                },
                5000
        );
    }
}
