package com.ft;


import android.app.Application;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AtrApplication extends Application {
//public class AtrApplication extends Application {
	private static AtrApplication me;
	public boolean is_need_commit = true;
	public String key_words = "";
	public String mDeployVer = "";
	public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        if (mUserInfo != null) {
//            params.put("oauth_token", mUserInfo.getToken());
//            params.put("oauth_token_secret", mUserInfo.getSecret());
//        }
        //UPGHttpClient.post(url, params, responseHandler);
    }
	
	@Override
	public void onCreate() {
		super.onCreate();
//		FrontiaApplication.initFrontiaApplication(this);
//		me = this ;
//		ResourceUtils.init(this);
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		// 注册crashHandler
//		crashHandler.init(getApplicationContext(),this);
	}

    public static AtrApplication getInstance() {
         return me;
         
//         AtrApplication application = AtrApplication.getInstance();
    }

//    public UserInfo mUserInfo;
    
    
}