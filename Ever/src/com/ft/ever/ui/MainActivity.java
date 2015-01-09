package com.ft.ever.ui;

import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;

import com.ft.ever.R;
import com.ft.ever.ui.base.AbsActivityGroup;

/**
 * 主页面
 *
 */
public class MainActivity extends AbsActivityGroup{

	private Timer toLoginTimer;
//	private ToLoginTimerTask toLoginTimerTask;
	private SharedPreferences mPreferences;
	private int isMark = 0, isMarkFinance = 0;
	private int activityIsMark = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkAppUpdate();
	    mPreferences = getSharedPreferences("push_status", Context.MODE_PRIVATE);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    Drawable drawable = null;
	    isMark = mPreferences.getInt("is_mark", 0);
	    activityIsMark = mPreferences.getInt("activity_is_mark", 0);
	    if(isMark == 1 || activityIsMark == 1){//判断是否标识  需要修改未持久化
			drawable = getResources().getDrawable(R.drawable.tab_icon_more_new);		
    		radioButtons[3].setCompoundDrawablesWithIntrinsicBounds(null,drawable, null, null);
    	}else{
    		drawable = getResources().getDrawable(R.drawable.tab_icon_more);		
    		radioButtons[3].setCompoundDrawablesWithIntrinsicBounds(null,drawable, null, null);
    	}
	    
		isMarkFinance = mPreferences.getInt("is_mark_finance", 0);
		Drawable drawable2 = null;
		if(isMarkFinance == 1){//判断是否标识  需要修改未持久化
			drawable2 = getResources().getDrawable(R.drawable.tab_icon_finance_new);		
		radioButtons[1].setCompoundDrawablesWithIntrinsicBounds(null,drawable2, null, null);
		}else{
			drawable2 = getResources().getDrawable(R.drawable.tab_icon_finance);		
			radioButtons[1].setCompoundDrawablesWithIntrinsicBounds(null,drawable2, null, null);
		}
	}
	
	// 第一个需要实现的方法，直接返回ActivityGroup实现类的layou布局即可
	// 注意该布局一定要有个id为activity_group_container的布局用来放子Activity的布局
	@Override
	protected int getLayoutResourceId() {
		// 横向排列选项卡
		return R.layout.activity_group_bottom_layout;
		// 如果是纵向排列选项卡，可以返回下面这个布局
		//return R.layout.activity_group_left5_layout;
	}

	// 第二个需要实现的方法，返回layout布局下选项卡对应的radioButton的id
	@Override
	protected int[] getRadioButtonIds() {
		return new int[] { R.id.activity_group_radioButton0,
				R.id.activity_group_radioButton1,
				R.id.activity_group_radioButton2,
				R.id.activity_group_radioButton3,R.id.activity_group_radioButton4};
	}

	// 第三个需要实现的方法，上面一个方法中的radioButton对应的图标，注意图标的尺寸要自己调整到合适大小
	@Override
	protected int[] getRadioButtonImageIds() {
		return new int[] { R.drawable.tab_icon_home, R.drawable.tab_icon_finance, R.drawable.tab_icon_account,
				R.drawable.tab_icon_more,R.drawable.tab_icon_more,};
	}

	// 第四个需要实现的方法，radioButton对应的文字，也就是选项卡标签的文字，
	// 最好不要太长，否则要到布局文件里调整文字大小到适应界面
	@Override
	protected String[] getRadioButtonTexts() {
//		return new String[]{"推荐","投资","理财","财富","更多"};
		return new String[]{"首页","找货","+","找车","更多"};
	}

	// 第五个需要实现的方法，返回每个选项卡对应的第一个子Activity（注意要继承自AbsSubActivity）
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Activity>[] getClasses() {
		Class<? extends Activity>[] classes = new Class[] { HomeActivity.class,
//				ManageFinanceActivity.class,ManageTransferActivity.class, AccountActivity.class, MoreActivity.class};
				HomeActivity.class, HomeActivity.class, HomeActivity.class, HomeActivity.class};
		return classes;
	}
	
	
//	@SuppressWarnings("deprecation")
//	@Override
//	public void onBackPressed() {
////      super.onBackPressed();
//        //把后退事件交给子Activity处理
//        this.getLocalActivityManager().getCurrentActivity().onBackPressed();
//    }
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        return getCurrentActivity().onKeyDown(keyCode, event);
	        //return true;
	    } else {
	        return super.onKeyDown(keyCode, event);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	}
	
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(markCastReceiver);
		if(toLoginTimer != null){
			toLoginTimer.cancel();
		}
		super.onDestroy();
	}
	
	/**
	 * 判断是否有新的版本
	 */
	private void checkAppUpdate(){
		/*	
		 //每天只检查一次
		SharedPreferences sharePreferences = getSharedPreferences("config_setting", Context.MODE_PRIVATE);
		Calendar calendar = Calendar.getInstance();
		String currentDate = String.valueOf(calendar.get(Calendar.YEAR)) + 
			String.valueOf(calendar.get(Calendar.MONTH)) + 
			String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		String saveDate = sharePreferences.getString("update_check_date", "");
		if(!currentDate.equals(saveDate)){
			UpdateManager.getInstance(MainActivity.this).checkUpdateInfo(0);
			sharePreferences.edit().putString("update_check_date", currentDate).commit();
		}
		*/
		//每次启动都检查
//		UpdateManager.getInstance(MainActivity.this).checkUpdateInfo(MainActivity.this, 0);
	}
	
//	private class ToLoginTimerTask extends TimerTask {
//    	@Override
//    	public void run() {
//    		Intent intent = new Intent();
//    		intent.setClass(MainActivity.this, UserRegisterFirActivity.class);
//    		intent.putExtra("intent_flag", 1);
//    		startActivity(intent);
//    	}
//    }

}
