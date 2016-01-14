package com.example.semirzahirovic.travelplanner;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Created by semirzahirovic on 07/12/15.
 */
public class AdminActivityTest extends ActivityInstrumentationTestCase2<AdminActivity> {
    AdminActivity activity;

    public AdminActivityTest() {
        super(AdminActivity.class);
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
    public void testFABOpensUserForm() throws Throwable {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(UserProfileActivity.class.getName(), null, false);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.fab.performClick();
            }
        });
        Thread.sleep(1000);
        UserProfileActivity nextActivity = (UserProfileActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(nextActivity);
        nextActivity.finish();
    }

}
