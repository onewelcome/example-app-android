# Copyright (c) 2016-2017 Onegini B.V.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Onegini Mobile Security Platform Android application proguard configuration

-printmapping build/obfuscation-mapping.map
-printseeds build/seeds.txt

-keep class com.onegini.mobile.sdk.android.internal.* { *; }
-keep class * implements com.onegini.mobile.sdk.android.model.OneginiClientConfigModel { *; }
-keepattributes Signature,InnerClasses,EnclosingMethod
-dontnote com.onegini.mobile.sdk.android.**

# Bouncycastle
-keep class org.spongycastle.jcajce.provider.** { *; }
-dontwarn org.bouncycastle.**
-dontwarn org.spongycastle.**
-dontnote org.spongycastle.**

# OkHttp
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-dontnote okio.**
-dontnote okhttp3.**

# Retrofit 2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Samsung FIDO
-keep class com.samsung.sds.fido.** { *; }
-dontwarn com.samsung.sds.fido.**
-dontnote **

# SQLCipher
-keep class net.sqlcipher.** { *; }

###
# Example app dependencies
###

-keep class com.onegini.mobile.exampleapp.SecurityController { *; }

# ButterKnife 7
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# RxJava 1
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}
-dontwarn sun.misc.Unsafe
-dontwarn java.lang.invoke.*

# Retrofit 1
-keep interface retrofit.** { *; }
-keep class retrofit.* { *; }
-dontwarn retrofit.**
-dontnote retrofit.**