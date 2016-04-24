package com.hhtech.base;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;

/**
 * @author tangtt
 * global context access
 *
 */
public class AppUtils {

	private static Context sContext;
	private static Thread mUiThread;
	
	private static Handler sHandler = new Handler(Looper.getMainLooper());
	
	/**
	 * 在Application的onCreate方法中调用
	 * @param context
	 */
	public static void init(Context context) {
		sContext = context;
		mUiThread = Thread.currentThread();
	}
	
	public static Context getAppContext() {
		return sContext;
	}

	public static AssetManager getAssets() {
		return sContext.getAssets();
	}
	
	public static Resources getResource() {
		return sContext.getResources();
	}
	
	public static boolean inOnUIThread() {
		return Thread.currentThread() == mUiThread;
	}
	
	public static void runOnUI(Runnable r) {
		sHandler.post(r);
	}
	
	public static void removeRunOnUI(Runnable r) {
		sHandler.removeCallbacks(r);
	}
}
