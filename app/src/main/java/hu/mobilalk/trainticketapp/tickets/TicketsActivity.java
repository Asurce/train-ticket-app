package hu.mobilalk.trainticketapp.tickets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import hu.mobilalk.trainticketapp.LoginActivity;
import hu.mobilalk.trainticketapp.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        // FIREBASE
        fireAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
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

        queryTickets();
    }

    private void queryTickets() {
        ticketItemList.clear();

        ticketsCollection.whereEqualTo("userID", fireAuth.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                ticketItemList.add(new TicketItem(
                        document.get("originCity").toString(),
                        document.get("destCity").toString(),
                        Integer.parseInt(document.get("price").toString()),
                        Long.parseLong(document.get("date").toString()),
                        document.get("userID").toString()
                ));
            }
            ticketsAdapter.notifyDataSetChanged();
        });

    }
}