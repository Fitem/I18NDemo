package com.fitem.i18ndemo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.LocaleList;

import com.fitem.i18ndemo.utils.I18NUtils;

/**
 * Created by Fitem on 2017/12/11.
 */

public class LocaleChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_LOCALE_CHANGED.equals(intent.getAction())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                I18NUtils.setSystemLocaleList(LocaleList.getDefault());
            }
        }
    }
}
