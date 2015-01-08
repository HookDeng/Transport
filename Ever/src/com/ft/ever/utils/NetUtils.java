package com.ft.ever.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 判断网络是否正常
 * 
 * @author: 
 * 
 */
public class NetUtils {
	public static boolean isNetworkConnected(Context context) {  
       if (context != null) {  
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
            if (mNetworkInfo != null) {  
                 return mNetworkInfo.isAvailable();  
            }  
        }  
        return false;  
    } 
}
