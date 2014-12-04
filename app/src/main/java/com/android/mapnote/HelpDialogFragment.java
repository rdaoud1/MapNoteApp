package com.android.mapnote;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class HelpDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // dialog title
        builder.setTitle("Help");

        // dialog content
        builder.setMessage("When creating a new reminder:\n" +
                           "Use commas to separate reminder items.\n" +
                           "Use the @ symbol to indicate the address of the location where the reminder takes place.\n\n" +
                           "Example: Buy groceries @123 Main St. W Toronto")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close and Continue
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();

    } // end onCreate Dialog
} // end HelpDialogFragment