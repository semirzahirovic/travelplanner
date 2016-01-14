package com.example.semirzahirovic.travelplanner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import utils.ConectivityChecker;

/**
 * Created by semirzahirovic on 03/12/15.
 */
public class WelcomeActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        // Determine whether the current user is an anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is anonymous, send the user to LoginActivity.class
            Intent intent = new Intent(WelcomeActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // If current user is NOT anonymous user
            // Get current user data from Parse.com
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (ConectivityChecker.isInternetOn(this)) {
                try {
                    currentUser = ParseUser.getCurrentUser().fetch();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (currentUser != null) {
                if (currentUser.getString("userRole").equals("admin")) {
                    Intent intent = new Intent(WelcomeActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                //Send user to LoginSignupActivity.class
                Intent intent = new Intent(WelcomeActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }
}

