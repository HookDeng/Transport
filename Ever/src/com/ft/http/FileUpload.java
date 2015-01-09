package com.ft.http;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.ft.AtrApplication;
import com.ft.ever.BuildConfig;
import com.ft.ever.utils.Log;
import com.ft.ever.utils.ToastUtil;
import com.ft.http.FileUploadProgressEntity.ProgressListener;
import com.loopj.android.http.RequestParams;

public class FileUpload extends AsyncTask<File[], Object, Integer> {
	public static final int STATUS_FAILURE = 0;
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_ALL_SYNCED = 2;
	public static final int STATUS_NO_FILES = 3;

	private HttpClient mHttpClient = null;
	private HttpContext mHttpContext = null;
	private HttpPost mHttpPost = null;

	private Activity mContext;
	private ProgressDialog mProgressDialog;
	private String mEvId;
	private OnUploadedListener mUploadListener;
//	private UserInfo mUserInfo;
	protected AtrApplication mApplication;
	
	/**
	 * 监听文件上传成功事件
	 * @author trevorwang
	 *
	 */
	public static interface OnUploadedListener {
		public void onUploaded(File file, JSONObject result);
	}
	
	public boolean isFinished = false;
	public void setOnUploadListener(OnUploadedListener listener) {
		mUploadListener = listener;
	}
	
	public FileUpload(Activity context) {
		mContext = context;
		mApplication = (AtrApplication) mContext.getApplication();
//		mUserInfo = mApplication.mUserInfo;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setTitle("文件上传");
		mProgressDialog.setMessage("");
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				if (!isCancelled())
					cancel(true);
				ToastUtil.showToast(mContext, "正在取消上传……");
				isFinished = true;
				 mHttpPost.abort();
			}
		}); 
	}

	public void sync(File[] files) {
		if(files == null || files.length == 0) {
			ToastUtil.showToast(mContext, "没有需要上传的文件！");
			return;
		}
		execute(files);
	}

	public void sync(File[] files,String evId) {
		if(files == null || files.length == 0) {
			ToastUtil.showToast(mContext, "没有需要上传的文件！");
			return;
		}		
		mEvId = evId;
		execute(files);
	}
	
	public void sync(File[] files,String evId, OnUploadedListener listener) {
		if(files == null || files.length == 0) {
			ToastUtil.showToast(mContext, "没有需要上传的文件！");
			return;
		}		
		mEvId = evId;
		mUploadListener = listener;
		execute(files);
	}

	public void reSetContext(Activity context) {
		mContext = context;
	}
	
	@Override
	protected Integer doInBackground(File[]... params) {
		File[] files = params[0];
		if (files.length == 0)
			return STATUS_NO_FILES;
		//publishProgress("正在检查需要上传的文件");
		//if(mDir == null) mDir = files[0].getParentFile().getName();
		ArrayList<File> list = new ArrayList<File>();
		for (File f : files) {
			list.add(f);
		}
		if (list.size() == 0) {
			// publishProgress("所有文件已经同步！");
			return STATUS_ALL_SYNCED;
		}
		
		mHttpClient = new DefaultHttpClient();
		mHttpContext = new BasicHttpContext(); 
		
		for (final File f : list) {
			if(isFinished) break;
			try {
				JSONObject json = uploadFile2(mEvId, f, new ProgressListener() {
					@Override
					public void onUpdate(long size, long total) {
						publishProgress("上传：" + f.getName(), String.valueOf(size * 100 / total));
					}
				});
				
				boolean result = isSuccess(json);
				if(BuildConfig.DEBUG) Log.d("---debug---- 出错了么 %b", result );
				if(result) {
					publishProgress(f,json);
				} else {
					return STATUS_FAILURE;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		mHttpClient.getConnectionManager().shutdown();
		return STATUS_SUCCESS;
	}

	@Override
	protected void onPreExecute() {
		isFinished = false;
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		if(values.length > 1) {
			if (values[0] instanceof File) {
				if(BuildConfig.DEBUG) Log.w("99999999=====%s " , ((File)values[0]).getAbsolutePath());
				if(mUploadListener != null) {
					mUploadListener.onUploaded((File)values[0], (JSONObject)values[1]);
				}
			} else {
				if(!mProgressDialog.isShowing() && mContext!= null) mProgressDialog.show();
				mProgressDialog.setMessage(values[0].toString());
				mProgressDialog.setProgress(Integer.valueOf(values[1].toString()));
			}
		} else {
			ToastUtil.showToast(mContext, values[0].toString());
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		if (BuildConfig.DEBUG) Log.w("---debug file upload onPostExecute     result");
		isFinished = true;
		String msg = null;
		switch (result) {
		case STATUS_ALL_SYNCED:
			msg = "所有文件已经同步！";
			break;
		case STATUS_FAILURE:
			msg = "上传失败！";
			break;
		case STATUS_NO_FILES:
			msg = "没有需要同步的文件！";
			break;
		case STATUS_SUCCESS:
		default:
			msg = "上传成功！";
			break;
		}
		mProgressDialog.dismiss();
		if (mContext != null) ToastUtil.showToast(mContext, msg);
	}

	private boolean isSuccess(JSONObject json) {
		if (BuildConfig.DEBUG) Log.d("--debug --- %s", json.toString());
		if (json != null && json.optInt("boolen") == 1) {
			return true;
		}
		return false;
	}

	@Override
	protected void onCancelled(Integer result) {
		if (BuildConfig.DEBUG) Log.w("---debug file upload oncancelled     result");
		isFinished = true;
	}
	
	@Override
	protected void onCancelled() {
		if (BuildConfig.DEBUG) Log.w("---debug file upload oncancelled");
		isFinished = true;
		if (mContext != null) ToastUtil.showToast(mContext, "上传已取消！");
	}

	/**
	 * 上传文件或者文件夹
	 * 
	 * @param file
	 * @return
	 * @throws JSONException
	 */
	@Deprecated
	private JSONObject uploadFile(final File file) throws JSONException, FileNotFoundException {
		String url = "mod=cFile&act=saveFolderFile";
		RequestParams rq = new RequestParams();
//		rq.put("oauth_token", mUserInfo.getToken());
//		rq.put("oauth_token_secret", mUserInfo.getSecret());
//		rq.put("Filedata", file);
		rq.put("folder_name", file.getParentFile().getName());
//		rq.put("attach_type", FileUtils.getAttacheType(file.getName()));
		String result = UPGSyncHttpClient.INSTANCE.post(url, rq);
		return new JSONObject(result);
	}

	private JSONObject uploadFile2(String evId,final File file, ProgressListener listener) throws JSONException {
		if(BuildConfig.DEBUG) {
			//Log.d("-----debug folder %s / file : %s", folder, file.getAbsolutePath());
		}
		String url = "mod=TaskEvidence&act=uploadTaskEvidenceFile";
		FileUploadProgressEntity entity = new FileUploadProgressEntity(listener);
		mHttpPost = new HttpPost(UPGHttpClient.getAbsoluteUrl(url));
		try {
//			entity.addPart("oauth_token", new StringBody(mUserInfo.getToken(), Charset.forName("UTF-8")));
//			entity.addPart("oauth_token_secret", new StringBody(mUserInfo.getSecret(), Charset.forName("UTF-8")));
			entity.addPart("ev_id", new StringBody(evId, Charset.forName("UTF-8")));
			entity.addPart("is_related_evidence", new StringBody("1", Charset.forName("UTF-8")));
//			entity.addPart("attach_type", new StringBody(FileUtils.getAttacheType(file.getName()), Charset.forName("UTF-8")));
			entity.addPart("Filedata", new FileBody(file));
			mHttpPost.setEntity(entity);
			HttpResponse response = mHttpClient.execute(mHttpPost, mHttpContext);
			String serverResponse = EntityUtils.toString(response.getEntity());
			return new JSONObject(serverResponse);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
