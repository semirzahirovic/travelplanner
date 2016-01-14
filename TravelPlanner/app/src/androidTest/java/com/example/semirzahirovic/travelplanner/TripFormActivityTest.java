package com.example.semirzahirovic.travelplanner;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Created by semirzahirovic on 07/12/15.
 */
public class TripFormActivityTest extends ActivityInstrumentationTestCase2<TripFormActivity> {
    TripFormActivity activity;

    public TripFormActivityTest() {
        super(TripFormActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    @SmallTest
    public void testUI(){
        getInstrumentation().waitForIdleSync();
        assertNotNull(activity.comment);
        assertNotNull(activity.destination);
        assertNotNull(activity.endDate);
        assertNotNull(activity.startDate);
        assertNotNull(activity.save);
    }

    @SmallTest
    public void testNoDestination(){
        getInstrumentation().waitForIdleSync();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.destination.setText("");
                activity.save.performClick();
            }
        });
        assertNull(activity.trip.getDestination());
    }

    public void testNoStartDate(){
        getInstrumentation().waitForIdleSync();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.startDate.setText(null);
                activity.save.performClick();
            }
        });
        assertNull(activity.trip.getStartDate());
    }

    public void testNoEndDate(){
        getInstrumentation().waitForIdleSync();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.endDate.setText(null);
                activity.save.performClick();
            }
        });
        assertNull(activity.trip.getEndDate());
    }

}
