package com.fitem.i18ndemo.base;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fitem.i18ndemo.utils.I18NUtils;

import butterknife.ButterKnife;

/**
 * 基类
 */

/***************使用例子*********************/
//1.mvp模式
//public class SampleActivity extends BaseActivity<NewsChanelPresenter, NewsChannelModel>implements NewsChannelContract.View {
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_news_channel;
//    }
//
//    @Override
//    public void initPresenter() {
//        mPresenter.setVM(this, mModel);
//    }
//
//    @Override
//    public void initView() {
//    }
//}
//2.普通模式
//public class SampleActivity extends BaseActivity {
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_news_channel;
//    }
//
//    @Override
//    public void initPresenter() {
//    }
//
//    @Override
//    public void initView() {
//    }
//}
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 兼容启动页没有布局的情况
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }
        //  设置本地化语言
        setLocale();

        ButterKnife.bind(this);
        this.initView();
    }

    /**
     * 设置Locale
     */
    private void setLocale() {
        if (!I18NUtils.isSameLanguage(this)) {
            I18NUtils.setLocale(this);
            I18NUtils.toRestartMainActvity(this);
        }
    }

    /*********************子类实现*****************************/
    //获取布局文件
    public abstract int getLayoutId();

    //初始化view
    public abstract void initView();

}
