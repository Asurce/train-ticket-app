package hu.mobilalk.trainticketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();

    // FIREBASE
    FirebaseAuth fireAuth;

    // INPUTS
    EditText lastName;
    EditText firstName;
    EditText email;
    EditText password;
    EditText passwordAgain;

    // BUTTONS
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // FIREBASE
        fireAuth = FirebaseAuth.getInstance();
        if (fireAuth.getCurrentUser() != null) finish();

        // INPUTS
        lastName = findViewById(R.id.lastNameEditText);
        firstName = findViewById(R.id.firstNameEditText);
        email = findViewById(R.id.registerEmailEditText);
        password = findViewById(R.id.registerPasswordEditText);
        passwordAgain = findViewById(R.id.registerPasswordAgainEditText);

        // BUTTONS
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this::register);

    }

    public void register(View view) {

        if (!password.getText().toString().equals(passwordAgain.getText().toString())) {
            Toast.makeText(this, "Jelszó nem egyezik!", Toast.LENGTH_SHORT).show();
            return;
        }

        fireAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(LOG_TAG, "User registration SUCCESSFUL!");
                        finish();
                    } else {
                        Log.i(LOG_TAG, "User registration FAILED!");
                        Toast.makeText(RegisterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}