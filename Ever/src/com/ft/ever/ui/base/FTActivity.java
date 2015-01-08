package com.ft.ever.ui.base;


import java.util.Date;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.ft.AtrApplication;
import com.ft.ever.ui.widget.SlideFinishOnGestureListener;
import com.ft.ever.ui.widget.SlideFinishOnGestureListener.SlideDirection;
import com.ft.ever.utils.NetUtils;
import com.ft.http.UPGHttpClient;
import com.ft.impl.BroadCastImpl;
import com.hook.ever.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FTActivity extends FragmentActivity implements BroadCastImpl{

    
	private MyReceiver myReceiver;
	protected GestureDetector detector; //触摸监听实例
	protected SlideFinishOnGestureListener gestureListener;
	protected SlideDirection slideDirection;
	public boolean flag=false;
//	HomeKeyEventBroadCastReceiver receiver;
    protected AtrApplication mApplication;
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MY_ACTION);
		intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		registerReceiver(myReceiver, intentFilter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		try {
//			unregisterReceiver(receiver);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
	
	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(myReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
	}
	
	/**
	 * 	post request to server for current activity
	 * 
	 * @param url				
	 * @param params			the parameters wanted to submit to the server
	 * @param responseHandler	that used to handle the response
	 * @param needToken			if the token is needed, it will be set as true.
	 */
	public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		if(params == null) throw new IllegalArgumentException("RequestParams cannot be empty!");
		if(!NetUtils.isNetworkConnected(this)){
			 String message = "网络连接异常,请检查网络";
			 showToast(message);
		}
//		if(mApplication != null) {
//            mApplication.post(url, params,responseHandler);
//		}
//		UPGHttpClient.post(url, params, responseHandler);
		UPGHttpClient.post(this, url, params, responseHandler);
	}
	
	/**
	 * show toast notification message
	 * @param message
	 */
	public void showToast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
	
	/** 
     * 得到自定义的progressDialog 
     * @param context 
     * @param msg 
     * @return 
     */  
    public static Dialog createLoadingDialog(Context context, String msg, boolean cancelAble) {  
        LayoutInflater inflater = LayoutInflater.from(context);  
        View view = inflater.inflate(R.layout.layout_custmor_loading, null);// 得到加载view
        TextView tipTextView = (TextView) view.findViewById(R.id.custmor_loadding_tv_message);// 提示文字  
        tipTextView.setText(msg);// 设置加载信息  
        Dialog loadingDialog = new Dialog(context, R.style.style_loading_dialog);// 创建自定义样式dialog
        loadingDialog.setContentView(view);
        loadingDialog.setCancelable(cancelAble);
        return loadingDialog;  
    }
    
    
    
    
    /**
     * 自定义弹出框
     * @return
     */
    public Dialog createDialog(String message, String absoluteStr, String negativeStr, 
    		OnClickListener absoluteListener, OnClickListener negativeListener) {
    	LayoutInflater layoutInflater = LayoutInflater.from(this);
		View view = layoutInflater.inflate(R.layout.layout_dialog_no_message, null);
		TextView titleTV = (TextView) view.findViewById(R.id.dialog_nomessage_tv_title);
		Button absoluteBT = (Button) view.findViewById(R.id.dialog_nomessage_bt_absolute);
		Button negativeBT = (Button) view.findViewById(R.id.dialog_nomessage_bt_negative);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.width = (int)(screenWidth * 0.88);
		titleTV.setLayoutParams(lp);
		
		if(android.os.Build.VERSION.SDK_INT < 12){
			absoluteBT.setBackgroundResource(R.drawable.bg_dialog_button_absolute_12);
			negativeBT.setBackgroundResource(R.drawable.bg_dialog_button_negative_12);
		}else{
			absoluteBT.setBackgroundResource(R.drawable.bg_dialog_button_absolute);
			negativeBT.setBackgroundResource(R.drawable.bg_dialog_button_negative);
		}
		
		titleTV.setText(message);
		absoluteBT.setText(absoluteStr);
		negativeBT.setText(negativeStr);
		
		absoluteBT.setOnClickListener(absoluteListener);
		negativeBT.setOnClickListener(negativeListener);
		
		Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
		dialog.setContentView(view);
		return dialog;
	}
    
    /**
     * 自定义弹出框(带标题)
     * @return
     */
    public Dialog createDialog(String title, String message, String absoluteStr, String negativeStr, 
    		OnClickListener absoluteListener, OnClickListener negativeListener) {
    	LayoutInflater layoutInflater = LayoutInflater.from(this);
		View view = layoutInflater.inflate(R.layout.layout_dialog_have_message, null);
		TextView titleTV = (TextView) view.findViewById(R.id.dialog_message_tv_title);
		TextView messageTV = (TextView) view.findViewById(R.id.dialog_message_tv_message);
		Button absoluteBT = (Button) view.findViewById(R.id.dialog_message_bt_absolute);
		Button negativeBT = (Button) view.findViewById(R.id.dialog_message_bt_negative);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.width = (int)(screenWidth * 0.88);
		titleTV.setLayoutParams(lp);
		
		if(android.os.Build.VERSION.SDK_INT < 12){
			absoluteBT.setBackgroundResource(R.drawable.bg_dialog_button_absolute_12);
			negativeBT.setBackgroundResource(R.drawable.bg_dialog_button_negative_12);
		}else{
			absoluteBT.setBackgroundResource(R.drawable.bg_dialog_button_absolute);
			negativeBT.setBackgroundResource(R.drawable.bg_dialog_button_negative);
		}
		
		titleTV.setText(title);
		messageTV.setText(message);
		absoluteBT.setText(absoluteStr);
		negativeBT.setText(negativeStr);
		
		absoluteBT.setOnClickListener(absoluteListener);
		negativeBT.setOnClickListener(negativeListener);
		
		Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		return dialog;
	}
    
    /**
     * 自定义弹出框(带标题一个按钮)
     * @return
     */
    public Dialog createDialog(String title, String message, String absoluteStr, 
    		OnClickListener absoluteListener) {
    	LayoutInflater layoutInflater = LayoutInflater.from(this);
		View view = layoutInflater.inflate(R.layout.layout_dialog_one_button, null);
		TextView titleTV = (TextView) view.findViewById(R.id.dialog_onebtn_tv_title);
		TextView messageTV = (TextView) view.findViewById(R.id.dialog_onebtn_tv_message);
		Button absoluteBT = (Button) view.findViewById(R.id.dialog_onebtn_bt_absolute);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.width = (int)(screenWidth * 0.88);
		titleTV.setLayoutParams(lp);
		
		absoluteBT.setBackgroundResource(R.drawable.bg_dialog_one_button);
		
//		if(android.os.Build.VERSION.SDK_INT < 12){
//			absoluteBT.setBackgroundResource(R.drawable.bg_dialog_button_absolute_12);
//		}else{
//			absoluteBT.setBackgroundResource(R.drawable.bg_dialog_button_absolute);
//		}
//		
		titleTV.setText(title);
		messageTV.setText(message);
		absoluteBT.setText(absoluteStr);
		
		absoluteBT.setOnClickListener(absoluteListener);
		
		Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
		dialog.setContentView(view);
		return dialog;
	}
    
    /**
     * 发送广播
     * @param reson 广播来源说明
     */
    public void sendBroadcast(String reson){
    	Intent intent=new Intent();
    	intent.setAction(MY_ACTION);
    	intent.putExtra(MY_RESON, reson);
    	sendBroadcast(intent);
    } 
    
	private class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			doReceive(context, intent);
		}
	}
	
	/**
	 * 接收广播
	 * @param context
	 * @param intent 广播来源
	 */
	protected void doReceive(Context context, Intent intent){
		if(intent.getAction().equals(MY_ACTION)){
			if(intent.getStringExtra(MY_RESON).equals(EXIT_EXTRA)){
				FTActivity.this.finish();
			}
		}else{
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev == null) {
			return true;
		}
		if(flag){//过滤右划关闭监听时间
			return super.dispatchTouchEvent(ev);
		}
//		if (this instanceof AbsSubActivity||this instanceof GestureLoginActivity||
//				this instanceof ManageFinanceBidSuccessActivity||this instanceof FundListInfoActivity||
//				this instanceof UserRegisterSecActivity||this instanceof NoviceActivity||
//				this instanceof AgreementActivity||this instanceof UserRegisterFouActivity||
//				this instanceof UserRegisterThiActivity||this instanceof UserRegisterAuthActivity || 
//				this instanceof MainWebActivity || this instanceof DynamicInfoActivity || 
//				this instanceof FirstActivity) {
//			return super.dispatchTouchEvent(ev);
//		}//过滤右划关闭监听时间
		boolean isGesture = false;
//		if (GlobalVars.IS_ENABLE_GESTURE) {
			if (slideDirection == null) {
				slideDirection = SlideDirection.RIGHT;
			}
			if (detector == null) {
			    gestureListener = new SlideFinishOnGestureListener(this, slideDirection);
			    detector = new GestureDetector(this, gestureListener);
			}
			isGesture = detector.onTouchEvent(ev);
//		}
		if (isGesture) {
			return isGesture;
		} else {
			try{
				return super.dispatchTouchEvent(ev);
			}catch(Exception e){
				e.printStackTrace();
			}
			return true;
		}
	}

	public SlideDirection getSlideDirection() {
		return slideDirection;
	}

	public void setSlideDirection(SlideDirection slideDirection) {
		this.slideDirection = slideDirection;
		if (gestureListener != null) {
		    gestureListener.setSlideDirection(slideDirection);
		}
	}
	
	/**
	 * 右划关闭回调
	 */
	public void doFinish(){
		this.finish();
	}
	
}
