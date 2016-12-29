#!/bin/bash
function copyEnvVarsToGradleProperties {
    GRADLE_PROPERTIES=$HOME"/.gradle/gradle.properties"
    export GRADLE_PROPERTIES
    echo "Gradle Properties should exist at $GRADLE_PROPERTIES"

    if [ ! -f "$GRADLE_PROPERTIES" ]; then
        echo "Gradle Properties does not exist"

        echo "Creating Gradle Properties file..."

        mkdir ~/.gradle -p
        touch $GRADLE_PROPERTIES

        echo "Writing artifactory_user and artifactory_password to gradle.properties..."
        echo "artifactory_user=$artifactory_user" >> $GRADLE_PROPERTIES
        echo "artifactory_password=$artifactory_password" >> $GRADLE_PROPERTIES
    fi
}