package com.fan.framework.base;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import com.fan.framework.config.FFConfig;
import com.fan.framework.http.FFNetWork;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFLogUtil;

public abstract class FFApplication extends Application implements
		UncaughtExceptionHandler {

	public static FFApplication app;
	private static Thread mUiThread;
	FFNetWork net;

	@Override
	public void onCreate() {
		super.onCreate();
		net = new FFNetWork(null);
		handler = new Handler();
		mUiThread = Thread.currentThread();
		app = this;
		// 捕获全局异常
		if (FFConfig.DEBUG)
			Thread.setDefaultUncaughtExceptionHandler(this);
		// 监听网络状态变化
		registerReceiver(new NetBroadcastReceiver(), new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	public static Activity getTopActivity() {

		return FFActivity.allActivities.isEmpty() ? null
				: FFActivity.allActivities.get(0);
	}

	public static <T> void post(String url, Class<T> clazz,
			FFNetWorkCallBack<T> callBack, Object... param) {
		app.net.post(url, null, callBack, clazz, param);
	}

	public static <T> void get(String url, Class<T> clazz,
			FFNetWorkCallBack<T> callBack, Object... param) {
		app.net.get(url, null, callBack, clazz, param);
	}

	public static <T> void post_synchronized(String url, Class<T> clazz,
			FFNetWorkCallBack<T> callBack, Object... param) {
		app.net.post_synchronized(url, null, callBack, clazz, param);
	}

	public static <T> void get_synchronized(String url, Class<T> clazz,
			FFNetWorkCallBack<T> callBack, Object... param) {
		app.net.get_synchronized(url, null, callBack, clazz, param);
	}

	public static void showToast(final String msg, final String debugMsg) {
		if (Looper.myLooper() == Looper.getMainLooper()) {
			FFLogUtil.e("FFApplication", "主线程");
			if (FFConfig.SHOW_DEBUG_TOAST) {
				SpannableString ss = new SpannableString(debugMsg + "\n" + msg);
				ss.setSpan(new ForegroundColorSpan(0xffff8888), 0,
						debugMsg != null ? debugMsg.toString().length()
								: 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				Toast.makeText(app, ss, Toast.LENGTH_SHORT).show();
			} else {
				if (msg != null) {
					Toast.makeText(app, msg.toString(), Toast.LENGTH_SHORT)
							.show();
					return;
				}
			}

		} else {
			getHandler().post(new Runnable() {
				@Override
				public void run() {
					FFLogUtil.e("FFApplication", "非主线程");
					if (FFConfig.SHOW_DEBUG_TOAST) {
						SpannableString ss = new SpannableString(debugMsg
								+ "\n" + msg);
						ss.setSpan(new ForegroundColorSpan(0xffff8888), 0,
								debugMsg != null ? debugMsg.toString().length()
										: 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
						Toast.makeText(app, ss, Toast.LENGTH_SHORT).show();
					} else {
						if (msg != null) {
							Toast.makeText(app, msg.toString(),
									Toast.LENGTH_SHORT).show();
							return;
						}
					}

				}
			});
		}
	}

	public static void runOnUiThread(Runnable runnable) {
		if (Thread.currentThread() == mUiThread) {
			FFLogUtil.e("FFApplication", "主线程");
			runnable.run();
		} else {
			getHandler().post(runnable);
		}
	}

	private static Handler handler;
	public static int netType;

	public static Handler getHandler() {
		if (handler == null) {
			handler = new Handler(Looper.getMainLooper());
		}
		return handler;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		FFLogUtil.i("uncaughtException", "Application uncaughtException。。。。。。");
		if (FFConfig.DEBUG) {
			exception2file(ex, false);
		}
		ex.printStackTrace();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private void exception2file(Throwable ex, boolean cause) {
		FFLogUtil.i("uncaughtException", cause ? "Cause by: " + ex.toString()
				: ex.toString());
		StackTraceElement[] ss = ex.getStackTrace();
		for (StackTraceElement s : ss) {
			String info = s.toString();
			if (info.startsWith("android.app.ActivityThread.access")) {
				FFLogUtil.i("uncaughtException", "...more");
				break;
			}
			FFLogUtil.i("uncaughtException", info);
		}
		Throwable c = ex.getCause();
		if (c != null) {
			exception2file(c, true);
		}
	}

	public abstract void onNetStatusChanged(boolean isConnect);
}
