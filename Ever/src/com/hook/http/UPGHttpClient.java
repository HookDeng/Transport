/**
 * @author trevorwang
 * 
 * 负责服务请求的静态类
 * 
 * 提供了2个访问服务器的主要方法 get post
 * 
 */

package com.hook.http;

import org.apache.http.HttpEntity;
import android.content.Context;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yrz.atourong.BuildConfig;

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
	
}
