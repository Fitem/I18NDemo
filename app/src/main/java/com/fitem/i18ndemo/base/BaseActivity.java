package com.fitem.i18ndemo.base;


import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fitem.i18ndemo.utils.LanguageUtils;

import java.util.Locale;

import butterknife.ButterKnife;

/**
 * 基类
 */

/***************使用例子*********************/
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 兼容启动页没有布局的情况
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }
        updateLocale();

        ButterKnife.bind(this);
        this.initView();
    }

    /**
     * 修复Android7.0、7.1系统不能切换语言问题
     */
    private void updateLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            Locale appLocale = LanguageUtils.getCurrentAppLocale();
            if (appLocale != null) {
                LanguageUtils.setAppLocale(this, appLocale);
            }
        }
    }
    /*********************子类实现*****************************/
    //获取布局文件
    public abstract int getLayoutId();

    //初始化view
    public abstract void initView();

}
