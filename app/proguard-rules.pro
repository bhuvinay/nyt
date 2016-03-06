# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/gagandeep/Setups/android-sdk/tools/proguard/proguard-android.txt
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


-printmapping newshour_proguard_mapping_1.0.0.txt

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.examples.android.model.** { *; }
-keep public class com.example.akki.thenewshour.models.**
-keep public class com.example.akki.thenewshour.models.geographicDetail.**
-keep public class com.example.akki.thenewshour.models.mostPopular.**
-keep public class com.example.akki.thenewshour.models.movieReview.**
-keepclassmembers class com.example.akki.thenewshour.models.geographicDetail** {
    *;
}
-keepclassmembers class com.example.akki.thenewshour.models.mostPopular.** {
    *;
}
-keepclassmembers class com.example.akki.thenewshour.models.movieReview.** {
    *;
}
-keepclassmembers class com.example.akki.thenewshour.models.** {
    *;
}

##---------------End: proguard configuration for Gson  ----------

-dontwarn com.android.volley.toolbox.**
-dontwarn com.google.android.gms.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
##---------------End: proguard configuration for Volley  ----------
-keep public class org.jsoup.** {
public *;
}
##---------------End: proguard configuration for Jsoup  ----------
 -keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
##---------------End: proguard configuration for MaterailDesign  ----------

-keep class org.acra.ACRA {
    *;
}
-keep class org.acra.ReportingInteractionMode {
    *;
}
-keepnames class org.acra.sender.HttpSender$** {
    *;
}
-keepnames enum org.acra.ReportField {
    *;
}
-keep public class org.acra.ErrorReporter{
    public void addCustomData(java.lang.String,java.lang.String);
    public void putCustomData(java.lang.String,java.lang.String);
    public void removeCustomData(java.lang.String);
}

-keep public class org.acra.ErrorReporter{
    public void handleSilentException(java.lang.Throwable);
}
## GreenRobot EventBus specific rules ##
# https://github.com/greenrobot/EventBus/blob/master/HOWTO.md#proguard-configuration

-keepclassmembers class ** {
    public void onEvent*(***);
}

# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    public <init>(java.lang.Throwable);
}

# Don't warn for missing support classes
-dontwarn de.greenrobot.event.util.*$Support
-dontwarn de.greenrobot.event.util.*$SupportManagerFragment


##---------------Begin: proguard configuration for ORMLITE  ----------
# OrmLite uses reflection
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keepclassmembers class * {
  public <init>(android.content.Context);
}

-keepclassmembers class **DateTime {
    <init>(long);
    long getMillis();
}
# Keep all model classes that are used by OrmLite
# Also keep their field names and the constructor
-keep @com.j256.ormlite.table.DatabaseTable class * {
    @com.j256.ormlite.field.DatabaseField <fields>;
    @com.j256.ormlite.field.ForeignCollectionField <fields>;
    <init>();
    }
-keepattributes *Annotation*
# Glide specific rules #
# https://github.com/bumptech/glide

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

