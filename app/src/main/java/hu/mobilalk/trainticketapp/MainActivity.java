package hu.mobilalk.trainticketapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hu.mobilalk.trainticketapp.enums.Comfort;
import hu.mobilalk.trainticketapp.enums.Discount;
import hu.mobilalk.trainticketapp.models.City;
import hu.mobilalk.trainticketapp.routes.RoutesActivity;
import hu.mobilalk.trainticketapp.tickets.TicketsActivity;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = Objects.requireNonNull(MainActivity.class.getPackage()).toString();

    // ANDROID
    Gson gson = new Gson();
    SharedPreferences preferences;
    Calendar inputDate;
    Calendar currentDate;

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

    // DATE INPUT
    Button dateButton;
    Button timeButton;

    // OTHER INPUT
    RadioGroup isDepartDateRadioGroup;
    RadioGroup discountRadioGroup;
    RadioGroup comfortRadioGroup;

    // BUTTONS
    Button searchButton;

    // MISC
    View loadingSpinner;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ANDROID
        preferences = this.getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        currentDate = Calendar.getInstance();
        inputDate = Calendar.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();

        // FIREBASE
        fireAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        citiesCollection = firestore.collection("cities");

        // LOADING SPINNER
        loadingSpinner = findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.VISIBLE);

        // GETTING CITIES LIST
        if (savedInstanceState != null) {
            citiesList = gson.fromJson(
                    savedInstanceState.getString("citiesList"),
                    new TypeToken<List<City>>() {
                    }.getType());
            loadingSpinner.setVisibility(View.GONE);
        }

        if (citiesList == null) {
            citiesList = new ArrayList<>();
            queryCities();
        }

        // CITY INPUT
        originACTV = findViewById(R.id.originACTV);
        destACTV = findViewById(R.id.destACTV);

        originAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                citiesList);
        originACTV.setAdapter(originAdapter);
        originACTV.setThreshold(1);
        originACTV.setOnItemClickListener((adapterView, view, i, l) -> {
            originCity = (City) adapterView.getItemAtPosition(i);
            Log.i(LOG_TAG, "Selected origin city: " + originCity);
        });

        destAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                citiesList);
        destACTV.setAdapter(destAdapter);
        destACTV.setThreshold(1);
        destACTV.setOnItemClickListener((adapterView, view, i, l) -> {
            destCity = (City) adapterView.getItemAtPosition(i);
            Log.i(LOG_TAG, "Selected destination city: " + originCity);
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
                        inputDate.set(Calendar.HOUR_OF_DAY, hour);
                        inputDate.set(Calendar.MINUTE, minute);
                        timeButton.setText(DateFormat.format("HH:mm", inputDate.getTime()));
                    },
                    inputDate.get(Calendar.HOUR_OF_DAY),
                    inputDate.get(Calendar.MINUTE),
                    true
            );
            timePickerDialog.show();
        });

        // OTHER INPUT
        isDepartDateRadioGroup = findViewById(R.id.isDepartDateRadioGroup);
        isDepartDateRadioGroup.check(R.id.departRB);
        discountRadioGroup = findViewById(R.id.discountRadioGroup);
        discountRadioGroup.check(R.id.noDiscountRB);
        comfortRadioGroup = findViewById(R.id.comfortRadioGroup);
        comfortRadioGroup.check(R.id.fastTrainRB);

        // BUTTONS
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this::search);

        // MISC
        // Bottom nav should be done with fragments though...
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.home);
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    return true;
                case R.id.tickets:
                    startActivity(new Intent(getApplicationContext(), TicketsActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    return true;
                case R.id.settings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    return true;
                default:
                    return true;
            }
        });
        bottomNav.setOnItemReselectedListener(item -> {
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // LOAD SAVED CITY SELECTIONS
        originCity = gson.fromJson(preferences.getString("originCity", ""), City.class);
        destCity = gson.fromJson(preferences.getString("destCity", ""), City.class);

        bottomNav.setSelectedItemId(R.id.home);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // SAVE SELECTED CITIES
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("originCity", gson.toJson(originCity));
        editor.putString("destCity", gson.toJson(destCity));
        editor.apply();

        Log.i(LOG_TAG, "ON_PAUSE_MAIN");

        originACTV.dismissDropDown();
        destACTV.dismissDropDown();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("citiesList", gson.toJson(citiesList));
    }

    public void queryCities() {
        citiesCollection.orderBy("name").get().addOnSuccessListener(queryDocumentSnapshots -> {
            citiesList.clear();
            queryDocumentSnapshots.forEach(document -> citiesList.add(new City(
                    document.getString("name"),
                    Objects.requireNonNull(document.getLong("distance")).intValue(),
                    Objects.requireNonNull(document.getLong("routeID")).intValue()
            )));

            loadingSpinner.setVisibility(View.GONE);
            Log.i(LOG_TAG, "SUCCESSFUL city data query!");
        });
    }

    public void search(View view) {

        // DATE TYPE
        boolean isDepartDate = isDepartDateRadioGroup.getCheckedRadioButtonId() == R.id.departRB;

        // CHECK DATA
        if (originCity == null || destCity == null) {
            Toast.makeText(this, "Mindkét állomást ki kell választani!", Toast.LENGTH_SHORT).show();
            return;
        } else if (originCity.equals(destCity)) {
            Toast.makeText(this, "Válassz különböző állomásokat!", Toast.LENGTH_SHORT).show();
            return;
        } else if (
                (!isDepartDate && currentDate.after(inputDate))
                        || (isDepartDate && inputDate.before(currentDate))) {
            Toast.makeText(this, "A választott idő már lejárt!", Toast.LENGTH_SHORT).show();
            return;
        }

        // DISTANCE
        int distance;
        if (originCity.getRouteID() == 0) {
            distance = destCity.getDistance();
        } else if (Objects.equals(originCity.getRouteID(), destCity.getRouteID())) {
            distance = Math.abs(originCity.getDistance() - destCity.getDistance());
        } else {
            distance = originCity.getDistance() + destCity.getDistance();
        }

        // TRAVEL TIME
        int travelTime = distance / 2;

        // DEPART FREQUENCY
        int departFrequency = 60 / (originCity.getRouteID() == 0 ? destCity.getRouteID() : originCity.getRouteID());

        // DEPART MINUTES
        int departMinutes = (int) Math.abs(Math.sin(distance) * 59);

        // COMFORT
        Map<Integer, Comfort> comfortMap = new HashMap<>();
        comfortMap.put(R.id.fastTrainRB, Comfort.FAST_TRAIN);
        comfortMap.put(R.id.secondClassRB, Comfort.SECOND_CLASS);
        comfortMap.put(R.id.firstClassRB, Comfort.FIRST_CLASS);

        Comfort comfort = comfortMap.get(comfortRadioGroup.getCheckedRadioButtonId());

        // DISCOUNT
        Map<Integer, Discount> discountMap = new HashMap<>();
        discountMap.put(R.id.noDiscountRB, Discount.NONE);
        discountMap.put(R.id.fiftyDiscountRB, Discount.STUDENT);
        discountMap.put(R.id.ninetyDiscountRB, Discount.WORKER);

        Discount discount = discountMap.get(discountRadioGroup.getCheckedRadioButtonId());

        // PRICE
        assert comfort != null;
        assert discount != null;
        int price = (int) ((Math.log(distance) * 500 + comfort.getValue()) * discount.getValue());

        // INTENT
        Intent searchIntent = new Intent(this, RoutesActivity.class);
        searchIntent.putExtra("originCity", originCity);
        searchIntent.putExtra("destCity", destCity);
        searchIntent.putExtra("inputDate", inputDate);
        searchIntent.putExtra("isDepartDate", isDepartDate);
        searchIntent.putExtra("distance", distance);
        searchIntent.putExtra("travelTime", travelTime);
        searchIntent.putExtra("departFrequency", departFrequency);
        searchIntent.putExtra("departMinutes", departMinutes);
        searchIntent.putExtra("comfort", comfort);
        searchIntent.putExtra("discount", discount);
        searchIntent.putExtra("price", price);

        startActivity(searchIntent);
    }
}