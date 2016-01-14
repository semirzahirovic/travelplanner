package com.example.semirzahirovic.travelplanner;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

/**
 * Created by semirzahirovic on 07/12/15.
 */
public class SignupActivityTest extends ActivityInstrumentationTestCase2<SignupActivity> {
    SignupActivity activity;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText username;
    EditText password;
    Button signup;

    public SignupActivityTest() {
        super(SignupActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = (SignupActivity) getActivity();
        firstName = (EditText) activity.findViewById(R.id.firstName);
        lastName = (EditText) activity.findViewById(R.id.lastname);
        email = (EditText) activity.findViewById(R.id.email);
        username = (EditText) activity.findViewById(R.id.username);
        password = (EditText) activity.findViewById(R.id.password);
        signup = (Button) activity.findViewById(R.id.signup);
    }

    @SmallTest
    public void testActivityNotNull() {
        assertNotNull(activity);
    }

    @SmallTest
    public void testCheckUIInflation() throws Throwable {
        assertNotNull(username);
        assertNotNull(password);
        assertNotNull(firstName);
        assertNotNull(lastName);
        assertNotNull(email);
        assertNotNull(signup);
    }

    @MediumTest
    public void testCheckInValidSignupUsername() throws Throwable {

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                firstName.setText("123");
                lastName.setText("123");
                password.setText("1234");
                email.setText("aaa@aa.aa");
                signup.performClick();
            }
        });
        Thread.sleep(200);
        assertEquals(activity.user, null);
    }

    @MediumTest
    public void testCheckInValidSignupEmail() throws Throwable {

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                firstName.setText("123");
                lastName.setText("123");
                password.setText("1234");
                username.setText("123");
                email.setText("aaaaa.aa");
                signup.performClick();
            }
        });
        Thread.sleep(200);
        assertEquals(activity.user, null);
    }

    @MediumTest
    public void testCheckInValidSignupPassword() throws Throwable {

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                firstName.setText("123");
                lastName.setText("123");
                email.setText("aaa@aa.aa");
                signup.performClick();
            }
        });
        Thread.sleep(200);
        assertEquals(activity.user, null);
    }

    @MediumTest
    public void testCheckValidSignupDefault() throws Throwable {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
        if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            ParseUser.logOut();
            Thread.sleep(2000);
        }
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                firstName.setText("123");
                lastName.setText("123");
                username.setText("1234");
                password.setText("1234");
                email.setText("aaa@aa.aa");
                signup.performClick();
            }
        });
        Thread.sleep(3000);
        assertFalse(ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser()));
        assertEquals(ParseUser.getCurrentUser().getEmail(), (email.getText().toString()));
        assertEquals(ParseUser.getCurrentUser().getUsername(), (username.getText().toString()));
        MainActivity nextActivity = (MainActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(nextActivity);
        nextActivity.finish();
    }
}
