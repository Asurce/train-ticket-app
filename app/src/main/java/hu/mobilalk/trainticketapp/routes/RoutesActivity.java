package hu.mobilalk.trainticketapp.routes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;

import hu.mobilalk.trainticketapp.MainActivity;
import hu.mobilalk.trainticketapp.R;

public class RoutesActivity extends AppCompatActivity {
    private static final String LOG_TAG = RoutesActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    // ANDROID
    Calendar calendar;

    // LISTING
    RecyclerView trainsRV;
    ArrayList<RouteItem> trainsList;

    RoutesAdapter routesAdapter;

    // MISC
    RouteItem searchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        // ANDROID, MISC
        calendar = Calendar.getInstance();
        searchData = (RouteItem) getIntent().getSerializableExtra("searchData");
        calendar.setTime(searchData.getDepartTime());

        // LISTING
        trainsRV = findViewById(R.id.trainRecyclerView);
        trainsList = new ArrayList<>();
        routesAdapter = new RoutesAdapter(this, trainsList);
        trainsRV.setLayoutManager(new LinearLayoutManager(this));
        trainsRV.setAdapter(routesAdapter);

        initializeData();
    }

    private void initializeData() {
        trainsList.clear();

        int day = calendar.get(Calendar.DAY_OF_MONTH);


        while (calendar.get(Calendar.DAY_OF_MONTH) < day+1) {
            trainsList.add(new RouteItem(
                    searchData.getOriginCity(),
                    searchData.getDestCity(),
                    calendar.getTime(),
                    calendar.getTime(),
                    searchData.getDiscount(),
                    searchData.getComfort(),
                    searchData.getDistance(),
                    searchData.getPrice()
                    ));
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR)+1);
        }

        routesAdapter.notifyDataSetChanged();
    }
}