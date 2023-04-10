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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import hu.mobilalk.trainticketapp.routes.RouteItem;
import hu.mobilalk.trainticketapp.routes.RoutesActivity;
import hu.mobilalk.trainticketapp.tickets.TicketsActivity;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    // ANDROID
    SharedPreferences preferences;
    Calendar calendar;

    // FIREBASE
    FirebaseAuth fireAuth;
    FirebaseFirestore firestore;
    CollectionReference citiesCollection;

    // CITY INPUT
    AutoCompleteTextView originACTV;
    AutoCompleteTextView destACTV;
    ArrayAdapter<String> originAdapter;
    ArrayAdapter<String> destAdapter;
    City originCity;
    City destCity;
    List<City> citiesList;
    List<String> cityNamesList;

    // DATE INPUT
    Button dateButton;
    Button timeButton;

    // OTHER INPUT
    RadioGroup discountRadioGroup;
    RadioGroup classRadioGroup;

    // BUTTONS
    Button searchButton;
    Button loginOrTicketButton;
    Button logoutButton;

    // MISC
    View loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ANDROID
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        calendar = Calendar.getInstance();

        // FIREBASE
        fireAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        citiesCollection = firestore.collection("cities");

        // CITY INPUT
        originACTV = findViewById(R.id.originACTV);
        destACTV = findViewById(R.id.destACTV);
        citiesList = new ArrayList<>();
        cityNamesList = new ArrayList<>();

        originAdapter = new ArrayAdapter<>(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                cityNamesList);
        originACTV.setAdapter(originAdapter);
        originACTV.setThreshold(1);
        originACTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                originCity = citiesList.get(i);
                Log.i(LOG_TAG, "Selected destination city: " + destCity.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                originCity = null;
                Log.i(LOG_TAG, "Selected destination city: NONE");
            }
        });

        destAdapter = new ArrayAdapter<>(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                cityNamesList);
        destACTV.setAdapter(destAdapter);
        destACTV.setThreshold(1);
        destACTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                destCity = citiesList.get(i);
                Log.i(LOG_TAG, "Selected destination city: " + destCity.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                destCity = null;
                Log.i(LOG_TAG, "Selected destination city: NONE");
            }
        });

        // DATE INPUT
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
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

        // OTHER INPUT
        discountRadioGroup = findViewById(R.id.discountRadioGroup);
        classRadioGroup = findViewById(R.id.classRadioGroup);
        discountRadioGroup.check(R.id.noDiscountRB);
        classRadioGroup.check(R.id.fastTrainRB);

        // BUTTONS
        loginOrTicketButton = findViewById(R.id.loginOrTicketButton);
        logoutButton = findViewById(R.id.logoutButton);
        searchButton = findViewById(R.id.searchButton);
        logoutButton.setOnClickListener(this::logout);
        searchButton.setOnClickListener(this::search);

        // MISC
        loadingSpinner = findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.VISIBLE);

        queryCities();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fireAuth.getCurrentUser() == null) {
            loginOrTicketButton.setText(R.string.login);
            loginOrTicketButton.setOnClickListener(this::login);
        } else {
            loginOrTicketButton.setText(getString(R.string.my_tickets));
            loginOrTicketButton.setOnClickListener(this::tickets);
        }
    }

    public void queryCities() {
        citiesCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            queryDocumentSnapshots.forEach(document -> {
                citiesList.add(new City(
                        (String) document.get("name"),
                        (Long) document.get("distance"),
                        (Long) document.get("routeID")
                ));
            });
            cityNamesList = citiesList.stream().map(City::getName).collect(Collectors.toList());
            originAdapter.notifyDataSetChanged();
            destAdapter.notifyDataSetChanged();

            loadingSpinner.setVisibility(View.GONE);
            Log.i(LOG_TAG, "SUCCESSFUL city data query!");
        });
    }

    public void search(View view) {
        if (originCity == null || destCity == null) {
            Toast.makeText(this, "Mindkét állomást ki kell választani!", Toast.LENGTH_SHORT).show();
        } else if (originCity.equals(destCity)) {
            Toast.makeText(this, "Válassz különböző állomásokat!", Toast.LENGTH_SHORT).show();
        } else {

            Log.i(LOG_TAG, calendar.getTime().toString());

            Calendar arrive = Calendar.getInstance();
            Long distance = 0L;

            if (originCity.getRouteID() == 0) {
                distance = destCity.getDistance();
                arrive.setTimeInMillis(calendar.getTimeInMillis() + distance / 2 * 60000L);
            } else if (Objects.equals(originCity.getRouteID(), destCity.getRouteID())) {
                distance = Math.abs(originCity.getDistance() - destCity.getDistance());
                arrive.setTimeInMillis(calendar.getTimeInMillis() +
                        distance / 2 * 60000L);
            } else {
                distance = originCity.getDistance() + destCity.getDistance();
                arrive.setTimeInMillis((calendar.getTimeInMillis() +
                        distance / 2 * 60000L));
            }

            Intent searchIntent = new Intent(this, RoutesActivity.class);
            searchIntent.putExtra("searchData", new RouteItem(
                    originCity.getName(),
                    destCity.getName(),
                    calendar.getTime(),
                    arrive.getTime(),
                    discountRadioGroup.indexOfChild(findViewById(discountRadioGroup.getCheckedRadioButtonId())),
                    classRadioGroup.indexOfChild(findViewById(classRadioGroup.getCheckedRadioButtonId())),
                    distance,
                    (int)Math.log(distance)*150));
            startActivity(searchIntent);
        }
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void logout(View view) {
        fireAuth.signOut();
        Log.i(LOG_TAG, "SUCCESSFULLY LOGGED OUT!");


        loginOrTicketButton.setText(R.string.login);
        loginOrTicketButton.setOnClickListener(this::login);
    }

    public void tickets(View view) {
        startActivity(new Intent(this, TicketsActivity.class));
    }
}