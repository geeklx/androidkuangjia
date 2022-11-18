# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\android-studio\sdk/tools/proguard/proguard-android.chatting_item_file_txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*
# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

#保持枚举 enum 类不被混淆
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#下面的类将不会被混淆，这样的类是需要被jar包使用者直接调用的
#忽略警告
-ignorewarnings
-keepattributes Signature,Deprecated,InnerClasses

-keep class **.R$* {
 *;
}


-keep class com.yuntongxun.rongxin.** {*; }

#友盟
-keep class com.umeng.**{*;}

-keep class org.bytedeco.javacpp.** {*;}

#EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
}

# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#tencent
-keepclassmembers class ** {
    public void on*Event(...);
}
-keepclasseswithmembernames class * {
  native <methods>;
}
-keep class com.tencent.**{*;}

-keep class com.melink.**{*;}

-dontwarn org.eclipse.jdt.annotation.**
-dontwarn android.location.Location
-keep class org.jsoup.**{*;}
-keep class **$CodeBoyJsInterface {
    public void callme(java.lang.String);
    public void shareTencentViews(java.lang.String);
}

-keep class com.melink.**{*;}
-keep class com.yuntongxun.rongxin.CustomCachingGlideModule{*;}



-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#华为推送
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.android.**{*;}

-keep class com.yuntongxun.phoneconference.activity.LauncherActivity  {
  public boolean onKeyDown(int, KeyEvent);
}

-keep class com.yuntongxun.plugin.contact.** { *; }


-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepattributes EnclosingMethod
-keepattributes InnerClasses

-keep class com.yuntongxun.file_transfer.**{*;}
-keepnames class com.yuntongxun.file_transfer$* {
    public <fields>;
    public <methods>;
}
-keep class com.yuntongxun.file_transfer.TaskCmd{
     *;
 }
-keep class com.yuntongxun.file_transfer.TaskCmd$* {
     *;
 }