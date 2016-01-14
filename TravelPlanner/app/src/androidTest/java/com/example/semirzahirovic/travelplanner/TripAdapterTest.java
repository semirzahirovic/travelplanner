package com.example.semirzahirovic.travelplanner;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import java.util.ArrayList;
import java.util.Date;

import adapters.TripAdapter;
import adapters.ViewHolder;
import models.Trip;
import utils.DateUtil;

/**
 * Created by semirzahirovic on 07/12/15.
 */
public class TripAdapterTest extends AndroidTestCase {
    private TripAdapter mAdapter;

    private Trip trip1;
    ViewHolder viewHolder;


    public TripAdapterTest() {
        super();
    }

    protected void setUp() throws Exception {
        super.setUp();
        ArrayList<Trip> data = new ArrayList<Trip>();

        trip1 = new Trip();
        trip1.setDestination("Dest1");
        trip1.setStartDate(DateUtil.addMonths(new Date(), 0));
        trip1.setEndDate(DateUtil.addMonths(new Date(), 1));
        data.add(trip1);
        mAdapter = new TripAdapter(data);
    }

    @SmallTest
    public void testGetItem() {
        assertEquals("Dest1 was expected.", trip1.getDestination(),
                ((Trip) mAdapter.trips.get(0)).getDestination());
    }

    public void testGetCount() {
        assertEquals("Contacts amount incorrect.", 1, mAdapter.getItemCount());
    }


}