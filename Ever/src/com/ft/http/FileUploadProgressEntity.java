package com.ft.http;

import java.io.FilterOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

public class FileUploadProgressEntity extends MultipartEntity {
	private ProgressListener mProgressListener;
	public static interface ProgressListener {
		public void onUpdate(long size, long total);
	}
	public FileUploadProgressEntity(ProgressListener listener) {
		super(HttpMultipartMode.BROWSER_COMPATIBLE,null,Charset.forName("UTF-8"));
		mProgressListener = listener;
		if (mProgressListener == null)
			throw new IllegalArgumentException("ProgressListener cannot be null!");
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		super.writeTo(new CountableOutputStream(outstream, mProgressListener,getContentLength()));
	}

	public static class CountableOutputStream extends FilterOutputStream {
		private final ProgressListener mListener;
		private final long mTotal;
		private long mSize;

		public CountableOutputStream(OutputStream out, ProgressListener listener, long total) {
			super(out);
			mListener = listener;
			mTotal = total;
			mSize = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			mSize += len;
			mListener.onUpdate(mSize, mTotal);
		}

		public void write(int b) throws IOException {
			out.write(b);
			mSize += b;
			mListener.onUpdate(mSize, mTotal);
		}

	}
}
