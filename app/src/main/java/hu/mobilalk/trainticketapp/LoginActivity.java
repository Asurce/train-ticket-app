package hu.mobilalk.trainticketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getName();

    // FIREBASE
    private FirebaseAuth fireAuth;

    // INPUTS
    EditText emailEditText;
    EditText passwordEditText;

    // BUTTONS
    Button loginButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // FIREBASE
        fireAuth = FirebaseAuth.getInstance();
        if (fireAuth.getCurrentUser() != null) finish();

        // INPUTS
        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // BUTTONS
        loginButton.setOnClickListener(this::login);
        registerButton.setOnClickListener(this::register);

        // ACTION BAR
        getSupportActionBar().setTitle(R.string.login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void login(View view) {
        fireAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(LOG_TAG, "Login SUCCESSFUL!");
                        finish();
                    } else Log.i(LOG_TAG, "Login FAILED!");
                });
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
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