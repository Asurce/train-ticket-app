package hu.mobilalk.trainticketapp.routes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import hu.mobilalk.trainticketapp.City;
import hu.mobilalk.trainticketapp.MainActivity;
import hu.mobilalk.trainticketapp.R;
import hu.mobilalk.trainticketapp.enums.Comfort;
import hu.mobilalk.trainticketapp.enums.Discount;

public class RoutesActivity extends AppCompatActivity {
    private static final String LOG_TAG = RoutesActivity.class.getName();

    // ANDROID
    Calendar referenceCalendar;
    Calendar inputDate;

    // LISTING
    RecyclerView routesRV;
    ArrayList<RouteItem> routesList;
    RoutesAdapter routesAdapter;

    // MISC
    boolean isDepartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        // ANDROID, MISC
        referenceCalendar = Calendar.getInstance();
        inputDate = (Calendar) getIntent().getSerializableExtra("inputDate");
        isDepartDate = getIntent().getBooleanExtra("isDepartDate", true);
        if (inputDate == null) finish();

        // LISTING
        routesRV = findViewById(R.id.trainRecyclerView);
        routesList = new ArrayList<>();
        routesAdapter = new RoutesAdapter(this, routesList);
        routesRV.setLayoutManager(new LinearLayoutManager(this));
        routesRV.setAdapter(routesAdapter);

        // ACTION BAR
        getSupportActionBar().setTitle(R.string.routes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeData();
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

        routesAdapter.notifyDataSetChanged();

        Log.i(LOG_TAG, "SUCCESSFULLY generated all route items!");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}