package hu.mobilalk.trainticketapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class TrainListActivity extends AppCompatActivity {

    private static final String LOG_TAG = TrainListActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    SharedPreferences preferences;
    Calendar calendar;

    RecyclerView trainsRV;
    TrainListAdapter trainListAdapter;
    ArrayList<SearchResultItem> trainsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        calendar = Calendar.getInstance();
        try {
            calendar.setTime(
                    Objects.requireNonNull(new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
                            .parse(preferences.getString("datetime", ""))));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        trainsRV = findViewById(R.id.trainRecyclerView);
        trainsList = new ArrayList<>();
        trainListAdapter = new TrainListAdapter(this, trainsList);

        trainsRV.setLayoutManager(new LinearLayoutManager(this));
        trainsRV.setAdapter(trainListAdapter);

        initializeData();
    }

    private void initializeData() {
        trainsList.clear();

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Log.i(LOG_TAG, calendar.getTime().toString());

        while (calendar.get(Calendar.DAY_OF_MONTH) < day+1) {
            trainsList.add(new SearchResultItem(
                    preferences.getString("fromCity", ""),
                    preferences.getString("toCity", ""),
                    calendar.getTime(),
                    preferences.getInt("discount", 0),
                    preferences.getInt("class", 0)
                    ));
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR)+1);
        }

        trainListAdapter.notifyDataSetChanged();
    }
}