package com.ft.ever.ui.base;


import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ft.ever.R;

/** 继承该类并实现其五个抽象方法即可 */
public abstract class AbsActivityGroup extends ActivityGroup{
	
	public boolean backFlag=false;
	public int tempId;
	public int tempBackId;
	
	/** 源Intent */
	protected Intent fromIntent;
	
	/** 功能模块跳转的目标Intent */
	protected Intent targetIntent = new Intent();
	
//	/** Group的标题控件 */ 
//	protected TextView topBarTitleTextView;
//	
//	/** Group的标题内容 */ 
//	protected String topBarName = "";
	
	/** 用来加载子Activity的布局 */
	private FrameLayout container = null;
	
	/** 选项卡 */
	private RadioGroup radioGroup = null;
	
	/** 选中标签的ID */
	private int radioGroupCheckId;
	
	/** 切换标签动作的标志位 */
	private boolean changedFlag;
	
	/** 选项卡的所有标签 */
	public RadioButton[] radioButtons = null;
	
	/** 选项卡所有标签的ID */
	private int[] radioButtonIds = null;
	
	/** 选项卡所有标签的图标ID */
	private int[] radioButtonImageIds;
	
	/** 选项卡所有标签的文字 */
	private String[] radioButtonTexts;
	
	/** 标签ID对应的初始Activity集合 */
	private Map<Integer,Class<? extends Activity>> classes = new HashMap<Integer,Class<? extends Activity>>();
	
	/** 标签ID对应的当前Activity集合 */
	public Map<Integer,Class<? extends Activity>> currentClasses = new HashMap<Integer,Class<? extends Activity>>();;
	
	/**
	 * 在子类中实现的设定布局的方法，Activity的布局的id必须为activity_group_container；
	 * 选项卡的id必须为activity_group_radioGroup
	 */
	protected abstract int getLayoutResourceId();
	
	/** 在子类中需要实现的获取选项卡所有标签的ID的方法 */ 
	protected abstract int[] getRadioButtonIds();
	
	/** 在子类中需要实现的获取选项卡所有标签的图标的方法 */ 
	protected abstract int[] getRadioButtonImageIds();
	
	/** 在子类中需要实现的获取选项卡所有标签的文字的方法 */ 
	protected abstract String[] getRadioButtonTexts();
	
	/** 在子类中需要实现的获取选项卡所有标签ID对应的初始Activity的方法 */ 
	public abstract Class<? extends Activity>[] getClasses();
	
	private SharedPreferences mPreferences;
	private int isMark = 0;
	private int isMarkFinance = 0;
	private int activityIsMark = 0;
	
	protected MarkBroadCastReceiver  markCastReceiver;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());         
        // 获取源Intent
		fromIntent = getIntent();
		// 设定原始数据
        setData();
        // 初始化控件
        initWidgetStatic();
        markCastReceiver=new MarkBroadCastReceiver();
        registerReceiver(markCastReceiver, new IntentFilter(
		"MARK_HOOK_MARK_ACTION"));
    }
			
	/** 设定数据源的方法 */ 
	protected  void setData(){
		radioButtonIds = getRadioButtonIds();
		radioButtonImageIds = getRadioButtonImageIds();
		radioButtonTexts = getRadioButtonTexts();
		Class<? extends Activity>[] classNames = getClasses();
		for(int i=0;i<radioButtonIds.length;i++){
			classes.put(radioButtonIds[i], classNames[i]);
			currentClasses.put(radioButtonIds[i], classNames[i]);
		}
	}
	
	/** 初始化控件 */ 
	protected void initWidgetStatic(){
		container = (FrameLayout) findViewById(R.id.activity_group_container);
		radioGroup = (RadioGroup) findViewById(R.id.activity_group_radioGroup);
		radioButtons = new RadioButton[radioButtonIds.length];
		for(int i=0;i<radioButtons.length;i++){
			radioButtons[i] = (RadioButton) findViewById(radioButtonIds[i]);
			if(radioButtonImageIds != null){
				radioButtons[i].setText(radioButtonTexts[i]);
				Drawable drawable = getResources().getDrawable(radioButtonImageIds[i]);		
				radioButtons[i].setCompoundDrawablesWithIntrinsicBounds(null,
						drawable, null, null);
			}
			radioButtons[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
						if(!changedFlag){//点击当前状态下tab
//							if(getCurrentActivity().getClass().equals(HomeActivity.class)){
//								changedFlag = false;
//								return ;
//							}
//							targetIntent.setClass(AbsActivityGroup.this,
//									classes.get(radioGroupCheckId));
//							launchActivity(targetIntent);
//							String name=classes.get(radioGroupCheckId).getName();
//							if(name.equals(ManageFinanceActivity.class.getName())){
//								MemorySave.MS.goToFinanceAll=true;
//							}else if(name.equals(ManageTransferActivity.class.getName())){
//								MemorySave.MS.mTransferRefresh=true;
//							}
						}else{
//							if(v.getId()==radioButtons[1].getId()){
//								if(getCurrentActivity() instanceof ManageFinanceActivity){
//									ManageFinanceActivity mfActivity=(ManageFinanceActivity) getCurrentActivity();
//									mfActivity.chooseItemNoreloadReALL();
//								}
//							}
						}
					changedFlag = false;
				}
			});
		}

		// 给选项卡设定监听
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				changedFlag = true;
				tempId=0;//重置gotemp()跳转，如果不是指向账户的登录不做记录
				if(!backFlag){
//					String name=classes.get(checkedId).getName();
//					if(name.equals(AccountActivity.class.getName())){
////						if(!MemorySave.MS.mIsLogin){
////							boolean isTrue=GoLoginUtil.ToLoginActivityForResult((UPGActivity) AbsActivityGroup.this.getCurrentActivity(),CallBack.RESULT_CODE,"");
////							tempId=checkedId;
////							if(!isTrue)return;
////						}
//					}else if(name.equals(ManageFinanceActivity.class.getName())){
//						MemorySave.MS.isCheck=true;
//						MemorySave.MS.goToFinanceAll=true;
//						//mfActivity.chooseItemNoreloadReALL();
//					}else if(name.equals(ManageTransferActivity.class.getName())){
//						MemorySave.MS.mTransferRefresh=true;
//					}
				}else{
					backFlag=false;
				}
				currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
						.getCurrentActivity().getClass());
				setTargetIntent(checkedId);	
				launchActivity(targetIntent);
				radioGroupCheckId = checkedId;
			}
		});	
		
		// 初始化加载
		radioGroupCheckId = getCheckedRadioButtonId();
		setTargetIntent(radioGroupCheckId);
		launchNewActivity(targetIntent);
	}
	
	/** 设定目标Intent的方法 */
	protected void setTargetIntent(int checkedId){
		targetIntent.setClass(AbsActivityGroup.this, currentClasses.get(checkedId));
	}
	
//	/** 设定目标Intent的方法 */
	public void goIt(int checkedId){
		currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
				.getCurrentActivity().getClass());
		setTargetIntent(checkedId);	
		launchActivity(targetIntent);
		radioGroupCheckId = checkedId;
		radioGroup.check(checkedId);
		changedFlag = true;
	}
	
	/** 设定目标Intent的方法 */
	public void goBackIt(int checkedId){
		currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
				.getCurrentActivity().getClass());
		targetIntent.setClass(AbsActivityGroup.this, classes.get(checkedId));	
		launchActivity(targetIntent);
		radioGroupCheckId = checkedId;
		radioGroup.check(checkedId);
		changedFlag = true;
	}
	
//	/** 设定目标Intent的方法 */
//	public void goFinance(){
//		for (Integer it : classes.keySet()) {
//			String name=classes.get(it).getName();
//			if(name.equals(ManageFinanceActivity.class.getName())){
//				goIt(it);
//				return;
//			}
//		}
//	}
	
	
	/** ActivityGroup加载新的子Activity的方法(创建新的) */ 
	public void launchNewActivity(Intent intent) {
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
					intent.getComponent().getShortClassName()+ getCheckedRadioButtonId(),
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
	}
	
	/** ActivityGroup加载新的子Activity的方法(创建新的) */ 
	public void launchNewActivityForResult(AbsSubActivity requestSubActivity,
			Intent intent, int requestCode) {
//		intent.putExtra("requestCode", requestCode);
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
				intent.getComponent().getShortClassName()+getCheckedRadioButtonId(), 
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
		((AbsSubActivity)getCurrentActivity()).setRequestSubActivity(requestSubActivity);
	}
	
	/** ActivityGroup加载子Activity的方法(先看有没有，有则加载原来的，否则创建新的) */ 
	public void launchActivity(Intent intent) {
//		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
				intent.getComponent().getShortClassName()+getCheckedRadioButtonId(), 
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)).getDecorView());
//		container.addView(getLocalActivityManager().startActivity(
//				intent.getComponent().getShortClassName()+getCheckedRadioButtonId(), 
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
	}
	
	
	
	/** 这个方法用于获取当前ActivityGroup的选项卡按下的单选按钮的ID */  
	public int getCheckedRadioButtonId(){
		return radioGroup.getCheckedRadioButtonId();
	}

//	public void reback(){
//		String classNamet=currentClasses.get(radioGroupCheckId).getName();
//		String classCurrentName=AbsActivityGroup.this.getCurrentActivity().getClass().getName();
//		if(classNamet.equals(classCurrentName))return;
//		backFlag=true;
//		goIt(radioGroupCheckId);//radioGroup.check(tempBackId);
//	}
	
	public void goTemp() {
		// TODO Auto-generated method stub
		if(tempId!=0)goIt(tempId);
	}
	
//	/** 这个方法用于获取当前ActivityGroup的topBarName */ 
//	public String getTopBarName(){
//		return topBarName;
//	}
	
	
	
    private class MarkBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("MARK_HOOK_MARK_ACTION")){//广播通知
				Drawable drawable = null;
				 mPreferences = getSharedPreferences("push_status", Context.MODE_PRIVATE);
			     isMark = mPreferences.getInt("is_mark", 0);
			     activityIsMark = mPreferences.getInt("activity_is_mark", 0);
			}
		}
	}
}
