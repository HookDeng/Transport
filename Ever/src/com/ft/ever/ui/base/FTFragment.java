package com.ft.ever.ui.base;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ft.ever.utils.Log;
import com.ft.ever.utils.NetUtils;
import com.ft.http.UPGHttpClient;
import com.hook.ever.BuildConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FTFragment extends Fragment {
	private static final boolean DEBUG = BuildConfig.DEBUG && false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(DEBUG) Log.d("%s----------onCreate", getTag());
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(DEBUG) Log.d("%s----------onActivityCreated", getTag());
		if(DEBUG) Log.d("%s----------savedInstanceState == %s ", getTag(), savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v =  super.onCreateView(inflater, container, savedInstanceState);
		if(DEBUG) Log.d("%s----------onCreateView", getTag());
		return v;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(DEBUG) Log.d("%s----------onActivityResult", getTag());
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(DEBUG) Log.d("%s----------onAttach", getTag());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(DEBUG) Log.d("%s----------onDestroy", getTag());
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(DEBUG) Log.d("%s----------onConfigurationChanged", getTag());
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(DEBUG) Log.d("%s----------onDestroyView", getTag());
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		if(DEBUG) Log.d("%s----------onDetach", getTag());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(DEBUG) Log.d("%s----------onPause", getTag());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(DEBUG) Log.d("%s----------onResume", getTag());
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(DEBUG) Log.d("%s----------onStart", getTag());
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if(DEBUG) Log.d("%s----------onStop", getTag());
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(DEBUG) Log.d("%s----------onSaveInstanceState", getTag());
	}
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if(DEBUG) Log.d("%s----------onViewStateRestored", getTag());
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if(DEBUG) Log.d("%s----------onViewCreated", getTag());
	}
	
	protected void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		if(!NetUtils.isNetworkConnected(getActivity())){
			 String message = "网络连接异常,请检查网络";
			 try {
				showToast(getActivity(), message);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		UPGHttpClient.post(getActivity(),url, params, responseHandler);
	}
	
	public void showToast(final Activity mContext,final String message) {
		new Thread(new Runnable() {  
            public void run() {  
            	try {
					Looper.prepare();
					Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
							.show();
					Looper.loop();
				} catch (Exception e) {
					// TODO: handle exception
				}
            }  
        }).start();  
	}

}
