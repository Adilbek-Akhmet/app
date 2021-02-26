package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    private TextView astana;
    private TextView moskow;
    private BroadcastReceiver minuteUpdateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText temperatureInput = findViewById(R.id.temperatureInput);
        final Spinner from = findViewById(R.id.fromTemperature);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.temperature_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from.setAdapter(adapter);
        final Spinner to = findViewById(R.id.toTemperature);
        to.setAdapter(adapter);
        final TextView result = findViewById(R.id.resultTextView);
        Button submitTemp = findViewById(R.id.submitTemp);

        final EditText currencyInput = findViewById(R.id.currencyInput);
        final Spinner from2 = findViewById(R.id.fromCurrency);
        final Spinner to2 = findViewById(R.id.toCurrency);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.currency_types, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from2.setAdapter(adapter2);
        to2.setAdapter(adapter2);
        final TextView result2 = findViewById(R.id.resultTextView2);
        Button submitCurr = findViewById(R.id.submitCurr);

        astana = findViewById(R.id.astanaTime);

        moskow = findViewById(R.id.moskowTime);

        submitTemp.setOnClickListener(v -> {
            double temp = Double.parseDouble(temperatureInput.getText().toString());
            String choice1 = from.getSelectedItem().toString();
            String choice2 = to.getSelectedItem().toString();
            if (choice1.equals("Celsius")) {
                if (choice2.equals("Kelvin")) {
                    result.setText("Result: " + fromCelsiusToKelvin(temp));
                } else if (choice2.equals("Fahrenheit")) {
                    result.setText("Result: " + fromCelsiusToFahrenheit(temp));
                } else {
                    result.setText("Result: " + temp);
                }
            } else if (choice1.equals("Kelvin")) {
                if (choice2.equals("Celsius")) {
                    result.setText("Result: " + fromKelvinToCelsius(temp));
                } else if (choice2.equals("Fahrenheit")) {
                    result.setText("Result: " + fromKelvinToFahrenheit(temp));
                } else {
                    result.setText("Result: " + temp);
                }
            } else {
                if (choice2.equals("Celsius")) {
                    result.setText("Result: " + fromFahrenheitToCelsius(temp));
                } else if (choice2.equals("Kelvin")) {
                    result.setText("Result: " + fromFahrenheitToKelvin(temp));
                } else {
                    result.setText("Result: " + temp);
                }
            }
        });

        submitCurr.setOnClickListener(v -> {
            double curr = Double.parseDouble(currencyInput.getText().toString());
            String choice1 = from2.getSelectedItem().toString();
            String choice2 = to2.getSelectedItem().toString();
            if (choice1.equals("Tenge") && choice2.equals("Dollar")) {
                result2.setText("Result: " + fromTengeToDollar(curr));
            } else if (choice1.equals("Dollar") && choice2.equals("Tenge")) {
                result2.setText("Result: " + fromDollarToTenge(curr));
            } else {
                result2.setText("Result: " + curr);
            }
        });
    }

    public void startMinuteUpdater() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        minuteUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                astana.setText("Astana: " + getTimeInAstana());
                moskow.setText("Moskow: " + getTimeInMoskow());
            }
        };
        registerReceiver(minuteUpdateReceiver, intentFilter);
    }

    private int fromCelsiusToKelvin(double from) {
        return (int)(from + 273.15);
    }

    private int fromKelvinToCelsius(double from) {
        return (int)(from - 273.15);
    }

    private int fromCelsiusToFahrenheit(double from) {
        return (int)(from * 1.8 + 32);
    }

    private int fromFahrenheitToCelsius(double from) {
        return (int)((from - 32) / 1.8);
    }

    private int fromKelvinToFahrenheit(double from) {
        return fromCelsiusToFahrenheit(fromKelvinToCelsius(from));
    }

    private int fromFahrenheitToKelvin(double from) {
        return fromCelsiusToKelvin(fromFahrenheitToCelsius(from));
    }

    private double fromTengeToDollar(double from) {
        return (from * 0.0024);
    }

    private double fromDollarToTenge(double from) {
        return (from * 417.47);
    }

    private String getTimeInAstana() {
        Instant nowUtc = Instant.now();
        ZoneId astana = ZoneId.of("UTC+06:00");
        ZonedDateTime nowAstana = ZonedDateTime.ofInstant(nowUtc, astana);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return nowAstana.format(formatter);
    }

    private String getTimeInMoskow() {
        Instant nowUtc = Instant.now();
        ZoneId moskow = ZoneId.of("GMT+3");
        ZonedDateTime nowMoskow = ZonedDateTime.ofInstant(nowUtc, moskow);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return nowMoskow.format(formatter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMinuteUpdater();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(minuteUpdateReceiver);
    }
}