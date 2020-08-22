package com.fitem.i18ndemo.utils;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.fitem.i18ndemo.base.AppApplication;
import com.fitem.i18ndemo.base.AppConstants;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * 多语言工具类
 * Created by Fitem on 2020/03/20.
 */

public class LanguageUtils {

    public static final String SYSTEM_LANGUAGE_TGA = "systemLanguageTag";

    /**
     * 设置App语言
     *
     * @param context 注意上下文需要使用application的上下文
     * @param locale
     */
    public static void setAppLocale(Context context, @NonNull Locale locale) {
        // Android9.0以上系统未做WebView适配，会报Crash：Using WebView from more
        // than one process at once with the same data directory is not supported.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            // 解决webview所在的activity语言没有切换问题
            new WebView(context).destroy();
        }
        // 切换语言
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
        } else {
            config.setLocale(locale);
        }
        resources.updateConfiguration(config, dm);
    }

    /**
     * 更新该context的config语言配置，对于application进行反射更新
     * @param context
     * @param locale
     */
    public static void updateLanguage(final Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        Locale contextLocale = config.locale;
        if (isSameLocale(contextLocale, locale)) {
            return;
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        config.setLocale(locale);
        if (context instanceof Application) {
            Context newContext = context.createConfigurationContext(config);
            try {
                //noinspection JavaReflectionMemberAccess
                Field mBaseField = ContextWrapper.class.getDeclaredField("mBase");
                mBaseField.setAccessible(true);
                mBaseField.set(context, newContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resources.updateConfiguration(config, dm);
    }

    /**
     * 对Application上下文进行替换
     *
     * @param context context
     */
    public static void applyAppLanguage(@NonNull Context context) {
        Locale appLocale = getCurrentAppLocale();
        updateLanguage(context, appLocale);
    }

    /**
     * 获取系统Local
     *
     * @return
     */
    public static Locale getSystemLocale() {
        return Resources.getSystem().getConfiguration().locale;
    }

    /**
     * 获取app缓存语言
     *
     * @return
     */
    private static String getPrefAppLocaleLanguage() {
        SharedPreferences sp = AppApplication.getAppContext().getSharedPreferences(AppConstants.I18N, Context.MODE_PRIVATE);
        return sp.getString(AppConstants.LOCALE_LANGUAGE, "");
   }

    /**
     * 获取app缓存Locale
     *
     * @return null则无
     */
    public static Locale getPrefAppLocale() {
        String appLocaleLanguage = getPrefAppLocaleLanguage();
        if (!TextUtils.isEmpty(appLocaleLanguage)) {
            if (SYSTEM_LANGUAGE_TGA.equals(appLocaleLanguage)) { //系统语言则返回null
                return null;
            } else {
                return Locale.forLanguageTag(appLocaleLanguage);
            }
        }
        return Locale.SIMPLIFIED_CHINESE; // 为空，默认是简体中文
    }

    /**
     * 获取当前需要使用的locale，用于activity上下文的生成
     *
     * @return
     */
    public static Locale getCurrentAppLocale() {
        Locale prefAppLocale = getPrefAppLocale();
        return prefAppLocale == null ? getSystemLocale() : prefAppLocale;
    }


    /**
     * 缓存app当前语言
     *
     * @param language
     */
    public static void saveAppLocaleLanguage(String language) {
        SharedPreferences sp = AppApplication.getAppContext().getSharedPreferences(AppConstants.I18N, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(AppConstants.LOCALE_LANGUAGE, language);
        edit.apply();
    }

    /**
     * 判断是否是APP语言
     *
     * @param context
     * @param locale
     * @return
     */
    public static boolean isSimpleLanguage(Context context, Locale locale) {
        Locale appLocale = context.getResources().getConfiguration().locale;
        return appLocale.equals(locale);
    }

    /**
     * 获取App当前语言
     *
     * @return
     */
    public static String getAppLanguage() {
        Locale locale = AppApplication.getAppContext().getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(language)) { //语言
            stringBuilder.append(language);
        }
        if (!TextUtils.isEmpty(country)) { //国家
            stringBuilder.append("-").append(country);
        }

        return stringBuilder.toString();
    }

    /**
     * 是否是相同的locale
     * @param l0
     * @param l1
     * @return
     */
    private static boolean isSameLocale(Locale l0, Locale l1) {
        return equals(l1.getLanguage(), l0.getLanguage())
                && equals(l1.getCountry(), l0.getCountry());
    }

    /**
     * Return whether string1 is equals to string2.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean equals(final CharSequence s1, final CharSequence s2) {
        if (s1 == s2) return true;
        int length;
        if (s1 != null && s2 != null && (length = s1.length()) == s2.length()) {
            if (s1 instanceof String && s2 instanceof String) {
                return s1.equals(s2);
            } else {
                for (int i = 0; i < length; i++) {
                    if (s1.charAt(i) != s2.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

}
