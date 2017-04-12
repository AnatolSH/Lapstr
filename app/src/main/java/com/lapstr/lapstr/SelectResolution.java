package com.lapstr.lapstr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.media.CamcorderProfile;
import android.os.Bundle;

public class SelectResolution extends DialogFragment {
    String selection;
    public static Integer size = CamcorderProfile.QUALITY_TIME_LAPSE_HIGH;
    final CharSequence[] items = {"4k 2160p","Full HD 1080p","HD 720p","SD 480p","QVGA 240p"};
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose resolution").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                switch (arg1){
                    case 0:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_2160P;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 1:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_1080P;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 2:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_720P;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 3:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_480P;
                        getActivity();
                        arg0.dismiss();
                        break;
                    case 4:
                        selection = (String) items[arg1];
                        size = CamcorderProfile.QUALITY_TIME_LAPSE_QVGA;
                        getActivity();
                        arg0.dismiss();
                        break;
                }
            }
        });

        return builder.create();
    }
}
