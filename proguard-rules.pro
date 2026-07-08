# 乐视S50 Air TVBOX 混淆规则

# 保留所有Activity
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# 保留WebView相关
-keep public class android.webkit.WebView
-keep public class android.webkit.WebViewClient
-keep public class android.webkit.WebSettings

# 保留MediaPlayer
-keep public class android.media.MediaPlayer

# 保留Parcelable
-keep public class * implements android.os.Parcelable

# 保留Serializable
-keep public class * implements java.io.Serializable

# 保留注解
-keepattributes *Annotation*

# 保留native方法
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留View构造函数
-keepclassmembers class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留SurfaceView
-keep public class android.view.SurfaceView
-keep public class android.view.SurfaceHolder

# 不混淆资源ID
-keep class **.R$* { *; }

# 乐视系统兼容
-dontwarn com.letv.**
-dontwarn org.json.**
