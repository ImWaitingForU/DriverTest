package com.iwfu.drivertest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Iwfu on 2016/11/10 0010.
 * <p/>
 * 监听网络状态改变的广播
 */
public class NetworkReceiver extends BroadcastReceiver {

    private ConnectivityManager cm;
    private NetworkInfo info;

    public NetworkReceiver (ConnectivityManager cm) {
        this.cm = cm;

    }

    @Override
    public void onReceive (Context context, Intent intent) {
        info = cm.getActiveNetworkInfo ();
        if (info == null || !info.isConnected ()) {
            //网络已连接
            Log.d ("tag", "网络断开");
        } else {
            Log.d ("tag", "网络连接");
        }
    }
}
