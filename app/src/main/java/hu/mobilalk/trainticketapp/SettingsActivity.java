package hu.mobilalk.trainticketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {

    FirebaseAuth fireAuth;
    FirebaseFirestore firestore;
    DocumentReference userReference;
    User user;

    Button modifyButton;
    Button logoutButton;

    EditText firstName;
    EditText lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firstName = findViewById(R.id.firstNameET);
        lastName = findViewById(R.id.lastNameET);

        fireAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        // TODO logged in
        firestore.collection("users").whereEqualTo("id", fireAuth.getUid()).get().addOnSuccessListener(querySnapshot -> {
            userReference = querySnapshot.getDocuments().get(0).getReference();
            firstName.setText(querySnapshot.getDocuments().get(0).getString("firstName"));
            lastName.setText(querySnapshot.getDocuments().get(0).getString("lastName"));
        });

        modifyButton = findViewById(R.id.modifyButton);
        modifyButton.setOnClickListener(this::modify);

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(view -> fireAuth.signOut());

    }

    public void modify(View view) {
        userReference.update("firstName", firstName.getText().toString());
        userReference.update("lastName", lastName.getText().toString());

        Toast.makeText(this, "Sikeres módosítás!", Toast.LENGTH_SHORT).show();
    }


}