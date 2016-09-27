# Android Example App
If you want to see working example app using Onegini SDK then you can open Example App project in Android Studio to test it live.

## Resolving dependencies

Before you can compile the application it must be able to resolve it's dependencies. The Onegini Android SDK is one of those dependencies. We have an 
Artifactory repository that distributes the required dependencies. Make sure that you have access to the Onegini Artifactory repository (https://repo.onegini.com).
If you don't have access please contact Onegini Support. Access to Artifactory is required to let Gradle download the Onegini Android SDK library.

When you have access you have to make sure that your Artifactory username and password are set in the `gradle.properties` file in your Gradle user home 
(e.g. ~/.gradle):

Example contents of the `gradle.properties` file in you Gradle user home:
```
artifactory_user=<username>
artifactory_password=<password>
```

See the documentation below for instructions on setting Gradle properties:
[https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_properties_and_system_properties](https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_properties_and_system_properties)
