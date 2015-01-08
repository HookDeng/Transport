package com.ft.ever.utils;


import android.app.Activity;
import android.widget.Toast;

public class ToastUtil {

	public static void showToast(final Activity activity, final String message) {
		if(null != activity){
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
