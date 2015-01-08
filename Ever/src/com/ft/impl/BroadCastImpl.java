package com.ft.impl;


/**
 * 广播相关字符串定义
 * @author 000814
 *
 */
public interface BroadCastImpl {
	
	public static final String MY_ACTION = "com.yrz.atourong.action.MyAction";
	public static final String MY_RESON = "com.yrz.atourong.action.MyReson";
	public static final String EXIT_EXTRA="UPGActionExitExtra";
	
	public static final String SYSTEM_REASON = "reason";
    public static final String SYSTEM_HOME_KEY = "homekey";// home key
    public static final String SYSTEM_RECENT_APPS = "recentapps";// long home key
	
    public static final String MY_RESON_LOGIN_STATUS="HOME_HOOK_HOME_LOGIN";//广播发送字符串  来源:登录状态改变
    public static final String MY_RESON_FINANCE_SUCCESS_URL = "action_finance_success";
    public static final String MY_RESON_ICON_SATUS="MARK_HOOK_MARK_ACTION";
    
    public static final String ACTION_FIRST_ACTIVITY_DATA = "first_activity_data";
    
}
