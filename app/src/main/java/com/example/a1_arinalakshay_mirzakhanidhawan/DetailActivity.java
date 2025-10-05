package com.example.a1_arinalakshay_mirzakhanidhawan;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;



public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show Up button
        }

        ListView list = findViewById(R.id.listHistory);
        TextView empty = findViewById(R.id.emptyView);
        list.setEmptyView(empty);

        list.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                MainActivity.historyList
        ));
    }

    //Todo add back button
    @Override
    public boolean onSupportNavigateUp() {
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
}