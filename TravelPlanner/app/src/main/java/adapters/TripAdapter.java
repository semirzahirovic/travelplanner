package adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.semirzahirovic.travelplanner.MainActivity;
import com.example.semirzahirovic.travelplanner.R;
import com.example.semirzahirovic.travelplanner.TripFormActivity;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import utils.DateUtil;
import models.Trip;
import utils.Utils;

/**
 * Created by semirzahirovic on 02/12/15.
 */
public class TripAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener, ItemTouchHelperAdapter {

    public ArrayList<Trip> trips;
    public ViewHolder.IMyViewHolderClicks mListener;

    public TripAdapter(ArrayList<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v, new ViewHolder.IMyViewHolderClicks() {
            @Override
            public void onClick(View caller, int position) {
                Intent intent = new Intent(viewGroup.getContext(), TripFormActivity.class);
                intent.putExtra(TripFormActivity.TRIPID, trips.get(position).getObjectId());
                ((Activity) viewGroup.getContext()).startActivityForResult(intent, MainActivity.TRIP_FORM);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.count.setVisibility(View.VISIBLE);
        if ((new Date()).before(trips.get(i).getStartDate())) {
            viewHolder.count.setText(DateUtils.getRelativeTimeSpanString(trips.get(i).getStartDate().getTime()));
        } else {
            viewHolder.count.setVisibility(View.GONE);
        }
        viewHolder.destination.setText(trips.get(i).getDestination());
        viewHolder.start.setText(DateUtil.formParseFormatToNormal(trips.get(i).getStartDate()));
        viewHolder.end.setText(DateUtil.formParseFormatToNormal(trips.get(i).getEndDate()));
        if (trips.get(i).getComment().equals("")) {
            viewHolder.comment.setVisibility(View.GONE);
        } else {
            viewHolder.comment.setText(trips.get(i).getComment());
            viewHolder.comment.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemDismiss(final int position, final View view) {
        trips.get(position).deleteEventually(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Utils.showSnackBar(view, "Trip deleted");
                    trips.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Utils.showSnackBar(view, e.getMessage());
                }
            }
        });

    }


    public void loadObjects() {
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);
        query.fromLocalDatastore();
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.whereGreaterThan("startDate", new Date());
        query.addAscendingOrder("startDate");
        query.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> list, ParseException e) {
                if (e == null) {
                    trips.clear();
                    notifyDataSetChanged();
                    trips.addAll(list);
                    notifyDataSetChanged();
                } else {
                }
            }
        });
    }
}

