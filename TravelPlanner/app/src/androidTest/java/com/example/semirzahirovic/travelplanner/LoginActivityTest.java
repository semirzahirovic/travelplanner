package com.example.semirzahirovic.travelplanner;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by semirzahirovic on 07/12/15.
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    LoginActivity activity;
    EditText username;
    EditText password;
    Button login;
    Button signup;

    public LoginActivityTest() {
        super(LoginActivity.class);
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        username = (EditText) activity.findViewById(R.id.username);
        password = (EditText) activity.findViewById(R.id.password);
        login = (Button) getActivity().findViewById(R.id.login);
        signup = (Button) getActivity().findViewById(R.id.signup);
    }
    @SmallTest
    public void testCheckCurrentUserExistance() {
        assertNotNull(ParseUser.getCurrentUser());
    }

    @SmallTest
    public void testActivityNotNull() {
        assertNotNull(activity);
    }

    @SmallTest
    public void testCheckUIInflation() throws Throwable {
        assertNotNull(username);
        assertNotNull(password);
        assertNotNull(login);
        assertNotNull(signup);
    }

    @SmallTest
    public void testSignupOpensNewActivity() throws Throwable {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(SignupActivity.class.getName(), null, false);
        assertNotNull(signup);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                signup.performClick();
            }
        });
        Thread.sleep(1000);
        SignupActivity nextActivity = (SignupActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(nextActivity);
        nextActivity.finish();
    }

    @MediumTest
    public void testCheckValidLoginAdmin() throws Throwable {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(AdminActivity.class.getName(), null, false);
        if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            ParseUser.logOut();
            Thread.sleep(2000);
        }
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                username.setText("semir");
                password.setText("semir");
                login.performClick();
            }
        });
        Thread.sleep(3000);
        assertFalse(ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser()));
        AdminActivity nextActivity = (AdminActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(nextActivity);
        nextActivity.finish();

    }

    @MediumTest
    public void testCheckValidLoginDefault() throws Throwable {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
        if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            ParseUser.logOut();
            Thread.sleep(2000);
        }
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                username.setText("sema");
                password.setText("sema");
                login.performClick();
            }
        });
        Thread.sleep(3000);
        assertFalse(ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser()));
        MainActivity nextActivity = (MainActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(nextActivity);
        nextActivity.finish();
    }
}
