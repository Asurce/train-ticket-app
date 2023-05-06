package hu.mobilalk.trainticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.Document;

import java.util.Objects;

import hu.mobilalk.trainticketapp.tickets.TicketsActivity;

public class SettingsActivity extends AppCompatActivity {

    // FIREBASE
    FirebaseAuth fireAuth;
    FirebaseFirestore firestore;
    DocumentReference userReference;

    // INPUT
    EditText firstName;
    EditText lastName;

    // BUTTONS
    Button loginButton;
    Button modifyButton;
    Button logoutButton;

    // MISC
    ActionBar actionBar;
    LinearLayout linearLayout;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // ACTION BAR
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // FIREBASE
        fireAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // INPUT
        firstName = findViewById(R.id.firstNameET);
        lastName = findViewById(R.id.lastNameET);

        // BUTTONS
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));

        modifyButton = findViewById(R.id.modifyButton);
        modifyButton.setOnClickListener(this::modify);

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(view -> {
            fireAuth.signOut();
            onResume();
        });

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
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    return true;
                case R.id.settings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    return true;
                default:
                    return true;
            }
        });
        bottomNav.setOnItemReselectedListener(item -> {

        });

        // MISC
        linearLayout = findViewById(R.id.linearLayout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fireAuth.getCurrentUser() == null) {
            linearLayout.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            firestore.collection("users")
                    .whereEqualTo("id", fireAuth.getUid())
                    .limit(1)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                if (querySnapshot.getDocuments().size() == 1) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    userReference = document.getReference();
                    firstName.setText(document.getString("firstName"));
                    lastName.setText(document.getString("lastName"));
                } else {
                    onResume();
                }
            });
        }
        bottomNav.setSelectedItemId(R.id.settings);
    }

    public void modify(View view) {
        userReference.update("firstName", firstName.getText().toString());
        userReference.update("lastName", lastName.getText().toString());

        Toast.makeText(this, "Sikeres módosítás!", Toast.LENGTH_SHORT).show();
    }


}