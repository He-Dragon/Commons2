package com.example.commons.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class NetUtil {
	/**
	 * 检测手机是否开启GPRS网络,需要调用ConnectivityManager,TelephonyManager 服务.
	 * 
	 * @Title: checkGprsNetwork
	 * @Description:
	 * @param @param context
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean checkGprsNetwork(Context context) {
		boolean has = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if (netType == ConnectivityManager.TYPE_MOBILE
				&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
				&& !mTelephony.isNetworkRoaming()) {
			has = info.isConnected();
		}
		return has;

	}

	/**
	 * 检测手机是否开启WIFI网络,需要调用ConnectivityManager服务.
	 * 
	 * @Title: checkWifiNetwork
	 * @Description:
	 * @param @param context
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean checkWifiNetwork(Context context) {
		boolean has = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if (netType == ConnectivityManager.TYPE_WIFI) {
			has = info.isConnected();
		}
		return has;
	}

	/**
	 * 检测当前手机是否联网
	 *
	 * @Title: isNetworkAvailable
	 * @Description:
	 * @param @param context
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 手机是否处在漫游
	 * 
	 * @Title: isNetworkRoaming
	 * @Description:
	 * @param @param mCm
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isNetworkRoaming(Context c) {
		ConnectivityManager connectivity = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		boolean isMobile = (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE);
		TelephonyManager mTm = (TelephonyManager) c
				.getSystemService(Context.TELEPHONY_SERVICE);
		boolean isRoaming = isMobile && mTm.isNetworkRoaming();
		return isRoaming;
	}

	/**
	 * 
	 * Description: 检测网络状态并友好提示
	 * 
	 * @param pContext
	 * @return
	 */
	public static boolean showNetworkState(Context pContext) {
		if (!isNetworkAvailable(pContext)) {
			Toast.makeText(pContext,"网络不可用！",Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	/**
	 * 弹出网络设置对话框
	 */
	public static void showNetworkSettingDialog(final Activity context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setTitle("无网络").setMessage("没有检测到网络,是否进行网络设置?");

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;
				try {
					int sdkVersion = android.os.Build.VERSION.SDK_INT;
					if (sdkVersion > 10) {
						intent = new Intent(
								android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					} else {
						intent = new Intent();
						ComponentName comp = new ComponentName(
								"com.android.settings",
								"com.android.settings.WirelessSettings");
						intent.setComponent(comp);
						intent.setAction("android.intent.action.VIEW");
					}
					context.startActivityForResult(intent, 100);
				} catch (Exception e) {
					Log.w("", "open network settings failed, please check...");
					e.printStackTrace();
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				context.finish();
			}
		}).show();
	}

}
