package com.fitem.i18ndemo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fitem.i18ndemo.R;
import com.fitem.i18ndemo.base.AppApplication;
import com.fitem.i18ndemo.base.BaseActivity;
import com.fitem.i18ndemo.utils.LanguageUtils;
import com.fitem.i18ndemo.utils.PopUtils;

import java.util.Locale;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private PopupWindow mPopupWindow;

    public static void actionActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_select_language)
    public void toSelectLanguage() {
        showSelectPop();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        TextView mTvApplicationName = findViewById(R.id.tv_application_name);
        TextView mTvActivityName = findViewById(R.id.tv_activity_name);
        mTvApplicationName.setText(AppApplication.getAppContext().getString(R.string.application_name));
        mTvActivityName.setText(R.string.activity_name);
    }

    private void showSelectPop() {
        // 设置contentView
        View contentView = LayoutInflater.from(this)
                .inflate(R.layout.pop_select_language, null);
        mPopupWindow = new PopupWindow(contentView);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置点击事件
        contentView.findViewById(R.id.tv_default_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                toSetLanguage(0);
            }
        });
        contentView.findViewById(R.id.tv_english).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                toSetLanguage(1);
            }
        });

        contentView.findViewById(R.id.tv_chinese).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                toSetLanguage(2);
            }
        });

        contentView.findViewById(R.id.tv_thai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                toSetLanguage(3);
            }
        });

        // 外部是否可以点击
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        //设置动画
        mPopupWindow.setAnimationStyle(R.style.betSharePopAnim);
        PopUtils.setBackgroundAlpha(this, 0.5f);//设置屏幕透明度
        // 显示PopupWindow
        mPopupWindow.showAtLocation(contentView, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                PopUtils.setBackgroundAlpha(MainActivity.this, 1.0f);
            }
        });
    }

    private void toSetLanguage(int type) {
        Locale locale;
        Context context = AppApplication.getAppContext();
        if (type == 0) {
            locale = LanguageUtils.getSystemLocale();
            LanguageUtils.saveAppLocaleLanguage(LanguageUtils.SYSTEM_LANGUAGE_TGA);
        }  else if (type == 1) {
            locale = Locale.US;
            LanguageUtils.saveAppLocaleLanguage(locale.toLanguageTag());
        }else if (type == 2) {
            locale = Locale.SIMPLIFIED_CHINESE;
            LanguageUtils.saveAppLocaleLanguage(locale.toLanguageTag());
        }else if(type == 3){
            locale = new Locale("th");
            LanguageUtils.saveAppLocaleLanguage(locale.toLanguageTag());
        } else {
            return;
        }
        if (LanguageUtils.isSimpleLanguage(context, locale)) {
            Toast.makeText(context, "选择的语言和当前语言相同", Toast.LENGTH_SHORT).show();
            return;
        }
        LanguageUtils.updateLanguage(context, locale);
        MainActivity.actionActivity(context);
    }

}
