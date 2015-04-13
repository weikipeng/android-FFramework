package com.fan.framework.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fan.framework.utils.FFLogUtil;
/**
 * 判断网络连接发送广播
 * @author maidoumi
 *
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

	private NetworkInfo info;
	private ConnectivityManager connectivityManager;

	@Override
	public void onReceive(Context context, Intent intent) {
//		getAction  隐性的没有指明从哪跳转到哪，需要自定义Action。 
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			connectivityManager = (ConnectivityManager) FFApplication.app
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			info = connectivityManager.getActiveNetworkInfo();
			boolean isConnect = false;
			if(info != null){
				FFApplication.netType = info.getType();
				if(FFApplication.netType == ConnectivityManager.TYPE_MOBILE){
					info.getSubtype();
				}
				isConnect = info != null && info.isAvailable();
			}
			FFApplication.app.onNetStatusChanged(isConnect);
			for (Activity activity : FFActivity.allActivities) {
				((FFActivity) activity).onNetStatusChanged(isConnect);
			}
		}
	}
}
