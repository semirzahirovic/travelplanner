package com.example.semirzahirovic.travelplanner;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Created by semirzahirovic on 08/12/15.
 */
public class UserProfileTest extends ActivityInstrumentationTestCase2<UserProfileActivity> {

    UserProfileActivity activity;

    public UserProfileTest() {
        super(UserProfileActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    @SmallTest
    public void testUI(){
        getInstrumentation().waitForIdleSync();
        assertNotNull(activity.firstName);
        assertNotNull(activity.lastName);
        assertNotNull(activity.username);
        assertNotNull(activity.password);
        assertNotNull(activity.email);
        assertNotNull(activity.save);
    }

    @SmallTest
    public void testNoUsername(){
        getInstrumentation().waitForIdleSync();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.username.setText("");
                activity.save.performClick();
            }
        });
        assertNull(activity.user);
    }

    @SmallTest
    public void testNoPassword(){
        getInstrumentation().waitForIdleSync();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.password.setText("");
                activity.save.performClick();
            }
        });
        assertNull(activity.user);
    }

    public void testNoEmail(){
        getInstrumentation().waitForIdleSync();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.username.setText(null);
                activity.save.performClick();
            }
        });
        assertNull(activity.user);
    }

    public void testWrongEmail(){
        getInstrumentation().waitForIdleSync();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.username.setText("abc");
                activity.save.performClick();
            }
        });
        assertNull(activity.user);
    }
}
