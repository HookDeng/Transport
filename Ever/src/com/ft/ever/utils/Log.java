package com.ft.ever.utils;


public class Log {
	public static final boolean LOGD = true;
	public static final String TAG = Log.class.getSimpleName();

	public static void v(String format, Object... args) {
		android.util.Log.v(TAG, logFormat(format, args));
	}
	
	public static void d(String format, Object... args) {
		android.util.Log.d(TAG, logFormat(format, args));
	}
	
	public static void w(String format, Object... args) {
		android.util.Log.w(TAG, logFormat(format, args));
	}
	
	public static void i(String format, Object... args) {
		android.util.Log.i(TAG, logFormat(format, args));
	}
	
	public static void e(String format, Object... args) {
		android.util.Log.e(TAG, logFormat(format, args));
	}

	private static String prettyArray(String[] array) {
		if (array.length == 0) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder("[");
		int len = array.length - 1;
		for (int i = 0; i < len; i++) {
			sb.append(array[i]);
			sb.append(", ");
		}
		sb.append(array[len]);
		sb.append("]");

		return sb.toString();
	}

	/**
	 * Format the arguments
	 * @param format
	 * @param args
	 * @return the formatted string
	 */
	private static String logFormat(String format, Object... args) {
		if (args.length == 0)
			return format;
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof String[]) {
				args[i] = prettyArray((String[]) args[i]);
			}
		}
		String s = String.format(format, args);
		s = "[" + Thread.currentThread().getId() + "] " + s;
		return s;
	}

}
