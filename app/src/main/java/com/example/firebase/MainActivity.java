package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * 10120054
 * Fajru Falah
 * IF-2
 */

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();



        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        final ProfileFragment profileFragment = new ProfileFragment();
        final InformationFragment informationFragment = new InformationFragment();
        final CatatanFragment catatanFragment = new CatatanFragment();

        loadFragment(profileFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_profile:
                    loadFragment(profileFragment);
                    return true;
                case R.id.menu_information:
                    loadFragment(informationFragment);
                    return true;
                case R.id.catatan:
                    loadFragment(catatanFragment);
                    return true;
                case R.id.menu_logout:
                    LogoutManager.performLogout(this); // Call the logout function here
                    return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, Login.class));
            finish();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}


