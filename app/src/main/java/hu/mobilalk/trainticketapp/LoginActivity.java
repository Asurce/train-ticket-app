package hu.mobilalk.trainticketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getName();

    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;
    private FirebaseAuth fireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fireAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.registerEmailEditText);
        passwordEditText = findViewById(R.id.registerPasswordEditText);
        loginButton = findViewById(R.id.loginOrTicketButton);
        registerButton = findViewById(R.id.registerButton);

        if (fireAuth.getCurrentUser() != null) {
            finish();
        }
//        int secretKey = getIntent().getIntExtra("SECRET_KEY", 0);
//        if (secretKey!=99) finish();

        loginButton.setOnClickListener(this::login);
        registerButton.setOnClickListener(this::register);

    }

    public void login(View view) {
        fireAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(LOG_TAG, "Login successful!");
                        startActivity(new Intent(this, MainActivity.class));
                    }
                    else Log.i(LOG_TAG, "Login FAILED!");
                });


        Log.i(LOG_TAG, "LOGIN -- Email: "+emailEditText.getText()+" - Password: "+passwordEditText.getText());
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

}