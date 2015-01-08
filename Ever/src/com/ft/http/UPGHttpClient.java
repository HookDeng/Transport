/**
 * @author trevorwang
 * 
 * 负责服务请求的静态类
 * 
 * 提供了2个访问服务器的主要方法 get post
 * 
 */

package com.ft.http;

import org.apache.http.HttpEntity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hook.ever.BuildConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 
 * @author trevorwang
 *
 */
public class UPGHttpClient {

	public static enum METHOD {
		GET, POST, DELETE, PUT
	}
	
	//正式环境
//	public static final String DEBUG_BASE_URL = "http://www.yrzdb.com/index.php?app=api&client_source=1&";
//	public static final String RELEASE_BASE_URL = "http://www.yrzdb.com/index.php?app=api&client_source=1&";
//	public static final String RELEASE_BASE_FLOANED_URL = "http://www.yrzdb.com/index.php?app=floaned&client_source=1&";
	
	//测试环境
	public static final String DEBUG_BASE_URL = "http://test.yrz.cn/";
	public static final String RELEASE_BASE_URL = "http://test.yrz.cn/";
	
    private static AsyncHttpClient ASYNC_HTTP_CLIENT = new AsyncHttpClient();

    /**
     * 
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	String absoluteUrl = getAbsoluteUrl(url);
    	ASYNC_HTTP_CLIENT.get(absoluteUrl, params, responseHandler);
    }
    
    public static void get(String url, BinaryHttpResponseHandler responseHandler) {
    	String absoluteUrl = getAbsoluteUrl(url);
        ASYNC_HTTP_CLIENT.get(absoluteUrl, responseHandler);
    }

    public static void post(Activity activity, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	String absoluteUrl = getAbsoluteUrl(url,activity);
    	if(BuildConfig.DEBUG) Log.d("UPGHttpClient",String.format("%s&%s", absoluteUrl, params));
        ASYNC_HTTP_CLIENT.post(absoluteUrl, params, responseHandler);
    }
    
    /**
     * 把数据post到服务器上
     * @param url 相对地址，此方法会直接添加服务器前缀"http://xxxxxxx/index.php?app=api&"
     * @param params @link(RequestParams)
     * @param responseHandler
     */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	String absoluteUrl = getAbsoluteUrl(url);
    	if(BuildConfig.DEBUG) Log.d("UPGHttpClient",String.format("%s&%s", absoluteUrl, params));
        ASYNC_HTTP_CLIENT.post(absoluteUrl, params, responseHandler);
    }
    
    
    public static void post(String url, BinaryHttpResponseHandler responseHandler) {
    	String absoluteUrl = url;
    	if(BuildConfig.DEBUG) Log.d("UPGHttpClient",String.format("%s&%s", absoluteUrl));
        ASYNC_HTTP_CLIENT.post(absoluteUrl, responseHandler);
    }
    
    public static void post(Context context,String url, HttpEntity params, AsyncHttpResponseHandler responseHandler) {
    	String absoluteUrl = getAbsoluteUrl(url);
    	if(BuildConfig.DEBUG) Log.d("UPGHttpClient",String.format("%s&%s", absoluteUrl, params));
        ASYNC_HTTP_CLIENT.post(context, absoluteUrl, params, "", responseHandler);
    }

    /**
     * 该方法获取服务器接口的绝对地址，会自动根据是不是release版本来连接服务器的地址
     * debug版本自动连接测试服务器
     * @param relativeUrl
     * @return
     */
    public static String getAbsoluteUrl(String relativeUrl) {
    	if(BuildConfig.DEBUG) {
    		return DEBUG_BASE_URL + relativeUrl;
    	} else {
    		return RELEASE_BASE_URL + relativeUrl;
    	}
    }
	
    
    public static String getAbsoluteUrl(String relativeUrl,Activity activity) {
    	PackageManager manager = activity.getPackageManager();
    	ApplicationInfo app_info = null;
    	PackageInfo pack_info = null;
		try {
			app_info = manager.getApplicationInfo(activity.getPackageName(),PackageManager.GET_META_DATA);
			pack_info = manager.getPackageInfo(activity.getPackageName(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String brand = android.os.Build.BRAND;//手机品牌
		String version = pack_info.versionName;
    	String channel = app_info.metaData.getString("UMENG_CHANNEL");
    	TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		String DEVICE_ID = tm.getDeviceId();
    	if(BuildConfig.DEBUG) {
    		return DEBUG_BASE_URL + relativeUrl + "?app_version=" + version + "&hmsr=" + channel + "&brand=" + brand + "&deviceId=" + DEVICE_ID;
    	} else {
    		return DEBUG_BASE_URL + relativeUrl + "?app_version=" + version + "&hmsr=" + channel + "&brand=" + brand + "&deviceId=" + DEVICE_ID;
    	}
    }	
}
