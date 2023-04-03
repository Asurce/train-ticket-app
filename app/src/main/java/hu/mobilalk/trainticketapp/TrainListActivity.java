package hu.mobilalk.trainticketapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;

public class TrainListActivity extends AppCompatActivity {
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    private RecyclerView trainsRV;
    private ArrayList<SearchResultItem> trainsList;
    private TrainListAdapter trainListAdapter;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        trainsRV = findViewById(R.id.trainRecyclerView);
        trainsRV.setLayoutManager(new LinearLayoutManager(this));
        trainsList = new ArrayList<>();
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        trainListAdapter = new TrainListAdapter(this, trainsList);
        trainsRV.setAdapter(trainListAdapter);

        initializeData();

    }

    private void initializeData() {
        trainsList.clear();

        for (int i = 0; i<11; i++) {
            trainsList.add(new SearchResultItem(preferences.getString("city", "")));
        }

        trainListAdapter.notifyDataSetChanged();
    }
}