package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * 10120054
 * Fajru Falah
 * IF-2
 */

public class LogoutFragment extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button buttonLogout;
    private TextView textViewUserDetails;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Check if the user is logged in
        if (user == null) {
            navigateToLogin();
        } else {
            setContentView(R.layout.activity_logout_fragment); // Set the layout if needed
            textViewUserDetails = findViewById(R.id.user_details);
            textViewUserDetails.setText(user.getEmail());

            buttonLogout = findViewById(R.id.logout);
            buttonLogout.setOnClickListener(view -> performLogout());
        }
    }

    private void performLogout() {
        FirebaseAuth.getInstance().signOut();
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Optionally, you can override the onBackPressed method to prevent going back after logout
    @Override
    public void onBackPressed() {
        // Do nothing or show a message like "You have been logged out"
    }
}