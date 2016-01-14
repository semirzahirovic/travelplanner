package com.example.semirzahirovic.travelplanner;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import models.Trip;

/**
 * Created by semirzahirovic on 02/12/15.
 */
public class TravelPlannerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Trip.class);
        Parse.initialize(this, "QXCWEno7mibqCvpe0OHP6i5LJeWBroDZ79MTQCO1", "EtNHBp5zgonpONND3upimBKby6hm3HKU0CvUOqBk");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
