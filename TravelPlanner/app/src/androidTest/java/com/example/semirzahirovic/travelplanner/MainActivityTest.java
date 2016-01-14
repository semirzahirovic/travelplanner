package com.example.semirzahirovic.travelplanner;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.parse.ParseUser;

/**
 * Created by semirzahirovic on 07/12/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    MainActivity activity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    @SmallTest
    public void testActivityNotNull() {
        assertNotNull(activity);
    }

    @SmallTest
    public void testUI() {
        assertNotNull(activity.fab);
        assertNotNull(activity.recyclerView);
    }

    @SmallTest
    public void testRVAdapter() throws Throwable {
        assertNotNull(activity.recyclerView.getAdapter());
    }

    @SmallTest
    public void testFABOpensTripForm() throws Throwable {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(TripFormActivity.class.getName(), null, false);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.fab.performClick();
            }
        });
        Thread.sleep(1000);
        TripFormActivity nextActivity = (TripFormActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(nextActivity);
        nextActivity.finish();
    }

    @MediumTest
    public void testParseResponse() throws Throwable {
    }
}
