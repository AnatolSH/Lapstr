package com.lapstr.lapstr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class SelectFpsInVideo extends DialogFragment {
    String selection;
    public static Integer fps = 24;
    final CharSequence[] items = {"30 frame","24 frame","15 frame","10 frame","5 frame"};
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("FPS in Video").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                switch (arg1) {
                    case 0:
                        selection = (String) items[arg1];
                        fps = 30;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 1:
                        selection = (String) items[arg1];
                        fps = 24;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 2:
                        selection = (String) items[arg1];
                        fps = 15;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 3:
                        selection = (String) items[arg1];
                        fps = 10;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 4:
                        selection = (String) items[arg1];
                        fps = 5;
                        getActivity();
                        arg0.dismiss();
                        break;
                }
            }
        });

        return builder.create();
    }
}
