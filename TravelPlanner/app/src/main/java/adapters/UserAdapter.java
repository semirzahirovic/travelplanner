package adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.semirzahirovic.travelplanner.AdminActivity;
import com.example.semirzahirovic.travelplanner.R;
import com.example.semirzahirovic.travelplanner.SimpleItemTouchHelperCallback;
import com.example.semirzahirovic.travelplanner.UserProfileActivity;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import models.Trip;
import utils.DateUtil;
import utils.Utils;

/**
 * Created by semirzahirovic on 03/12/15.
 */
public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> implements UserViewHolder.IMyViewHolderClicks, ItemTouchHelperAdapter {
    public ArrayList<ParseUser> users;

    public UserAdapter(ArrayList<ParseUser> users) {
        this.users = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);
        UserViewHolder viewHolder = new UserViewHolder(v, new UserViewHolder.IMyViewHolderClicks() {
            @Override
            public void onClick(View caller, int position) {
                Intent i = new Intent(caller.getContext(), UserProfileActivity.class);
                i.putExtra("userId", users.get(position).getObjectId());
                ((Activity) caller.getContext()).startActivityForResult(i, AdminActivity.USER_FORM);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.username.setText(users.get(position).getUsername());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void onItemDismiss(final int position, final View view) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId", users.get(position).getObjectId());
        ParseCloud.callFunctionInBackground("deleteUserWithId", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object result, ParseException e) {
                if (e == null) {
                    // success
                    users.get(position).unpinInBackground();
                    Utils.showSnackBar(view, "User deleted");
                    users.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Utils.showSnackBar(view, e.getMessage());
                }
            }
        });

    }

    public void loadObjects() {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.fromLocalDatastore();
        query.whereNotEqualTo("userRole", "admin");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    users.clear();
                    notifyDataSetChanged();
                    users.addAll(list);
                    notifyDataSetChanged();
                } else {
                }
            }
        });
    }

    @Override
    public void onClick(View caller, int position) {

    }
}
