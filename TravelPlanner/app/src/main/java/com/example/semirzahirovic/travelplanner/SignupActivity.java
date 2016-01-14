package com.example.semirzahirovic.travelplanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.semirzahirovic.travelplanner.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import utils.ConectivityChecker;
import utils.Utils;


/**
 * Created by semirzahirovic on 21/10/15.
 */
public class SignupActivity extends AppCompatActivity {

    private static String FIRSTNAME = "firstname";
    private static String LASTNAME = "lastname";
    public static String USERROLE = "userRole";
    private static String DEFAULT = "default";
    @Bind(R.id.firstName)
    EditText firstName;
    @Bind(R.id.lastname)
    EditText lastName;
    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.signup)
    Button signup;
    ParseUser user;
    ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (ConectivityChecker.isInternetOn(SignupActivity.this)) {
                    if (email.getText().toString().length() == 0) {
                        Utils.showSnackBar(v, getString(R.string.email_blank));
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                        Utils.showSnackBar(v, getString(R.string.email_invalid_format));
                    } else if (username.getText().toString().length() == 0) {
                        Utils.showSnackBar(v, getString(R.string.username_blank));
                    } else if (password.getText().toString().length() == 0) {
                        Utils.showSnackBar(v, getString(R.string.password_blank));
                    } else {
                        progressDialog = new ProgressDialog(SignupActivity.this);
                        progressDialog.setMessage(getString(R.string.signing_in));
                        progressDialog.show();
                        user = new ParseUser();
                        user.setUsername(username.getText().toString());
                        user.setPassword(password.getText().toString());
                        user.setEmail(email.getText().toString());
                        user.put(FIRSTNAME, firstName.getText().toString());
                        user.put(LASTNAME, lastName.getText().toString());
                        user.put(USERROLE, DEFAULT);
                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                if (progressDialog != null && progressDialog.isShowing())
                                    progressDialog.dismiss();
                                if (e == null) {
                                    Intent i = new Intent(SignupActivity.this, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Utils.showSnackBar(v, e.getMessage());
                                }
                            }
                        });
                    }
                } else {
                    Utils.showAlertDialog(SignupActivity.this);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}