# Onegini Mobile Security Platform Android application proguard configuration

-printmapping build/obfuscation-mapping.map
-printseeds build/seeds.txt

-dontwarn retrofit.**
-dontwarn android.test.**
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn org.bouncycastle.**
-dontwarn org.spongycastle.**
-dontwarn rx.*

-keep class com.onegini.mobile.sdk.android.library.internal.* { *; }
-keep class * implements com.onegini.mobile.sdk.android.library.model.OneginiClientConfigModel { *; }

-keep interface retrofit.** { *; }
-keep class retrofit.* { *; }
-keep class com.squareup.okhttp.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.inject.* { *; }
-keep class org.apache.http.* { *; }
-keep class org.apache.james.mime4j.* { *; }
-keep class javax.inject.* { *; }
-keep class sun.misc.Unsafe { *; }
-keep class org.spongycastle.jcajce.provider.** { *; }

-keepattributes Signature,InnerClasses,EnclosingMethod
