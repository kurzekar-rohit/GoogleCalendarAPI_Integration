package com.example.hp.calender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OpenActivity extends AppCompatActivity {
    TextView descreption;
    TextView summary;
    TextView startDate;
    TextView endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        descreption = (TextView) findViewById(R.id.Id);
        summary = (TextView) findViewById(R.id.Summary);
        startDate = (TextView) findViewById(R.id.StartDate);
        endDate = (TextView) findViewById(R.id.EndDate);

        Intent intent = getIntent();
        String descreptions = intent.getStringExtra("description");
        String summaries = intent.getStringExtra("summary");
        String start = intent.getStringExtra("startDate");
        String end = intent.getStringExtra("endDate");

        summary.setText("Tittle: " + summaries);
        descreption.setText("Note: " + descreptions);
        startDate.setText("Start Time: " + start);
        endDate.setText("End Time: " + end);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
