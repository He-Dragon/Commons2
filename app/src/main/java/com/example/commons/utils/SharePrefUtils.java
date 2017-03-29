package com.example.commons.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @ClassName: SharePrefUtils
 * @Description:(用于SharedPreferences保存键值对数据，有String boolean 和int类型的)
 * @date: 2017/2/15
 */
@SuppressWarnings("deprecation")
public class SharePrefUtils {
	private volatile static SharePrefUtils sharePref;
	/**
	 * 保存文件的名称
	 */
	public static final String FILE_NAME = "userInfo";
	/**
	 * SharedPreferences对象
	 */
	public static SharedPreferences mPreferences;
	/**
	 * 是否第一次打开app
	 */
	public static final String ISFIRST = "isFirst";


	public SharePrefUtils(Context context) {
		//  Auto-generated constructor stub
		mPreferences = context.getSharedPreferences(FILE_NAME,
				Context.MODE_WORLD_READABLE);
	}

	public static SharePrefUtils getInstance(Context context) {
		if (sharePref == null) {
			synchronized (SharePrefUtils.class) {
				if (sharePref == null) {
					sharePref = new SharePrefUtils(context);
				}
			}
		}
		return sharePref;
	}

	/**
	 * 保存的Boolean值
	 *
	 * @param key
	 * @param value
	 */
	public static void setBoolean(String key, boolean value) {
		Editor editor = mPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 获取保存的Boolean值
	 *
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key, boolean value) {
		return mPreferences.getBoolean(key, value);
	}

	/**
	 * 保存的String值
	 *
	 * @param key
	 * @param value
	 */
	public static void setString(String key, String value) {
		Editor editor = mPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 获取保存的String值
	 *
	 * @param key
	 * @return
	 */
	public static String getString(String key, String value) {
		return mPreferences.getString(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setInt(String key, int value) {
		Editor editor = mPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * @param key
	 * @return
	 */
	public int getInt(String key, int value) {
		return mPreferences.getInt(key, value);
	}

	public static void remove(String key) {
		mPreferences.edit().remove(key).commit();
	}
}
