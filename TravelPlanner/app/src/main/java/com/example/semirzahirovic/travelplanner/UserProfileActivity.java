package com.example.semirzahirovic.travelplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import utils.Utils;

/**
 * Created by semirzahirovic on 04/12/15.
 */
public class UserProfileActivity extends AppCompatActivity {
    private String userId;
    ParseUser user;
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
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        save.setText("Save");
        if (getIntent().getExtras() != null && getIntent().getExtras().get("userId") != null) {
            userId = (String) getIntent().getExtras().get("userId");

            ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
            query.whereEqualTo("objectId", userId);
            query.getFirstInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e == null) {
                        user = parseUser;
                        firstName.setText(user.getString("firstname"));
                        lastName.setText(user.getString("lastname"));
                        email.setText(user.getEmail());
                        password.setVisibility(View.GONE);
                        username.setText(user.getUsername());
                    }
                }
            });
        }
        save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {
                                        if (email.getText().toString().length() == 0) {
                                            Utils.showSnackBar(view, getString(R.string.email_blank));
                                        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                                            Utils.showSnackBar(view, getString(R.string.email_invalid_format));
                                        } else if (username.getText().toString().length() == 0) {
                                            Utils.showSnackBar(view, getString(R.string.username_blank));
                                        } else if (password.getVisibility() == View.VISIBLE && password.getText().toString().length() == 0) {
                                            Utils.showSnackBar(view, getString(R.string.password_blank));
                                        } else {
                                            HashMap<String, String> params = new HashMap<String, String>();
                                            params.put("firstname", firstName.getText().toString());
                                            params.put("lastname", lastName.getText().toString());
                                            params.put("email", email.getText().toString());
                                            params.put("username", username.getText().toString());
                                            params.put("password", password.getText().toString());

                                            if (user != null) {
                                                params.put("userId", user.getObjectId());
                                                ParseCloud.callFunctionInBackground("modifyUser", params, new FunctionCallback<Object>() {
                                                    @Override
                                                    public void done(Object result, ParseException e) {
                                                        if (e == null) {
                                                            // success
                                                            user = (ParseUser) result;
                                                            user.pinInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                    if (e == null) {
                                                                        setResult(RESULT_OK);
                                                                        if (!isFinishing())
                                                                            finish();
                                                                    } else {
                                                                        Utils.showSnackBar(view, e.getMessage());
                                                                    }
                                                                }
                                                            });


                                                        }
                                                    }
                                                });
                                            } else {
                                                params.put("userRole", "default");
                                                ParseCloud.callFunctionInBackground("addNewUser", params, new FunctionCallback<Object>() {
                                                    @Override
                                                    public void done(Object result, ParseException e) {
                                                        if (e == null) {
                                                            // success
                                                            setResult(RESULT_OK);
                                                            user = (ParseUser) result;
                                                            user.pinInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                    if (e == null) {
                                                                        if (!isFinishing())
                                                                            finish();
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            Utils.showSnackBar(view, e.getMessage());
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
        );
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
