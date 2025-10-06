package com.example.a1_arinalakshay_mirzakhanidhawan;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Enable the Up (back) button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Payment History");
        }

        // Get the payment history from the Intent
        ArrayList<String> historyList = getIntent().getStringArrayListExtra("payment_history");

        // If the list is null, initialize an empty one and show message
        if (historyList == null) {
            historyList = new ArrayList<>();
            Toast.makeText(this, "No payment data received", Toast.LENGTH_SHORT).show();
        }

        // Find views
        ListView list = findViewById(R.id.listHistory);
        TextView empty = findViewById(R.id.emptyView);

        // Set empty view - will show when historyList is empty
        list.setEmptyView(empty);

        // Create adapter and set it to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                historyList
        );
        list.setAdapter(adapter);

        // Add click listener for individual items
        list.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(this, "Entry " + (position + 1), Toast.LENGTH_SHORT).show());

        // Show count of entries in subtitle
        if (!historyList.isEmpty()) {
            String subtitle = historyList.size() + (historyList.size() == 1 ? " Entry" : " Entries");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setSubtitle(subtitle);
            }
        }
    }

    // Handle Up button click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}