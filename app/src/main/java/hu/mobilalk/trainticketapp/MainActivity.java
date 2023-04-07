package hu.mobilalk.trainticketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    SharedPreferences preferences;
    Calendar calendar;

    FirebaseAuth fireAuth;
    FirebaseFirestore firestore;
    CollectionReference citiesCollection;

    AutoCompleteTextView fromACTV;
    AutoCompleteTextView toACTV;
    List<String> citiesList;
    String fromCity;
    String toCity;

    Button dateButton;
    Button timeButton;

    RadioGroup discountRadioGroup;
    RadioGroup classRadioGroup;

    Button searchButton;

    Button loginOrTicketButton;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        calendar = Calendar.getInstance();

        fireAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        citiesCollection = firestore.collection("cities");

        fromACTV = findViewById(R.id.fromACTV);
        toACTV = findViewById(R.id.toACTV);
        citiesList = new ArrayList<>();

        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);

        discountRadioGroup = findViewById(R.id.discountRadioGroup);
        classRadioGroup = findViewById(R.id.classRadioGroup);

        loginOrTicketButton = findViewById(R.id.loginOrTicketButton);
        logoutButton = findViewById(R.id.logoutButton);
        searchButton = findViewById(R.id.searchButton);

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                citiesList);
        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                citiesList);

        fromACTV.setAdapter(fromAdapter);
        fromACTV.setThreshold(1);
        fromACTV.setOnItemClickListener((adapterView, view, i, l) -> {
            fromCity = (String) adapterView.getItemAtPosition(i);
            Log.i(LOG_TAG, "Selected start city: " + fromCity);
        });

        toACTV.setAdapter(toAdapter);
        toACTV.setThreshold(1);
        toACTV.setOnItemClickListener((adapterView, view, i, l) -> {
            toCity = (String) adapterView.getItemAtPosition(i);
            Log.i(LOG_TAG, "Selected destination city: " + toCity);
        });

        dateButton.setText(DateFormat.format("yyyy. MMMM. dd.", calendar.getTime()));
        dateButton.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (datePicker, year, month, day) -> {
                        calendar.set(year, month, day);
                        dateButton.setText(DateFormat.format("yyyy. MMMM. dd.", calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        timeButton.setText(DateFormat.format("HH:mm", calendar.getTime()));
        timeButton.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (timePicker, hour, minute) -> {
                        calendar.set(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH),
                                hour,
                                minute);
                        timeButton.setText(DateFormat.format("HH:mm", calendar.getTime()));
                    },
                    calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    true
            );
            timePickerDialog.show();
        });

        discountRadioGroup.check(R.id.noDiscountRB);
        classRadioGroup.check(R.id.fastTrainRB);

        if (fireAuth.getCurrentUser() == null) {
            loginOrTicketButton.setText(R.string.login);
            loginOrTicketButton.setOnClickListener(this::login);
        } else {
            loginOrTicketButton.setText(getString(R.string.my_tickets));
            loginOrTicketButton.setOnClickListener(this::tickets);
        }
        logoutButton.setOnClickListener(this::logout);

        searchButton.setOnClickListener(this::search);

        queryCities();

    }

    public void search(View view) {
        if (fromCity == null || toCity == null) {
            Toast.makeText(this, "Az indulási és célállomást is ki kell választani!", Toast.LENGTH_SHORT).show();
        } else if (fromCity.equals(toCity)) {
            Toast.makeText(this, "Válassz különböző állomásokat!", Toast.LENGTH_SHORT).show();
        } else {

            Log.i(LOG_TAG, calendar.getTime().toString());

            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putString("fromCity", fromCity);
            editor.putString("toCity", toCity);
            editor.putString("datetime", calendar.getTime().toString());
            editor.putInt("discount", discountRadioGroup.indexOfChild(findViewById(discountRadioGroup.getCheckedRadioButtonId())));
            editor.putInt("class", classRadioGroup.indexOfChild(findViewById(classRadioGroup.getCheckedRadioButtonId())));
            editor.apply();
            startActivity(new Intent(this, TrainListActivity.class));
        }
    }

    public void login(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.putExtra("SECRET_KEY", 99);
        startActivity(loginIntent);
    }

    public void logout(View view) {
        fireAuth.signOut();
        Log.i(LOG_TAG, "SUCCESSFULLY LOGGED OUT!");
        loginOrTicketButton.setText(R.string.login);
        loginOrTicketButton.setOnClickListener(this::login);
    }

    public void queryCities() {
        citiesCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
           for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
               String city = Objects.requireNonNull(document.get("name")).toString();
               Log.i(LOG_TAG, city);
               citiesList.add(city);
           }
        });
    }

    public void tickets(View view) {
        startActivity(new Intent(this, TicketsActivity.class));
    }
}