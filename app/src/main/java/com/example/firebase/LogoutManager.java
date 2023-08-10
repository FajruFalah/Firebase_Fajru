package com.example.firebase;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;


/**
 * 10120054
 * Fajru Falah
 * IF-2
 */

public class LogoutManager {

    public static void performLogout(Context context) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(context, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}

