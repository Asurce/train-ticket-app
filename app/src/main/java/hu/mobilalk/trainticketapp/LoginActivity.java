package hu.mobilalk.trainticketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getName();

    EditText emailEditText;
    EditText passwordEditText;
    private FirebaseAuth fireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fireAuth = FirebaseAuth.getInstance();


        int secretKey = getIntent().getIntExtra("SECRET_KEY", 0);
        if (secretKey!=99) finish();

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            emailEditText = findViewById(R.id.loginEmailEditText);
            passwordEditText = findViewById(R.id.loginPasswordEditText);

            fireAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) Log.i(LOG_TAG, "Login successful!");
                        else Log.i(LOG_TAG, "Login FAILED!");
                    });


            Log.i(LOG_TAG, "LOGIN -- Email: "+emailEditText.getText()+" - Password: "+passwordEditText.getText());
        });

    }
}