package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.semirzahirovic.travelplanner.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by semirzahirovic on 04/12/15.
 */
public class Utils {

    private static ProgressDialog progressDialog;

    public static void showSnackBar(View view, String string) {
        Snackbar snackbar = Snackbar.make(view, string, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void showAlertDialog(final Activity context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Please turn Internet on and try again!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.finish();
            }
        });
        alertDialog.show();
    }

    public static void logout(final Activity context) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("Are you sure you want to log out?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ParseObject.unpinAllInBackground();
                ParseUser.logOut();
                context.finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }
}
