package hu.mobilalk.trainticketapp.tickets;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import hu.mobilalk.trainticketapp.LoginActivity;
import hu.mobilalk.trainticketapp.MainActivity;
import hu.mobilalk.trainticketapp.R;
import hu.mobilalk.trainticketapp.SettingsActivity;

public class TicketsActivity extends AppCompatActivity {
    private static final String LOG_TAG = TicketsAdapter.class.getName();

    // FIREBASE
    FirebaseAuth fireAuth;
    FirebaseFirestore fireStore;
    CollectionReference ticketsCollection;

    // LISTING
    RecyclerView ticketsRV;
    ArrayList<TicketItem> ticketItemList;
    TicketsAdapter ticketsAdapter;

    // MISC
    ActionBar actionBar;
    Button loginButton;
    TextView noResultTV;
    BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        // ACTION BAR
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        noResultTV = findViewById(R.id.noResultTV);

        // FIREBASE
        fireAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fireStore.setFirestoreSettings(settings);
        ticketsCollection = fireStore.collection("tickets");

        // LISTING
        ticketsRV = findViewById(R.id.ticketRecyclerView);
        ticketItemList = new ArrayList<>();
        ticketsAdapter = new TicketsAdapter(this, ticketItemList);
        ticketsRV.setLayoutManager(new LinearLayoutManager(this));
        ticketsRV.setAdapter(ticketsAdapter);

        // MISC
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.tickets);
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                case R.id.tickets:
                    startActivity(new Intent(getApplicationContext(), TicketsActivity.class));
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

        // LOGIN BUTTON
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // DISPLAY LOGIN BUTTON IF NOT LOGGED IN
        if (fireAuth.getCurrentUser() == null) {
            ticketsRV.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        } else {
            ticketsRV.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            queryTickets();
        }
        bottomNav.setSelectedItemId(R.id.tickets);
    }

    private void queryTickets() {
        ticketsCollection
                .whereEqualTo("userID", fireAuth.getUid())
                .orderBy("departTime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ticketItemList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        ticketItemList.add(
                                new TicketItem(
                                        document.getString("originCity"),
                                        document.getString("destCity"),
                                        document.getLong("departTime"),
                                        document.getLong("arriveTime"),
                                        Objects.requireNonNull(document.getLong("travelTime")).intValue(),
                                        document.getString("discount"),
                                        document.getString("comfort"),
                                        Objects.requireNonNull(document.getLong("distance")).intValue(),
                                        Objects.requireNonNull(document.getLong("price")).intValue(),
                                        document.getString("userID"),
                                        document.getId()
                                ));
                    }

                    if (ticketItemList.isEmpty()) {
                        noResultTV.setVisibility(View.VISIBLE);
                        ticketsRV.setVisibility(View.GONE);
                    }

                    ticketsAdapter.notifyDataSetChanged();
                    Log.i(LOG_TAG, "SUCCESSFUL tickets query!");
                });
    }
}