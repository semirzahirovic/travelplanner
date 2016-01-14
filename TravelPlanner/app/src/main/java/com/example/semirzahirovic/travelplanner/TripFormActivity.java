package com.example.semirzahirovic.travelplanner;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import utils.DateUtil;
import models.Trip;
import utils.Utils;

/**
 * Created by semirzahirovic on 02/12/15.
 */
public class TripFormActivity extends AppCompatActivity implements View.OnClickListener {

    public static String TRIPID = "TRIPID";
    private String tripid = null;
    private ProgressDialog progressDialog;
    Trip trip;
    @Bind(R.id.startDate)
    TextView startDate;
    @Bind(R.id.endDate)
    TextView endDate;
    @Bind(R.id.comment)
    EditText comment;
    @Bind(R.id.save)
    Button save;
    @Bind(R.id.destination)
    EditText destination;
    private DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
    private Date startdate, enddate;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_form_layout);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras() != null && getIntent().getExtras().get(TRIPID) != null) {
            tripid = (String) getIntent().getExtras().get(TRIPID);
        }
        dateFormatter = new SimpleDateFormat("dd MMM yy", Locale.US);
        if (tripid == null) {
            trip = new Trip();
        } else {
            ParseQuery query = new ParseQuery(Trip.class);
            try {
                trip = (Trip) query.fromLocalDatastore().whereEqualTo("objectId", tripid).get(tripid);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            destination.setText(trip.getDestination());
            comment.setText(trip.getComment());

            if (trip.getStartDate() != null) {
                startdate = trip.getStartDate();
                startDate.setText(DateUtil.formParseFormatToNormal(startdate));

            }
            if (trip.getEndDate() != null) {
                enddate = trip.getEndDate();
                endDate.setText(DateUtil.formParseFormatToNormal(enddate));
            }
        }


        setDateTimeField();
        save.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void setDateTimeField() {
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth, 0, 0);
                enddate = newDate.getTime();
                endDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth, 0, 0);
                startdate = newDate.getTime();
                startDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == save.getId()) {
            if (startdate == null) {
                Utils.showSnackBar(view, getString(R.string.start_date_cant));
            } else if (enddate == null) {
                Utils.showSnackBar(view, getString(R.string.end_date_cant_be_blank));
            } else if (enddate.before(startdate)) {
                Utils.showSnackBar(view, getString(R.string.start_end_date_err));
            } else if (!(destination.getText().toString().length() > 0)) {
                Utils.showSnackBar(view, getString(R.string.dest_blank));
            } else {
                progressDialog = new ProgressDialog(TripFormActivity.this);
                progressDialog.setMessage(getString(R.string.saving_trip));
                progressDialog.show();
                trip.setComment(comment.getText().toString());
                trip.setDestination(destination.getText().toString());
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("destination", trip.getDestination());
                params.put("startDate", startdate);
                params.put("endDate", enddate);
                params.put("comment", trip.getComment());
                params.put("ownerId", ParseUser.getCurrentUser().getObjectId());

                if (tripid != null) {
                    params.put("objectId", trip.getObjectId());
                    ParseCloud.callFunctionInBackground("editTrip", params, new FunctionCallback<Object>() {
                        @Override
                        public void done(Object result, ParseException e) {
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                            if (e == null) {
                                Trip trip;
                                trip = (Trip) result;
                                Intent i = new Intent();
                                if (trip.getStartDate().after(new Date())) {
                                    i.putExtra("addedTo", "Trip added to Future trips");
                                } else if (trip.getEndDate().before(new Date())) {
                                    i.putExtra("addedTo", "Trip added to Past trips");
                                } else if (trip.getStartDate().before(new Date()) && trip.getEndDate().after(new Date())) {
                                    i.putExtra("addedTo", "Trip added to Active trips");
                                }
                                setResult(RESULT_OK, i);
                                setResult(RESULT_OK, i);
                                trip.pinInBackground();
                                finish();
                            } else {
                                Utils.showSnackBar(view, e.getMessage());
                            }
                        }
                    });
                } else {
                    ParseCloud.callFunctionInBackground("addNewTrip", params, new FunctionCallback<Object>() {
                        @Override
                        public void done(Object result, ParseException e) {
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                            if (e == null) {
                                Trip trip;
                                trip = (Trip) result;
                                Intent i = new Intent();
                                if (trip.getStartDate().after(new Date())) {
                                    i.putExtra("addedTo", "Trip added to Future trips");
                                } else if (trip.getEndDate().before(new Date())) {
                                    i.putExtra("addedTo", "Trip added to Past trips");
                                } else if (trip.getStartDate().before(new Date()) && trip.getEndDate().after(new Date())) {
                                    i.putExtra("addedTo", "Trip added to Active trips");
                                }
                                setResult(RESULT_OK, i);
                                trip.pinInBackground();
                                finish();
                            } else {
                                Utils.showSnackBar(view, e.getMessage());
                            }
                        }
                    });
                }
            }
        } else if (view.getId() == endDate.getId()) {
            toDatePickerDialog.show();
        } else if (view.getId() == startDate.getId()) {
            fromDatePickerDialog.show();
        }
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
