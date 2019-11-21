package com.example.hotelreservationsystem;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etCheckInDate, etCheckOutDate, etAdult, etChildren, etRoom;
    Button btnCalculate, btnCheckInDate, btnCheckOutDate;
    Spinner spinLocation, spinRoomType;
    TextView txtCheckInDate, txtCheckOutDate, txtAdult, txtChildren, txtRoom, txtBookedDays, txtLocation,
            txtRoomType, txtTotalPrice;
    ProgressBar pbCircle;

    String[] roomTypes = {"AC", "Deluxe", "Platinum"};
    String[] locations = {"Kathmandu", "Chitwan", "Bhaktapur"};

    private int mYear, mMonth, mDay;
    private double totalPrice, daysDifference = 0.0d;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.bindProperties();
        this.configureListeners();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pbCircle.setProgress(progressStatus);
                        }
                    });
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                pbCircle.setVisibility(View.GONE);
            }

        }, 5000);
    }

    private void bindProperties() {
        this.btnCalculate = findViewById(R.id.btnCalculate);

        this.etCheckInDate = findViewById(R.id.etCheckInDate);
        this.etCheckInDate.setEnabled(false); // So as to disable keyboard popping out

        this.etCheckOutDate = findViewById(R.id.etCheckOutDate);
        this.etCheckOutDate.setEnabled(false); // So as to disable keyboard popping out

        this.etAdult = findViewById(R.id.etAdult);
        this.etChildren = findViewById(R.id.etChildren);
        this.etRoom = findViewById(R.id.etRoom);
        this.spinLocation = findViewById(R.id.spinLocation);
        this.spinRoomType = findViewById(R.id.spinRoomType);
        this.btnCheckInDate = findViewById(R.id.btnCheckInDate);
        this.btnCheckOutDate = findViewById(R.id.btnCheckOutDate);

        this.txtAdult = findViewById(R.id.txtAdult);
        this.txtBookedDays = findViewById(R.id.txtBookedDays);
        this.txtChildren = findViewById(R.id.txtChildren);
        this.txtRoom = findViewById(R.id.txtRoom);
        this.txtCheckInDate = findViewById(R.id.txtCheckInDate);
        this.txtCheckOutDate = findViewById(R.id.txtCheckOutDate);
        this.txtRoomType = findViewById(R.id.txtRoomType);
        this.txtLocation = findViewById(R.id.txtLocation);
        this.txtTotalPrice = findViewById(R.id.txtTotalPrice);

        this.pbCircle = findViewById(R.id.pbCircle);
    }

    private void setSpinner(Spinner spinner, String[] spinnerArray) {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, spinnerArray);
        spinner.setAdapter(adapter);
    }

    private void configureListeners() {
        // Setting options to spinner
        this.setSpinner(this.spinLocation, this.locations);
        this.setSpinner(this.spinRoomType, this.roomTypes);

        // Date time picker configuration
        this.btnCheckInDate.setOnClickListener(this);
        this.btnCheckOutDate.setOnClickListener(this);

        // Calculate
        this.btnCalculate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCheckInDate:
                this.datePicker(this.etCheckInDate);
                break;

            case R.id.btnCheckOutDate:
                this.datePicker(this.etCheckOutDate);
                break;

            case R.id.btnCalculate:
                    daysDifference = this.setDifferenceDays();

                try {
                    double test = Double.parseDouble(this.etAdult.getText().toString());
                } catch (Exception e) {
                    this.etAdult.setError("Invalid Adult Number!");
                    return;
                }

                try {
                    double test = Double.parseDouble(this.etChildren.getText().toString());
                } catch (Exception e) {
                    this.etChildren.setError("Invalid Children Number!");
                    return;
                }

                try {
                    double test = Double.parseDouble(this.etRoom.getText().toString());
                } catch (Exception e) {
                    this.etRoom.setError("Invalid Room Number!");
                    return;
                }

                switch (this.spinRoomType.getSelectedItem().toString()) {
                    case "AC":
                        this.totalPrice = (3000.0d * (Double.parseDouble(this.etAdult.getText().toString()) +
                                Double.parseDouble(this.etChildren.getText().toString())) * daysDifference *
                                Double.parseDouble(this.etRoom.getText().toString())) * 0.13d * 0.10d;
                        break;

                    case "Deluxe":
                        this.totalPrice = (2000.0d * (Double.parseDouble(this.etAdult.getText().toString()) +
                                Double.parseDouble(this.etChildren.getText().toString())) * daysDifference *
                                Double.parseDouble(this.etRoom.getText().toString())) * 0.13d * 0.10d;
                        break;

                    case "Platinum":
                        this.totalPrice = (4000.0d * (Double.parseDouble(this.etAdult.getText().toString()) +
                                Double.parseDouble(this.etChildren.getText().toString())) * daysDifference *
                                Double.parseDouble(this.etRoom.getText().toString())) * 0.13d * 0.10d;
                        break;
                }
                this.txtLocation.setText(this.spinLocation.getSelectedItem().toString());
                this.txtAdult.setText(this.etAdult.getText().toString());
                this.txtChildren.setText(this.etChildren.getText().toString());
                this.txtRoom.setText(this.etRoom.getText().toString());
                this.txtRoomType.setText(this.spinRoomType.getSelectedItem().toString());
                this.txtCheckInDate.setText(this.etCheckInDate.getText().toString());
                this.txtCheckOutDate.setText(this.etCheckOutDate.getText().toString());
                this.txtBookedDays.setText(String.valueOf(this.daysDifference));
                this.txtTotalPrice.setText(String.valueOf(this.totalPrice));
                break;
        }
    }

    private double setDifferenceDays() {
        Date checkInDate, checkOutDate;

        try {
            checkInDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(this.etCheckInDate.getText().toString());
        } catch (Exception e) {
            this.etCheckInDate.setError("Invalid Date");
            return 0.0d;
        }

        try {
            checkOutDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(this.etCheckOutDate.getText().toString());
        } catch (Exception e) {
            this.etCheckOutDate.setError("Invalid Date");
            return 0.0d;
        }
        return TimeUnit.MILLISECONDS.toDays(checkOutDate.getTime() - checkInDate.getTime());
    }

    private void datePicker(EditText et) {
        final EditText innerEt = et;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        this.etCheckInDate.setError(null);
        this.etCheckOutDate.setError(null);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        innerEt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
