package com.fitem.i18ndemo.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by Fitem on 2017/10/31.
 */

public class PopUtils {
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }
}
