# Android多语言切换完美解决方案

最近公司开始做多语言版本，由于之前没有做过，所以在网上搜寻了一番这方面的资料，最后经过实践、总结，写下了这篇文章。[源码Github](https://github.com/Fitem/I18NDemo/)

## 多语言的切换功能

首先，实现多语言的切换功能，参考[Android App 多语言切换](https://jaeger.itscoder.com/android/2016/05/14/switch-language-on-android-app.html)。

1.在res资源文件目录下添加不同语言的values,如图：

![添加多语言.png](http://upload-images.jianshu.io/upload_images/4759690-73a7d8c9faee176e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2.通过修改Configuration中的locale来实现app语言的切换，具体代码如下：
```
Resources resources = context.getResources();
DisplayMetrics dm = resources.getDisplayMetrics();
Configuration config = resources.getConfiguration();
resources.updateConfiguration(config, dm);
```
3.根据本地缓存的type获取对应的locale,其中7.0以上的系统需要另做处理，具体代码如下：
```
Locale locale;
// 应用用户选择语言
switch (type) {
case 0:
//由于API仅支持7.0，需要判断，否则程序会crash(解决7.0以上系统不能跟随系统语言问题)
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
LocaleList localeList = LocaleList.getDefault();
locale = localeList.get(localeList.size() - 1);
} else {
locale = Locale.getDefault();
}
break;
、、、
default:
locale = thLocale;
break;
}
```
4.在AppApplication中初始化时设置本地语言，用于每次启动APP后切换到本地缓存的语言

    //  设置本地化语言
    I18NUtils.setLocale(this);
    
5.在BaseActivity的OnCreate()方法中设置语言，用于处理每次切换系统语言后app语言会跟随系统变化的问题

    if(!I18NUtils.isSameLanguage(this)) {
        I18NUtils.setLocale(this);
        I18NUtils.toRestartMainActvity(this);
    }
    
6.手动切换语言时，先更新locale配置，然后通过跳转到主Activity实现。Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK通过清理掉进程中的所有activity重新配置。

    Intent intent = new Intent(activity, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    activity.startActivity(intent);
    // 杀掉进程，如果是跨进程则杀掉当前进程
    //  android.os.Process.killProcess(android.os.Process.myPid());
    //  System.exit(0);

## 解决7.0以上系统存在的兼容问题

这个问题的解决参考了[Android 7.0 语言设置爬坑](http://www.jianshu.com/p/9a304c2047ff/)。由于Android7.0以上Configuration将通过LocaleList来管理语言，并且系统切换语言后，系统默认语言可能并不在LocaleList顶部[官方API说明](https://developer.android.com/reference/android/os/LocaleList.html#getDefault()/)

进测试得出结论，如果APP手动选择过语言则系统语言是第二个，否则是第一个。获取当前系统locale,代码如下：
```
//由于API仅支持7.0，需要判断，否则程序会crash(解决7.0以上系统不能跟随系统语言问题)
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
 LocaleList localeList = LocaleList.getDefault();
int spType = getLanguageType(AppApplication.getAppContext());
// 如果app已选择不跟随系统语言，则取第二个数据为系统默认语言
if(spType != 0 && localeList.size() > 1) {
    locale = localeList.get(1);
    } else {
        locale = localeList.get(0);
    }
} else {
locale = Locale.getDefault();
}
```

## 7.0以上系统WebView所在Activity没有跟随语言切换问题
更新日期：2017年12月14日15:56:47

在后续的测试发现，在有webView的activitiy中语言并没有随着语言进行切换。参考[多语言切换失效的问题](http://blog.csdn.net/xunmeng_93/article/details/78632210)以及[stackoverflow](https://stackoverflow.com/questions/40398528/android-webview-language-changes-abruptly-on-android-n)后，通过在切换语言之前执行new WebView(context).destroy(）解决。代码：

    // 解决webview所在的activity语言没有切换问题
    new WebView(context).destroy();
    // 切换语言
    Resources resources = context.getResources();
    DisplayMetrics dm = resources.getDisplayMetrics();
    Configuration config = resources.getConfiguration();
    config.locale = getLocaleByType(type);
    LogUtils.logd("setLocale: " + config.locale.toString());
    resources.updateConfiguration(config, dm);

## 总结

自此，多语言切换的问题已经完美解决了。经测试，完全兼容7.0以上系统的多语言切换。具体代码我已上传至[Github](https://github.com/Fitem/I18NDemo/)

## 后续(2020年8月22日17:47:46)

时隔两年，公司再次启动多语言版本，回头看当初的方案，发现很多问题。这次重新进行了更新，目前适用所有系统版本！后续我也会对这次的调整整理成文章更新！

简书地址:http://www.jianshu.com/p/16efe98d4554/)

E-mail:931675174@qq.com