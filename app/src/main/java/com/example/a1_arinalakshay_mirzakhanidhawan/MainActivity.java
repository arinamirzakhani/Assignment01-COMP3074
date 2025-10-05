package com.example.a1_arinalakshay_mirzakhanidhawan;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private EditText etHours, etRate;
    private TextView tvPay, tvOvertime, tvGross, tvTax, tvTotal;
    private final NumberFormat currency = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            Toast.makeText(this, "Please enter number of hours.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(rateStr)) {
            etRate.setError("Enter rate");
            Toast.makeText(this, "Please enter hourly rate.", Toast.LENGTH_SHORT).show();
            return;
        }

        double hours, rate;
        try {
            hours = Double.parseDouble(hoursStr);
        } catch (NumberFormatException e) {
            etHours.setError("Invalid number");
            Toast.makeText(this, "Hours must be a valid number.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            rate = Double.parseDouble(rateStr);
        } catch (NumberFormatException e) {
            etRate.setError("Invalid number");
            Toast.makeText(this, "Rate must be a valid number.", Toast.LENGTH_SHORT).show();
            return;
        }

        // More validation rules
        if (hours < 0) {
            etHours.setError("Cannot be negative");
            Toast.makeText(this, "Hours cannot be negative.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (hours > 168) { // > 1 week of hours is unlikely
            etHours.setError("Unrealistic hours");
            Toast.makeText(this, "Please enter realistic weekly hours.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rate < 0) {
            etRate.setError("Cannot be negative");
            Toast.makeText(this, "Hourly rate cannot be negative.", Toast.LENGTH_SHORT).show();
            return;
        }


        double regularHours = Math.min(hours, 40.0);
        double overtimeHours = Math.max(0.0, hours - 40.0);

        double regularPay = regularHours * rate;
        double overtimePay = overtimeHours * rate * 1.5;

        double grossPay = regularPay + overtimePay;
        double tax = grossPay * 0.18;
        double total = grossPay - tax;


        tvPay.setText("Pay (regular): " + currency.format(regularPay));
        tvOvertime.setText("Overtime pay: " + currency.format(overtimePay));
        tvGross.setText("Total (gross): " + currency.format(grossPay));
        tvTax.setText("Tax (18%): " + currency.format(tax));
        tvTotal.setText("Total (after tax): " + currency.format(total));

        Toast.makeText(this, "Calculated successfully!", Toast.LENGTH_SHORT).show();
    }
}