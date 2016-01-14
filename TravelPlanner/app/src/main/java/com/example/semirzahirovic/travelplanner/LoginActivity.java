package com.example.semirzahirovic.travelplanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import utils.ConectivityChecker;
import utils.Utils;

/**
 * Created by semirzahirovic on 02/12/15.
 */
public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.login)
    Button loginbutton;
    @Bind(R.id.signup)
    Button signup;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        // Login Button Click Listener
        ButterKnife.bind(this);
        loginbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View arg0) {
                // Retrieve the text entered from the EditText
                if (ConectivityChecker.isInternetOn(LoginActivity.this)) {
                    String usernametxt = username.getText().toString();
                    String passwordtxt = password.getText().toString();

                    if (usernametxt.equals("")) {
                        Utils.showSnackBar(arg0, getString(R.string.username_blank));
                    } else if (passwordtxt.equals("")) {
                        Utils.showSnackBar(arg0, getString(R.string.password_blank));
                    } else {
                        progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setMessage(getString(R.string.logging_in));
                        progressDialog.show();
                        // Send data to Parse.com for verification)
                        ParseUser.logInInBackground(usernametxt, passwordtxt,
                                new LogInCallback() {
                                    public void done(ParseUser user, ParseException e) {
                                        if (progressDialog != null && progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        if (user != null) {
                                            if (!user.getString("userRole").equals("admin")) {
                                                Intent intent = new Intent(
                                                        LoginActivity.this,
                                                        MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(
                                                        LoginActivity.this,
                                                        AdminActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        } else {
                                            Utils.showSnackBar(arg0, e.getMessage());
                                        }
                                    }
                                });
                    }
                } else {
                    if (!isFinishing()) Utils.showAlertDialog(LoginActivity.this);
                }
            }
        });
        // Sign up Button Click Listener
        signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
