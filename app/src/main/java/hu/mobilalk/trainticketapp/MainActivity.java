package hu.mobilalk.trainticketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import hu.mobilalk.trainticketapp.routes.RouteItem;
import hu.mobilalk.trainticketapp.routes.RoutesActivity;
import hu.mobilalk.trainticketapp.tickets.TicketsActivity;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    // ANDROID
    SharedPreferences preferences;
    Calendar inputDate;

    // FIREBASE
    FirebaseAuth fireAuth;
    FirebaseFirestore firestore;
    CollectionReference citiesCollection;

    // CITY INPUT
    AutoCompleteTextView originACTV;
    AutoCompleteTextView destACTV;
    ArrayAdapter<City> originAdapter;
    ArrayAdapter<City> destAdapter;
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
    BottomNavigationView bottomNav;

    // MISC
    View loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ANDROID
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        inputDate = Calendar.getInstance();

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
                citiesList);
        originACTV.setAdapter(originAdapter);
        originACTV.setThreshold(1);
        originACTV.setOnItemClickListener((adapterView, view, i, l) -> {
            originCity = (City) adapterView.getItemAtPosition(i);
            Log.i(LOG_TAG, "Selected start city: " + originCity);
        });

        destAdapter = new ArrayAdapter<>(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                citiesList);
        destACTV.setAdapter(destAdapter);
        destACTV.setThreshold(1);
        destACTV.setOnItemClickListener((adapterView, view, i, l) -> {
            destCity = (City) adapterView.getItemAtPosition(i);
            Log.i(LOG_TAG, "Selected start city: " + originCity);
        });

        // DATE INPUT
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        dateButton.setText(DateFormat.format("yyyy. MMMM. dd.", inputDate.getTime()));
        dateButton.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (datePicker, year, month, day) -> {
                        inputDate.set(year, month, day);
                        dateButton.setText(DateFormat.format("yyyy. MMMM. dd.", inputDate.getTime()));
                    },
                    inputDate.get(Calendar.YEAR),
                    inputDate.get(Calendar.MONTH),
                    inputDate.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
        timeButton.setText(DateFormat.format("HH:mm", inputDate.getTime()));
        timeButton.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (timePicker, hour, minute) -> {
                        inputDate.set(Calendar.HOUR, hour);
                        inputDate.set(Calendar.MINUTE, minute);
                        timeButton.setText(DateFormat.format("HH:mm", inputDate.getTime()));
                    },
                    inputDate.get(Calendar.HOUR),
                    inputDate.get(Calendar.MINUTE),
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
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this::search);

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                case R.id.tickets:
                    startActivity(new Intent(getApplicationContext(), TicketsActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    return true;
                case R.id.settings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    return true;
                default:
                    return true;
            }
        });
        bottomNav.setOnItemReselectedListener(item -> {

        });

        // MISC
        loadingSpinner = findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.VISIBLE);

        queryCities();

    }

    @Override
    protected void onResume() {
        super.onResume();

        bottomNav.setSelectedItemId(R.id.home);
    }

    public void queryCities() {
        citiesCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            queryDocumentSnapshots.forEach(document -> {
                citiesList.add(new City(
                        document.getString("name"),
                        document.getLong("distance").intValue(),
                        document.getLong("routeID").intValue()
                ));
                cityNamesList.add(document.getString("name"));
            });
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

            Log.i(LOG_TAG, inputDate.getTime().toString());

            Calendar calcDate = Calendar.getInstance();
            Integer distance;

            if (originCity.getRouteID() == 0) {
                distance = destCity.getDistance();
                calcDate.setTimeInMillis(inputDate.getTimeInMillis() + distance / 2 * 60000L);
            } else if (Objects.equals(originCity.getRouteID(), destCity.getRouteID())) {
                distance = Math.abs(originCity.getDistance() - destCity.getDistance());
                calcDate.setTimeInMillis(inputDate.getTimeInMillis() +
                        distance / 2 * 60000L);
            } else {
                distance = originCity.getDistance() + destCity.getDistance();
                calcDate.setTimeInMillis((inputDate.getTimeInMillis() +
                        distance / 2 * 60000L));
            }

            Intent searchIntent = new Intent(this, RoutesActivity.class);
            searchIntent.putExtra("searchData", new RouteItem(
                    originCity.getName(),
                    destCity.getName(),
                    inputDate.getTime(),
                    calcDate.getTime(),
                    discountRadioGroup.indexOfChild(findViewById(discountRadioGroup.getCheckedRadioButtonId())),
                    classRadioGroup.indexOfChild(findViewById(classRadioGroup.getCheckedRadioButtonId())),
                    distance,
                    (int)Math.log(distance)*700));
            searchIntent.putExtra("isDepartDate", true);
            startActivity(searchIntent);
        }
    }

    public void tickets(View view) {
        startActivity(new Intent(this, TicketsActivity.class));
    }
}