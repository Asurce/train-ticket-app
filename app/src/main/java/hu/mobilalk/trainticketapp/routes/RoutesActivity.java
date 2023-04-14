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
    Calendar inputDate;
    Calendar calcDate;

    // LISTING
    RecyclerView routesRV;
    ArrayList<RouteItem> routesList;

    RoutesAdapter routesAdapter;

    // MISC
    RouteItem searchData;
    boolean isDepartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        // ANDROID, MISC
        inputDate = Calendar.getInstance();
        calcDate = Calendar.getInstance();
        searchData = (RouteItem) getIntent().getSerializableExtra("searchData");
        isDepartDate = getIntent().getBooleanExtra("isDepartDate", true);
        inputDate.setTime(searchData.getDepartTime());
        calcDate.setTime(searchData.getArriveTime());

        // LISTING
        routesRV = findViewById(R.id.trainRecyclerView);
        routesList = new ArrayList<>();
        routesAdapter = new RoutesAdapter(this, routesList);
        routesRV.setLayoutManager(new LinearLayoutManager(this));
        routesRV.setAdapter(routesAdapter);

        initializeData();
    }

    private void initializeData() {
        routesList.clear();

        int day = inputDate.get(Calendar.DAY_OF_MONTH);

        while (inputDate.get(Calendar.DAY_OF_MONTH) < day+1) {
            routesList.add(new RouteItem(
                    searchData.getOriginCity(),
                    searchData.getDestCity(),
                    inputDate.getTime(),
                    calcDate.getTime(),
                    searchData.getDiscount(),
                    searchData.getComfort(),
                    searchData.getDistance(),
                    searchData.getPrice()
                    ));
            inputDate.set(Calendar.HOUR, inputDate.get(Calendar.HOUR)+1);
            calcDate.set(Calendar.HOUR, calcDate.get(Calendar.HOUR)+1);
        }

        routesAdapter.notifyDataSetChanged();
    }
}