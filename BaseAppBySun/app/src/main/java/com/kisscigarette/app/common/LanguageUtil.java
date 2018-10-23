package com.kisscigarette.app.common;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;


import java.util.Locale;

public class LanguageUtil {

    public static final String LOCALE_LANGUAGE = "Locale_Language";
    public static final int LOCALE_ZH_RCN = 1;
    public static final int LOCALE_ZH_RTW = 2;
    public static final int LOCALE_ENGLISH = 3;
    public static final int LOCALE_DEFAULT = 0;


    /**
     * 设置语言类型
     *
     * @param cxt
     * @param lang 语言
     */
    public static void selectLang(Context cxt, int lang) {
        switch (lang) {
            case LOCALE_ZH_RCN:
                changeLanguage(cxt, Locale.SIMPLIFIED_CHINESE);
                break;
            case LOCALE_ZH_RTW:
                changeLanguage(cxt, Locale.TRADITIONAL_CHINESE);
                break;
            case LOCALE_ENGLISH:
                changeLanguage(cxt, Locale.ENGLISH);
                break;
            default:
                changeLanguage(cxt, Locale.getDefault());
                break;
        }
        // 保存语言类型
        saveLang(lang);
    }

    /**
     * 保存语言类型
     *
     * @param lang
     */
    private static void saveLang(int lang) {
        SharePreferencesUtility.save(LOCALE_LANGUAGE, lang);
    }

    /**
     * 获取语言类型
     *
     * @return
     */
    public static int getLang() {
        return SharePreferencesUtility.get(LOCALE_LANGUAGE, LOCALE_DEFAULT);
    }

    /**
     * 设置语言类型
     *
     * @param cxt
     * @param locale
     */
    public static void changeLanguage(Context cxt, Locale locale) {
        Resources resources = cxt.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
