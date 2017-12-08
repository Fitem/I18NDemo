package com.fitem.i18ndemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.fitem.i18ndemo.base.app.AppConstants;

import java.util.Locale;

/**
 * Created by Fitem on 2017/12/8.
 */

public class I18NUtils {

    private static final String TAG = "I18NUtils";
    private static Locale thLocale = new Locale("th");

    /**
     * 设置本地化语言
     *
     * @param context
     * @param type
     */
    public static void setLocale(Context context, int type) {
        putLanguageType(context, type);
        if (I18NUtils.isSameLanguage(context)) {
            return;
        }
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        switch (type) {
            case 0:
                config.locale = Locale.getDefault();
                break;
            case 1:
                config.locale = Locale.ENGLISH;
                break;
            case 2:
                config.locale = Locale.CHINESE;
                break;
            default:
                config.locale = thLocale;
                break;
        }

        Log.d(TAG, "setLocale: " + config.locale.getLanguage() + " - " + config.locale.getCountry());
        resources.updateConfiguration(config, dm);
    }

    /**
     * 根据sp数据设置本地化语言
     *
     * @param context
     */
    public static void setLocale(Context context) {
        int type = getLanguageType(context);
        setLocale(context, type);
    }


    public static boolean isSameLanguage(Context context) {
        Locale locale;
        int type = getLanguageType(context);
        // 应用用户选择语言
        switch (type) {
            case 0:
                locale = Locale.getDefault();
                break;
            case 1:
                locale = Locale.ENGLISH;
                break;
            case 2:
                locale = Locale.CHINESE;
                break;
            default:
                locale = thLocale;
                break;
        }
        return locale.equals(Locale.getDefault());
    }

    /**
     * sp存储本地语言类型
     *
     * @param context
     * @param type
     */
    private static void putLanguageType(Context context, int type) {
        SharedPreferences sp = context.getSharedPreferences(AppConstants.I18N, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(AppConstants.LOCALE_LANGUAGE, type);
        edit.commit();
    }

    /**
     * sp获取本地存储语言类型
     *
     * @param context
     * @return
     */
    private static int getLanguageType(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AppConstants.I18N, Context.MODE_PRIVATE);
        int type = sp.getInt(AppConstants.LOCALE_LANGUAGE, 0);
        return type;
    }
}
