# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:/Users/Overtech/AppData/Local/Android/sdk/tools/proguard/proguard-android.txt
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
-dontwarn okio.**
-keep class com.google.**{
<fields>;
<methods>;
}
-keep class com.google.gson.**{
*;
}
-keep public class [com.overtech.lenovo].R$*{
public static final int *;
}
-keep class com.overtech.lenovo.entity.*{
*;
}
-keep class com.overtech.lenovo.widget.*{
*;
}
#-libraryjars libs/BaiduLBS_Android.jar
-keep class com.baidu.**{
*;
}
-keepattributes Signature
-keepattributes InnerClasser
-keep class android.net.http.*
-dontnote android.net.http.*
-keep class org.apache.http.*
-dontnote  org.apache.http.*