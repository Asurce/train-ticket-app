package hu.mobilalk.trainticketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //FirebaseUser user;
    //user = FirebaseAuth.getInstance().getUser();
    //if (user == null) finish();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.putExtra("SECRET_KEY", 99);

        startActivity(loginIntent);
    }
}