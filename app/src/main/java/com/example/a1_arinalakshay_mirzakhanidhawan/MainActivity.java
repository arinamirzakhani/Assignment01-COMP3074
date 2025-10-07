package com.example.a1_arinalakshay_mirzakhanidhawan;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<String> historyList = new ArrayList<>();
    private EditText etHours, etRate;
    private TextView tvPay, tvOvertime, tvGross, tvTax, tvTotal;
    private final NumberFormat currency = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Pay Calculator");
        }

        // Initialize views
        etHours = findViewById(R.id.etHours);
        etRate = findViewById(R.id.etRate);
        Button btnCalculate = findViewById(R.id.btnCalculate);

        tvPay = findViewById(R.id.tvPay);
        tvOvertime = findViewById(R.id.tvOvertime);
        tvGross = findViewById(R.id.tvGross);
        tvTax = findViewById(R.id.tvTax);
        tvTotal = findViewById(R.id.tvTotal);


        btnCalculate.setOnClickListener(v -> calculateAndShow());
    }

    private void calculateAndShow() {
        // Reset previous errors
        etHours.setError(null);
        etRate.setError(null);

        String hoursStr = etHours.getText().toString().trim();
        String rateStr  = etRate.getText().toString().trim();

        // Basic validation
        if (TextUtils.isEmpty(hoursStr)) {
            etHours.setError("Enter hours");
            etHours.requestFocus();
            Toast.makeText(this, "Please enter number of hours.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(rateStr)) {
            etRate.setError("Enter rate");
            etRate.requestFocus();
            Toast.makeText(this, "Please enter hourly rate.", Toast.LENGTH_SHORT).show();
            return;
        }

        double hours, rate;
        try {
            hours = Double.parseDouble(hoursStr);
        } catch (NumberFormatException e) {
            etHours.setError("Invalid number");
            etHours.requestFocus();
            Toast.makeText(this, "Hours must be a valid number.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            rate = Double.parseDouble(rateStr);
        } catch (NumberFormatException e) {
            etRate.setError("Invalid number");
            etRate.requestFocus();
            Toast.makeText(this, "Rate must be a valid number.", Toast.LENGTH_SHORT).show();
            return;
        }

        // More validation rules
        if (hours < 0) {
            etHours.setError("Cannot be negative");
            etHours.requestFocus();
            Toast.makeText(this, "Hours cannot be negative.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (hours == 0) {
            etHours.setError("Cannot be zero");
            etHours.requestFocus();
            Toast.makeText(this, "Please enter hours worked (greater than 0).", Toast.LENGTH_SHORT).show();
            return;
        }
        if (hours > 168) { // > 1 week of hours is unlikely
            etHours.setError("Unrealistic hours");
            etHours.requestFocus();
            Toast.makeText(this, "Please enter realistic weekly hours.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rate < 0) {
            etRate.setError("Cannot be negative");
            etRate.requestFocus();
            Toast.makeText(this, "Hourly rate cannot be negative.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rate == 0) {
            etRate.setError("Cannot be zero");
            etRate.requestFocus();
            Toast.makeText(this, "Please enter hourly rate (greater than 0).", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rate > 2000) { // > $2000 an hour is unlikely
            etRate.setError("Unrealistic rate");
            etRate.requestFocus();
            Toast.makeText(this, "Please enter realistic hourly rate.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculations
        double regularHours = Math.min(hours, 40.0);
        double overtimeHours = Math.max(0.0, hours - 40.0);

        double regularPay = regularHours * rate;
        double overtimePay = overtimeHours * rate * 1.5;

        double grossPay = regularPay + overtimePay;
        double tax = grossPay * 0.18;
        double total = grossPay - tax;

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        // Add to history
        historyList.add(0,
                "Date: " + timestamp + "\n" +
                        "Hours: " + hours + " @ " + currency.format(rate) + "\n" +
                        "Regular Pay: " + currency.format(regularPay) + "\n" +
                        "Overtime Pay: " + currency.format(overtimePay) + "\n" +
                        "Gross Pay: " + currency.format(grossPay) + "\n" +
                        "Tax (18%): " + currency.format(tax) + "\n" +
                        "Net Pay: " + currency.format(total)
        );

        // Show results
        tvPay.setText(getString(R.string.pay_regular, currency.format(regularPay)));
        tvOvertime.setText(getString(R.string.overtime_pay, currency.format(overtimePay)));
        tvGross.setText(getString(R.string.total_gross, currency.format(grossPay)));
        tvTax.setText(getString(R.string.tax_18, currency.format(tax)));
        tvTotal.setText(getString(R.string.total_after_tax, currency.format(total)));

        Toast.makeText(this, "Calculated successfully!", Toast.LENGTH_SHORT).show();

        // Clear input fields
        etHours.getText().clear();
        etRate.getText().clear();
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle menu item click
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_details) {

            if (historyList.isEmpty()) {
                Toast.makeText(this, "No payment history yet. Calculate first!", Toast.LENGTH_SHORT).show();
                return true;
            }

            android.content.Intent intent = new android.content.Intent(this, DetailActivity.class);

            intent.putStringArrayListExtra("payment_history", historyList);


            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Save and restore state
    @Override
    protected void onSaveInstanceState(@NonNull android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("payment_history", historyList);
    }

    // Restore state
    @Override
    protected void onRestoreInstanceState(@NonNull android.os.Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey("payment_history")) {
            ArrayList<String> restored = savedInstanceState.getStringArrayList("payment_history");
            if (restored != null) {
                historyList.clear();
                historyList.addAll(restored);
            }
        }
    }

}