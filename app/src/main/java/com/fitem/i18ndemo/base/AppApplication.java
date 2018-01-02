package com.fitem.i18ndemo.base;

import android.app.Application;
import android.content.Context;

import com.fitem.i18ndemo.utils.I18NUtils;

/**
 * Created by Fitem on 2017/12/8.
 */

public class AppApplication extends Application {
    private static Application baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        //  设置本地化语言
        I18NUtils.setLocale(this);
    }

    public static Context getAppContext() {
        return baseApplication;
    }
}
