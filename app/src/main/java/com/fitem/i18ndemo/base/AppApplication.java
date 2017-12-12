package com.fitem.i18ndemo.base;

import android.app.Application;

import com.fitem.i18ndemo.utils.I18NUtils;

/**
 * Created by Fitem on 2017/12/8.
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //  设置本地化语言
        I18NUtils.setLocale(this);
    }
}
