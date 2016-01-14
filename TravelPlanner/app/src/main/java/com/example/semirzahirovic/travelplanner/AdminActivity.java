package com.example.semirzahirovic.travelplanner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.UserAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import utils.Utils;

/**
 * Created by semirzahirovic on 03/12/15.
 */
public class AdminActivity extends AppCompatActivity {

    private UserAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    @Bind(R.id.rv)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private ProgressDialog progressDialog;
    public static int USER_FORM = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);
        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new UserAdapter(new ArrayList<ParseUser>());
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewUser();
            }
        });
        loadUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Utils.logout(AdminActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewUser() {
        Intent i = new Intent(AdminActivity.this, UserProfileActivity.class);
        startActivityForResult(i, USER_FORM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USER_FORM && resultCode == RESULT_OK) {
            adapter.loadObjects();
        }
    }

    private void loadUsers() {
        progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setMessage(getString(R.string.loading_users));
        progressDialog.show();
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereNotEqualTo(SignupActivity.USERROLE, "admin");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
                if (e == null) {
                    ParseObject.unpinAllInBackground();
                    ParseObject.pinAllInBackground(list);
                    adapter.users = (ArrayList<ParseUser>) list;
                    adapter.notifyDataSetChanged();
                    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                    mItemTouchHelper = new ItemTouchHelper(callback);
                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                } else {
                    Utils.showSnackBar(recyclerView, e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }
}
