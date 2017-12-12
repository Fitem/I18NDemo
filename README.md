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
4.在AppApplication中初始化时设置本地语言，用于每次启动APP后选择本地缓存的语言

    //  设置本地化语言
    I18NUtils.setLocale(this);
    
5.在BaseActivity的OnCreate()方法中设置语言，用于每次切换系统语言后app语言会跟随系统变化的问题

    if(!I18NUtils.isSameLanguage(this)) {
        I18NUtils.setLocale(this);
        I18NUtils.toRestartMainActvity(this);
    }
    
6.切换语言时，先更新locale配置，然后通过跳转到主Activity时间

    Intent intent = new Intent(activity, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    activity.startActivity(intent);
    // 杀掉进程
    //  android.os.Process.killProcess(android.os.Process.myPid());
    //  System.exit(0);

## 解决7.0以上系统存在的兼容问题

这个问题的解决参考了[Android 7.0 语言设置爬坑](http://www.jianshu.com/p/9a304c2047ff/)。由于Android7.0以上Configuration将通过LocaleList来管理语言，并且系统切换语言后，系统默认语言可能并不在顶部[官方API说明](https://developer.android.com/reference/android/os/LocaleList.html#getDefault()/)

所以通过 localeList.get(localeList.size() - 1)获取系统locale,代码如下：
```
//由于API仅支持7.0，需要判断，否则程序会crash(解决7.0以上系统不能跟随系统语言问题)
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
LocaleList localeList = LocaleList.getDefault();
locale = localeList.get(localeList.size() - 1);
} else {
locale = Locale.getDefault();
}
```
## 总结

自此，多语言切换的问题已经完美解决了。经测试，完全兼容7.0以上系统的多语言切换。

我的简书:http://www.jianshu.com/p/16efe98d4554/)

E-mail:931675174@qq.com
