package com.hook.http;

import java.io.File;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.RequestParams;
import com.yrz.atourong.db.UserInfo;
import com.yrz.atourong.utils.MD5;

public class FileSyncStatus {
	
	public static ArrayList<File> needSync(File dir, UserInfo mUserInfo) {
		return needSync(dir.getName(), dir.listFiles(), mUserInfo);
	}
	
	public static ArrayList<File> needSync(String dir, File[] files, UserInfo mUserInfo) {
		ArrayList<File> al = new ArrayList<File>();
		String url = "mod=cFile&act=checkMd5File";
		try {
			// 判断是否需要坚持文件
			JSONArray filelist = generateCheckList(files);
			if (filelist == null || filelist.length() == 0)
				return al;

			RequestParams rq = new RequestParams();
			rq.put("oauth_token", mUserInfo.getToken());
			rq.put("oauth_token_secret", mUserInfo.getSecret());
			rq.put("fileMd5list", filelist.toString());
			rq.put("folder_name", dir);
			String result = UPGSyncHttpClient.INSTANCE.post(url, rq);
			JSONArray array = new JSONArray(result);

			// 检察是否需要上传
			ArrayList<String> sa = new ArrayList<String>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				if (!obj.has("uploaded") || obj.getInt("uploaded") == 0) {
					String name = obj.getString("file_name");
					sa.add(name);
				}
			}

			for (File f : files) {
				if (sa.contains(f.getName())) {
					al.add(f);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return al;
	}

	private static JSONArray generateCheckList(File[] files) throws JSONException {
		JSONArray filelist = new JSONArray();
		for (File f : files) {
			String md5 = MD5.checkSum(f);
			if (md5 != null) {
				JSONObject obj = new JSONObject();
				obj.put("file_name", f.getName());
				obj.put("md5", md5);
				filelist.put(obj);
			}
		}
		return filelist;
	}
}
