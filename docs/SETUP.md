# Setup instruction

1. Setup credentials for arifactory. You should set username and password in maven's settings.xml file like:
```
<settings>
  <mirrors>
    <mirror>
      <id>Onegini Artifactory</id>
      <name>libs-release</name>
      <url>https://repo.onegini.com/artifactory/public</url>
      <mirrorOf>*</mirrorOf>
    </mirror>
  </mirrors>
  <servers>
    <server>
      <id>Onegini Artifactory</id>
      <username>your username</username>
      <password>your encrypted password</password>
    </server>
    <server>
      <id>release.onegini.com</id>
      <username>your username</username>
      <password>your encrypted password</password>
    </server>
    <server>
      <id>snapshot.onegini.com</id>
      <username>your username</username>
      <password>your encrypted password</password>
    </server>
  </servers>
</settings>
```

2. Add variables into your global `~/.gradle/gradle.properties` like:
```
artifactory_user=your username
artifactory_password=your encrypted password
artifactory_contextUrl=https://repo.onegini.com/artifactory
```

3. Open project in Android Studio.
