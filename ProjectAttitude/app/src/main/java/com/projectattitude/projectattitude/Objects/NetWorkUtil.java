package com.projectattitude.projectattitude.Objects;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Vuk on 3/11/2017.
 */

/**
 * checks if user is connected online or not
 */

public class NetWorkUtil {
    public static int CONNECTED = 1;
    public static int NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return CONNECTED;
        }
        return NOT_CONNECTED;
    }
}
