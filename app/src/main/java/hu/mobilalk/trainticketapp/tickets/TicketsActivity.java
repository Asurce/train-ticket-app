package hu.mobilalk.trainticketapp.tickets;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import hu.mobilalk.trainticketapp.LoginActivity;
import hu.mobilalk.trainticketapp.MainActivity;
import hu.mobilalk.trainticketapp.R;
import hu.mobilalk.trainticketapp.SettingsActivity;

public class TicketsActivity extends AppCompatActivity {
    private static final String LOG_TAG = TicketsAdapter.class.getName();

    // FIREBASE
    FirebaseAuth fireAuth;
    FirebaseFirestore fireStore;
    FirebaseFirestoreSettings fireSettings;
    CollectionReference ticketsCollection;

    // LISTING
    RecyclerView ticketsRV;
    ArrayList<TicketItem> ticketItemList;
    TicketsAdapter ticketsAdapter;

    // BOTTOM NAV
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        // FIREBASE
        fireAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fireStore.setFirestoreSettings(settings);
        ticketsCollection = fireStore.collection("tickets");

        if (fireAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        // LISTING
        ticketsRV = findViewById(R.id.ticketRecyclerView);
        ticketItemList = new ArrayList<>();
        ticketsAdapter = new TicketsAdapter(this, ticketItemList);
        ticketsRV.setLayoutManager(new LinearLayoutManager(this));
        ticketsRV.setAdapter(ticketsAdapter);

        // BOTTOM NAV
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.tickets);
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryTickets();
        bottomNav.setSelectedItemId(R.id.tickets);
    }

    private void queryTickets() {
        ticketsCollection.whereEqualTo("userID", fireAuth.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            ticketItemList.clear();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                ticketItemList.add(new TicketItem(
                        document.get("originCity").toString(),
                        document.get("destCity").toString(),
                        document.getLong("departTime"),
                        document.getLong("arriveTime"),
                        document.getLong("discount"),
                        document.getLong("comfort").intValue(),
                        document.getLong("distance").intValue(),
                        Integer.parseInt(document.get("price").toString()),
                        document.get("userID").toString()
                ));
            }
            ticketsAdapter.notifyDataSetChanged();
        });

    }
}