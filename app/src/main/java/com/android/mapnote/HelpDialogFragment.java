package com.android.mapnote;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Artemy on 15/11/2014.
 */
public class HelpDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Help");
        builder.setMessage("When creating a new reminder:\n" +
                           "Use commas to separate reminder items.\n" +
                           "Use the @ symbol to indicate the location where the reminder takes place.\n" +
                           "Example: pen, pencil @Staples")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close and Continue
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}