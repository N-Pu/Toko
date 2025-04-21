# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile




#-keepclassmembers class * {
#    java.util.List *;
#}




# Оставить все лямбды Compose (часто удаляются)
#-keepclassmembers class * {
#    @androidx.compose.runtime.Composable *;
#}
#-keep class ** {
#    @androidx.compose.runtime.Composable <methods>;
#}
#-keepclassmembers class * implements kotlin.jvm.functions.Function1 {
#    *;
#}

# Retrofit
-keep class com.project.toko.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Gson
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class com.google.gson.** { *; }

# Kotlin metadata
#-keep class kotlin.Metadata { *; }

# Для data-классов с вложенными моделями
#-keepclasseswithmembers class * {
#    <init>(...);
#}

