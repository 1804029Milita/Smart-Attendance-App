package com.example.smartattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {
    CardView software;
    CardView softlab;
    CardView dsd;
    CardView dsdlab;
    CardView archi;
    CardView management;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        software = findViewById(R.id.software);
        softlab = findViewById(R.id.softlab);
        dsd = findViewById(R.id.dsd);
        dsdlab = findViewById(R.id.dsdlab);
        archi = findViewById(R.id.archi);
        management = findViewById(R.id.management);

        software.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity(1);
            }
        });

        softlab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity(2);
            }
        });

        dsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity(3);
            }
        });

        dsdlab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity(4);
            }
        });

        archi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity(5);
            }
        });

        management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity(6);
            }
        });
    }

    private void launchMainActivity(int cid) {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.putExtra("cid", cid);
        startActivity(intent);
    }
}
