package com.projectattitude.projectattitude.Objects;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Vuk on 3/11/2017.
 */

/**
 * android is weird with connecting disconnecting, you can follow the Logs.d, so required if
 * statements to filter out the wierdness
 *
 * This can be removed, just thought it'd be nice
 */
public class NetWorkChangeReceiver extends BroadcastReceiver{
    private static boolean firstConnect = true;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            Log.d("test1", "connected attempt");
                Toast.makeText(context, "Connected to internet!",
                        Toast.LENGTH_LONG).show();
                firstConnect = true;
        }

        else {
            Log.d("test1", "disconnect attempt");
            if(firstConnect) {
                Toast.makeText(context, "Disconnected from internet!",
                        Toast.LENGTH_LONG).show();
                firstConnect = false;
            }
        }
    }

}
