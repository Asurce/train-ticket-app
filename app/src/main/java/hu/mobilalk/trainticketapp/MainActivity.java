package hu.mobilalk.trainticketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    FirebaseAuth fireAuth;
    FirebaseFirestore firestore;
    CollectionReference citiesCollection;
    Button loginOrTicketButton;
    Button logoutButton;
    AutoCompleteTextView fromACTV;
    List<String> citiesList;
    SharedPreferences preferences;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fireAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        citiesCollection = firestore.collection("cities");
        loginOrTicketButton = findViewById(R.id.loginOrTicketButton);
        logoutButton = findViewById(R.id.logoutButton);
        fromACTV = findViewById(R.id.fromACTV);
        citiesList = new ArrayList<>();
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        searchButton = findViewById(R.id.searchButton);

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, citiesList);
        fromACTV.setAdapter(fromAdapter);
        fromACTV.setThreshold(1);
        fromACTV.setOnItemClickListener((adapterView, view, i, l) -> {
            Log.i(LOG_TAG, "Selected city: " + adapterView.getItemAtPosition(i));
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putString("city", adapterView.getItemAtPosition(i).toString());
            editor.apply();
        });

        if (fireAuth.getCurrentUser() == null) {
            loginOrTicketButton.setText(R.string.login);
            loginOrTicketButton.setOnClickListener(this::login);
        } else {
            loginOrTicketButton.setText(getString(R.string.my_tickets));
            loginOrTicketButton.setOnClickListener(this::tickets);
        }
        logoutButton.setOnClickListener(this::logout);

        searchButton.setOnClickListener(view -> {
            startActivity(new Intent(this, TrainListActivity.class));
        });

        queryData();

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

    public void queryData() {
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