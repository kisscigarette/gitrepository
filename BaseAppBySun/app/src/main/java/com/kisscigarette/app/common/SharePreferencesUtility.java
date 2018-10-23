package com.kisscigarette.app.common;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * <B>SharePreferences</B><p>
 * PreferenceManager.getDefaultSharedPreferences
 * 文件存放在/data/data/<package name>/shared_prefs
 */

public class SharePreferencesUtility {
    public final static String NETWORK_NODE = "network_node";
    public static final String FIRST_OPEN = "first_open";
    public static final String IS_LOGIN = "is_login";
    public static final String HEADPHOTO = "head_photo";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_PASSWD = "userPassWd";
    public static final String KEY_PHONE_NUM = "phoneNum";
    public static final String IS_GESTURE = "gesture_key";

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private static SharePreferencesUtility mSharePreferencesUtility = null;

    private static SharePreferencesUtility getInstance() {
        if (SharePreferencesUtility.mSharePreferencesUtility == null) {
            synchronized (SharePreferencesUtility.class) {
                if (SharePreferencesUtility.mSharePreferencesUtility == null)
                    SharePreferencesUtility.mSharePreferencesUtility = new SharePreferencesUtility();
            }
        }
        return SharePreferencesUtility.mSharePreferencesUtility;
    }

    /**
     * 私有构造
     */
    private SharePreferencesUtility() {
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
    }


    public SharedPreferences.Editor getSharePerferencesEdit() {
        if (null == this.mEditor) {
            this.mEditor = this.mSharedPreferences.edit();
        }
        return this.mEditor;
    }

    /**
     * 存储数据
     *
     * @param key   键
     * @param value 需要保存的数据
     */
    public static void save(String key, @NonNull String value) {
        SharePreferencesUtility.getInstance().getSharePerferencesEdit().putString(key, value).commit();
    }

    public static void save(String key, @NonNull int value) {
        SharePreferencesUtility.getInstance().getSharePerferencesEdit().putInt(key, value).commit();
    }

    public static void save(String key, @NonNull float value) {
        SharePreferencesUtility.getInstance().getSharePerferencesEdit().putFloat(key, value).commit();
    }

    public static void save(String key, @NonNull long value) {
        SharePreferencesUtility.getInstance().getSharePerferencesEdit().putLong(key, value).commit();
    }

    public static void save(String key, @NonNull boolean value) {
        SharePreferencesUtility.getInstance().getSharePerferencesEdit().putBoolean(key, value).commit();
    }

    /**
     * 获取数据
     *
     * @param key   键
     * @param value 默认值(当获取不到数据时，返回默认值)
     * @return 存储在sharedPreferences中的数据
     */
    public static String get(String key, @NonNull String value) {
        return SharePreferencesUtility.getInstance().mSharedPreferences.getString(key, value);
    }

    public static Integer get(String key, @NonNull int value) {
        return SharePreferencesUtility.getInstance().mSharedPreferences.getInt(key, value);
    }

    public static Float get(String key, @NonNull float value) {
        return SharePreferencesUtility.getInstance().mSharedPreferences.getFloat(key, value);
    }

    public static Long get(String key, @NonNull long value) {
        return SharePreferencesUtility.getInstance().mSharedPreferences.getLong(key, value);
    }

    public static Boolean get(String key, @NonNull boolean value) {
        return SharePreferencesUtility.getInstance().mSharedPreferences.getBoolean(key, value);
    }

    /**
     * 更改数据
     *
     * @param key   键
     * @param value 需要保存的数据
     */
    public static void update(String key, @NonNull String value) {
        SharePreferencesUtility.getInstance().save(key, value);
    }

    public static void update(String key, @NonNull int value) {
        SharePreferencesUtility.getInstance().save(key, value);
    }

    public static void update(String key, @NonNull float value) {
        SharePreferencesUtility.getInstance().save(key, value);
    }

    public static void update(String key, @NonNull long value) {
        SharePreferencesUtility.getInstance().save(key, value);
    }

    public static void update(String key, @NonNull boolean value) {
        SharePreferencesUtility.getInstance().save(key, value);
    }

    /**
     * 删除键
     *
     * @param key 键
     */
    public static void remove(String key) {
        SharePreferencesUtility.getInstance().getSharePerferencesEdit().remove(key).commit();
    }

    /**
     * 清空数据(全部清空)
     */
    public static void clear() {
        SharePreferencesUtility.getInstance().getSharePerferencesEdit().clear().commit();
    }

    /**
     * 删除SharedPreferences文件
     */
    @Deprecated
    public static void deleteSharePreferences(String fileName) {
        File file = new File("/data/data/" +
                MyApplication.getInstance().getPackageName().toString()
                + "/shared_prefs", fileName + ".xml");
        if (file.exists()) {
            file.delete();
        }
    }
}
