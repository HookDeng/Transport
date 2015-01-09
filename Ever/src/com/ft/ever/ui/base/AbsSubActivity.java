package com.ft.ever.ui.base;


import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

/** 继承该类即可实现子Activity的功能 */
public abstract class AbsSubActivity extends FTActivity{
		
	private AbsSubActivity requestSubActivity;
	private Dialog mExitDialog;
	private boolean dialogShowing = false;
	
	private ArrayList<String> mClass = null;
	
	public static final String TAB1="com.ft.ever.ui.HomeActivity";
	public static final String TAB2="com.ft.ever.ui.HomeActivity";
	public static final String TAB3="com.ft.ever.ui.HomeActivity";
	public static final String TAB4="com.ft.ever.ui.HomeActivity";
	public static final String TAB5="com.ft.ever.ui.HomeActivity";
	
	public AbsSubActivity getRequestSubActivity() {
		return requestSubActivity;
	}

	public void setRequestSubActivity(AbsSubActivity requestSubActivity) {
		this.requestSubActivity = requestSubActivity;
	}
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mClass = new ArrayList<String>();
	    mClass.add(TAB1);
	    mClass.add(TAB2);
	    mClass.add(TAB3);
	    mClass.add(TAB4);
	    mClass.add(TAB5);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Intent it = getIntent();
		if(mClass.contains(it.getComponent().getClassName())){
			mExitDialog = createDialog("您确定要退出吗？", "确定", "取消", 
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//解除当前用户推送绑定
//							new BDPushUitl(AbsSubActivity.this).deletePushInfoToServer();
							finish();
							if(mExitDialog.isShowing()){
								mExitDialog.dismiss(); 
							}
						}
					}, 
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(mExitDialog.isShowing()){
								mExitDialog.dismiss(); 
							}
						}
					});
			if(dialogShowing) mExitDialog.show();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(mExitDialog != null && mExitDialog.isShowing()) {
			dialogShowing = mExitDialog.isShowing();
			mExitDialog.dismiss();
		} else {
			dialogShowing = false;
		}
	}
    
//    private Dialog createDialog() {
//    	LayoutInflater layoutInflater = LayoutInflater.from(this);
//		View view = layoutInflater.inflate(R.layout.layout_dialog_no_message, null);
//		TextView titleTV = (TextView) view.findViewById(R.id.dialog_nomessage_tv_title);
//		Button absoluteBT = (Button) view.findViewById(R.id.dialog_nomessage_bt_absolute);
//		Button negativeBT = (Button) view.findViewById(R.id.dialog_nomessage_bt_negative);
//		
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int screenWidth = dm.widthPixels;
//		
//		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		lp.width = (int)(screenWidth * 0.88);
//		titleTV.setLayoutParams(lp);
//		
//		if(android.os.Build.VERSION.SDK_INT < 12){
//			absoluteBT.setBackgroundResource(R.drawable.bg_dialog_button_absolute_12);
//			negativeBT.setBackgroundResource(R.drawable.bg_dialog_button_negative_12);
//		}else{
//			absoluteBT.setBackgroundResource(R.drawable.bg_dialog_button_absolute);
//			negativeBT.setBackgroundResource(R.drawable.bg_dialog_button_negative);
//		}
//		
//		titleTV.setText("您确定要退出吗？");
//		absoluteBT.setText("确定");
//		negativeBT.setText("取消");
//		
//		absoluteBT.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//解除当前用户推送绑定
////				new BDPushUitl(AbsSubActivity.this).deletePushInfoToServer();
//				UPGHttpClient.clearCookie();
//				MomerySave.MS.mIsLogin = false;
//				finish();
//				if(mExitDialog.isShowing()){
//					mExitDialog.dismiss(); 
//				}
//			}
//		});
//		
//		negativeBT.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(mExitDialog.isShowing()){
//					mExitDialog.dismiss(); 
//			       }
//			}
//		});
//		
//		Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
//		dialog.setContentView(view);
//		return dialog;
//	}

	private Class getTargetClass(Intent intent){
		Class clazz = null;
		try {
			if(intent.getComponent() != null)
			clazz = Class.forName(intent.getComponent().getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}
	
	// 重写了startActivity()方法，
	// 这样调用该方法时会根据目标Activity是否是子Activity而调用不同的方法
	@Override
	public void startActivity(Intent intent) {
		if( getTargetClass(intent) != null && AbsSubActivity.class.isAssignableFrom(getTargetClass(intent)) ){
			if(this.getParent() instanceof AbsActivityGroup){
				intent.putExtra("fromSubActivity", getClass().getName());
				((AbsActivityGroup)this.getParent()).launchNewActivity(intent);
			}
		}else{
			super.startActivity(intent);
		}
	}

	// 重写了startActivity()方法，
	// 这样调用该方法时会根据目标Activity是否是子Activity而调用不同的方法
	public void startActivityNoFrom(Intent intent) {
			super.startActivity(intent);
	}
	
	// 重写了startActivityForResult()方法，
	// 这样调用该方法时会根据目标Activity是否是子Activity而调用不同的方法
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if( getTargetClass(intent) != null && AbsSubActivity.class.isAssignableFrom(getTargetClass(intent)) ){
			if(this.getParent() instanceof AbsActivityGroup){
				intent.putExtra("fromSubActivity", getClass().getName());
				((AbsActivityGroup) this.getParent())
						.launchNewActivityForResult(this, intent, requestCode);
			}
		}else{
			super.startActivityForResult(intent, requestCode);
		}
	}
		
	
	/** 调用此方法来返回上一个界面 */
	public void goback() {
		Class clazz = null;
		try {
			Intent intent=getIntent();
			String className=intent.getStringExtra("fromSubActivity");
			clazz = Class.forName(className);
		} catch (Exception e) {
			return;
		}
		Intent intent = new Intent(this,clazz);
		((AbsActivityGroup)this.getParent()).launchActivity(intent);
	}
	
	/** 调用此方法来返回上一个界面并返回数据 */
	public void gobackWithResult(int resultCode, Intent data) {
		Class clazz = null;
		try {
			clazz = Class.forName(getIntent().getStringExtra("fromSubActivity"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		data.setClass(this, clazz);
		if( requestSubActivity != null){
			requestSubActivity.onActivityResult(
					data.getIntExtra("requestCode", 0), resultCode, data);
		}
		((AbsActivityGroup)this.getParent()).launchActivity(data);	
	}
	
//	@Override
//	public void onBackPressed() {
//		Intent it = getIntent();
//		if(mClass.contains(it.getComponent().getClassName())){
//			exit();
//		}else{
//			goback();
//		}
//	}
//	
	protected void exit() {
		dialogShowing = true;
		if(mExitDialog != null) mExitDialog.show();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent it = getIntent();
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	String className=it.getComponent().getClassName();
	    	if(mClass.contains(className)){
				exit();
			}else{
				goback();
			}
	        return true;
	    } else {
	        return super.onKeyDown(keyCode, event);
	    }
	}
	
}
