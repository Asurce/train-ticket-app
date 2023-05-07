package hu.mobilalk.trainticketapp.routes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import hu.mobilalk.trainticketapp.LoginActivity;
import hu.mobilalk.trainticketapp.NotificationHelper;
import hu.mobilalk.trainticketapp.R;
import hu.mobilalk.trainticketapp.enums.Comfort;
import hu.mobilalk.trainticketapp.enums.Discount;
import hu.mobilalk.trainticketapp.models.City;
import hu.mobilalk.trainticketapp.tickets.TicketItem;
import hu.mobilalk.trainticketapp.tickets.TicketsActivity;

public class RoutesActivity extends AppCompatActivity {
    private static final String LOG_TAG = RoutesActivity.class.getName();

    // ANDROID
    Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
    Calendar referenceCalendar;
    Calendar inputDate;
    NotificationHelper notificationHelper;

    // FIREBASE
    FirebaseAuth fireAuth;
    FirebaseFirestore firestore;
    CollectionReference ticketsCollection;

    // LISTING
    RecyclerView routesRV;
    ArrayList<RouteItem> routesList;
    RoutesAdapter routesAdapter;

    // MISC
    boolean isDepartDate;
    ActionBar actionBar;
    TextView noResultTV;
    ActivityResultLauncher<Intent> loginLauncher;
    TicketItem ticketToBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        // ANDROID, MISC
        referenceCalendar = Calendar.getInstance();
        inputDate = (Calendar) getIntent().getSerializableExtra("inputDate");
        isDepartDate = getIntent().getBooleanExtra("isDepartDate", true);
        notificationHelper = new NotificationHelper(this);
        noResultTV = findViewById(R.id.noResultTV);
        if (inputDate == null && savedInstanceState == null) finish();

        // ACTION BAR
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.routes);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // FIREBASE
        fireAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        ticketsCollection = firestore.collection("tickets");

        // LISTING
        routesRV = findViewById(R.id.trainRecyclerView);
        routesList = new ArrayList<>();
        if (savedInstanceState != null) {
            routesList = gson.fromJson(savedInstanceState.getString("routesList"), new TypeToken<ArrayList<RouteItem>>() {}.getType());
        } else {
            initializeData();
        }
        routesAdapter = new RoutesAdapter(this, routesList);
        routesRV.setLayoutManager(new LinearLayoutManager(this));
        routesRV.setAdapter(routesAdapter);

        // WAIT FOR LOGIN
        loginLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(), (result) -> {
                            if (fireAuth.getCurrentUser() != null) {
                                ticketToBuy.setUserID(fireAuth.getUid());
                                ticketsCollection.add(ticketToBuy);
                                notificationHelper.send("Sikeres vásárlás!");
                                startActivity(new Intent(this, TicketsActivity.class));
                            }
                        });

        // NOTIFICATION PERMISSION FOR ANDROID 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    private void initializeData() {
        routesList.clear();

        // CALCULATE TIMES
        int travelTime = getIntent().getIntExtra("travelTime", 0);
        int departFrequency = getIntent().getIntExtra("departFrequency", 1);
        int departMinutes = getIntent().getIntExtra("departMinutes", 0);

        if (isDepartDate) {
            referenceCalendar.setTime(inputDate.getTime());
            referenceCalendar.set(Calendar.HOUR_OF_DAY, 23);
            referenceCalendar.set(Calendar.MINUTE, 59);

            inputDate.add(Calendar.HOUR_OF_DAY, inputDate.get(Calendar.MINUTE) < departMinutes ? 0 : 1);
            inputDate.set(Calendar.MINUTE, departMinutes);
        } else {
            referenceCalendar.setTime(inputDate.getTime());
            inputDate.set(Calendar.HOUR_OF_DAY, 2);
            inputDate.set(Calendar.MINUTE, departMinutes);
        }

        // GENERATE ROUTES
        while ((inputDate.getTimeInMillis() + (travelTime * 60000L)) < referenceCalendar.getTimeInMillis()) {
            routesList.add(new RouteItem(
                    (City) getIntent().getSerializableExtra("originCity"),
                    (City) getIntent().getSerializableExtra("destCity"),
                    inputDate.getTime(),
                    new Date(inputDate.getTimeInMillis() + (travelTime * 60000L)),
                    travelTime,
                    (Discount) getIntent().getSerializableExtra("discount"),
                    (Comfort) getIntent().getSerializableExtra("comfort"),
                    getIntent().getIntExtra("distance", 0),
                    getIntent().getIntExtra("price", 0)
            ));
            inputDate.setTimeInMillis(inputDate.getTimeInMillis() + (departFrequency * 60000L));
        }

        if (routesList.isEmpty()) {
            noResultTV.setVisibility(View.VISIBLE);
            routesRV.setVisibility(View.GONE);
        }

        Log.i(LOG_TAG, "SUCCESSFULLY generated all route items!");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("routesList", gson.toJson(routesList));
    }

    public void purchaseTicket() {
        if (fireAuth.getCurrentUser() == null) {
            loginLauncher.launch(new Intent(this, LoginActivity.class));
        } else {
            ticketToBuy.setUserID(fireAuth.getUid());
            ticketsCollection.add(ticketToBuy);
            notificationHelper.send("Sikeres vásárlás!");
            startActivity(new Intent(this, TicketsActivity.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTicketToBuy(TicketItem ticketToBuy) {
        this.ticketToBuy = ticketToBuy;
    }
}