package hu.mobilalk.trainticketapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import hu.mobilalk.trainticketapp.models.User;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();

    // FIREBASE
    FirebaseAuth fireAuth;
    FirebaseFirestore firestore;
    CollectionReference usersCollection;

    // INPUTS
    EditText lastName;
    EditText firstName;
    EditText email;
    EditText password;
    EditText passwordAgain;

    // BUTTONS
    Button registerButton;

    // MISC
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ACTION BAR
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.register);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // FIREBASE
        fireAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        usersCollection = firestore.collection("users");

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

    @Override
    protected void onResume() {
        super.onResume();
        if (fireAuth.getCurrentUser() != null) finish();
    }

    public void register(View view) {

        if (!password.getText().toString().equals(passwordAgain.getText().toString())) {
            Toast.makeText(this, "Jelszó nem egyezik!", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.getText().length() < 6) {
            Toast.makeText(this, "Túl rövid jelszó, minimum 6 karakter legyen!", Toast.LENGTH_SHORT).show();
            return;
        }

        fireAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(LOG_TAG, "User registration SUCCESSFUL!");
                        usersCollection.add(new User(
                                Objects.requireNonNull(task.getResult().getUser()).getUid(),
                                firstName.getText().toString(),
                                lastName.getText().toString()
                        ));
                        finish();
                    } else {
                        Log.i(LOG_TAG, "User registration FAILED!");
                        Toast.makeText(RegisterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
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