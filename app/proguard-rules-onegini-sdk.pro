# Copyright (c) 2016-2018 Onegini B.V.
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

-keep class com.onegini.mobile.sdk.android.internal.* { *; }
-keep class * implements com.onegini.mobile.sdk.android.model.OneginiClientConfigModel { *; }
-keepattributes Signature,InnerClasses,EnclosingMethod
-dontnote com.onegini.mobile.sdk.android.**
-dontwarn android.test.**
-dontwarn com.onegini.mobile.sdk.android.model.OneginiAuthenticator$OneginiAuthenticatorType

# Bouncycastle
-keep class org.spongycastle.jcajce.provider.** { *; }
-dontwarn org.bouncycastle.**
-dontwarn org.spongycastle.**
-dontnote org.spongycastle.**
-dontwarn org.junit.**

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

-dontnote **

# SQLCipher
-keep class net.sqlcipher.** { *; }

-dontwarn com.guardsquare.dexguard.runtime.encryption.EncryptedXWalkCordovaResourceClient
-dontwarn com.guardsquare.dexguard.runtime.encryption.EncryptedSystemWebViewClient
-dontwarn com.guardsquare.dexguard.runtime.encryption.EncryptedCordovaWebViewClient