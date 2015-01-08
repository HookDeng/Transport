package com.hook.http;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.yrz.atourong.BuildConfig;
import com.yrz.atourong.utils.Log;

public class UPGSyncHttpClient extends SyncHttpClient {
	public static final UPGSyncHttpClient INSTANCE = new UPGSyncHttpClient();

	@Override
	public String onRequestFailed(Throwable arg0, String arg1) {
		Log.e("request content : %s, exception : %s", arg1, arg0);
		return arg1;
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return (BuildConfig.DEBUG ? UPGHttpClient.DEBUG_BASE_URL : UPGHttpClient.RELEASE_BASE_URL) + relativeUrl;
	}

	@Override
	public String post(String relativeUrl, RequestParams params) {
		String absoluteUrl = getAbsoluteUrl(relativeUrl);
		if(BuildConfig.DEBUG) Log.d("UPGSyncHttpClient %s",String.format("%s&%s", absoluteUrl, params));
		return super.post(absoluteUrl, params);
	}
}
