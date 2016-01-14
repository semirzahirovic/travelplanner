package com.example.semirzahirovic.travelplanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import adapters.TripAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import utils.DateUtil;
import models.Trip;
import utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ItemTouchHelper mItemTouchHelper;
    private ArrayList<Trip> trips;
    private ProgressDialog progressDialog;
    private CharSequence mTitle;
    private TripAdapter adapter;
    @Bind(R.id.rv)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    public static int TRIP_FORM = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        trips = new ArrayList<>();
        adapter = new TripAdapter(trips);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTrip();
            }
        });
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position == 0) {
            getFutureTrips();
        }
        if (position == 1) {
            getPastTrips();
        }
        if (position == 2) {
            getActiveTrips();
        }
        if (position == 3) {
            getNextMonthTrips();
        }
        if (position == 4) {
            Utils.logout(MainActivity.this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add) {
            addNewTrip();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TRIP_FORM && resultCode == RESULT_OK) {
            adapter.loadObjects();
            Toast.makeText(MainActivity.this, data.getExtras().getString("addedTo"), Toast.LENGTH_LONG).show();
        }
    }

    private void addNewTrip() {
        Intent i = new Intent(MainActivity.this, TripFormActivity.class);
        startActivityForResult(i, TRIP_FORM);
    }

    private void getPastTrips() {
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.addDescendingOrder("endDate");
        JSONObject dateAsObj = new JSONObject();
        try {
            try {
                dateAsObj.put("__type", "Date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                dateAsObj.put("iso", DateUtil.formatDateForParse(new Date()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
        query.whereLessThan("endDate", dateAsObj);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.loading_past));
        progressDialog.show();
        query.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> list, ParseException e) {
                if (e == null) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    adapter.trips = (ArrayList<Trip>) list;
                    adapter.notifyDataSetChanged();
                    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                    mItemTouchHelper = new ItemTouchHelper(callback);
                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                    ParseObject.pinAllInBackground(list);
                } else {
                    Utils.showSnackBar(recyclerView, e.getMessage());
                }
            }
        });
    }

    private void getActiveTrips() {
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.addDescendingOrder("startDate");
        final JSONObject dateAsObj = new JSONObject();
        try {
            try {
                dateAsObj.put("__type", "Date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Calendar date = new GregorianCalendar();
                date.set(Calendar.HOUR_OF_DAY, 0);
                date.set(Calendar.MINUTE, 0);
                dateAsObj.put("iso", DateUtil.formatDateForParse(date.getTime()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
        query.whereLessThan("startDate", dateAsObj);
        query.whereGreaterThan("endDate", dateAsObj);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.loading_active));
        progressDialog.show();
        query.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> list, ParseException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (e == null) {
                    adapter.trips = (ArrayList<Trip>) list;
                    adapter.notifyDataSetChanged();
                    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                    mItemTouchHelper = new ItemTouchHelper(callback);
                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                    ParseObject.pinAllInBackground(list);
                } else {
                    Utils.showSnackBar(recyclerView, e.getMessage());
                }
            }
        });
    }


    private void getNextMonthTrips() {
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.whereGreaterThan("startDate", new Date());
        query.addAscendingOrder("startDate");
        JSONObject dateAsObj = new JSONObject();
        try {
            try {
                dateAsObj.put("__type", "Date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                dateAsObj.put("iso", DateUtil.formatDateForParse(DateUtil.addMonths(new Date(), 1)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
        query.whereLessThan("endDate", dateAsObj);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.loading_next_month));
        progressDialog.show();
        query.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> list, ParseException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (e == null) {
                    adapter.trips = (ArrayList<Trip>) list;
                    adapter.notifyDataSetChanged();
                    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                    mItemTouchHelper = new ItemTouchHelper(callback);
                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                    ParseObject.pinAllInBackground(list);
                } else {
                    Utils.showSnackBar(recyclerView, e.getMessage());
                }
            }
        });
    }

    private void getFutureTrips() {
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.whereGreaterThan("startDate", new Date());
        query.addAscendingOrder("startDate");
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.loading_future));
        progressDialog.show();
        query.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> list, ParseException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (e == null) {
                    adapter.trips = (ArrayList<Trip>) list;
                    adapter.notifyDataSetChanged();
                    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                    mItemTouchHelper = new ItemTouchHelper(callback);
                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                    ParseObject.pinAllInBackground(list);
                } else {
                    Utils.showSnackBar(recyclerView, e.getMessage());
                }
            }
        });
    }
}
