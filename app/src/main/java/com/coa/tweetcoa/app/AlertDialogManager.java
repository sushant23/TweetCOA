package com.coa.tweetcoa.app;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by sushant on 5/23/14.
 */
public class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * */
    public void showAlertDialog (Context context, String title, String message, Boolean status){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setIcon((status) ? R.drawable.success : R.drawable.fail)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.show();

    }

 }
