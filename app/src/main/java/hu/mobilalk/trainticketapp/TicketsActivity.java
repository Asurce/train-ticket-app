package hu.mobilalk.trainticketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class TicketsActivity extends AppCompatActivity {

    FirebaseAuth fireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        fireAuth = FirebaseAuth.getInstance();

        if (fireAuth.getCurrentUser() == null) {
            finish();
        }
    }
}