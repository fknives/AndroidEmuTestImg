# Android Emulator Test Image

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

This is a simple Java app to generate and publish my DockerImages.
The DockerImages I am planning to use with `/dev/kvm` enabled Containers so I can run my tests androidTests on Emulators in my CI with acceptable speed.

## To Use
The images are published [here on DockerHub](https://hub.docker.com/repository/docker/fknives/android-test-img).

> The Images are not yet tested, I consider testing them in the future (once I figure out how).

To use as a base image: `FROM fknives/android-test-img:<version>`

To use in CI: `image: fknives/android-test-img:1.0.0`

*Feel free to use or suggest alternatives.*

## To Generate

To generate the Java program is used. (For ease of maintenance, since Kotlin is the language I use the most)
To run I use the following command: `./gradlew run --args="$(pwd)/configuration.toml`

A standalone JAR could be generated if needed.

> Note: GitHub Action is created to run the script.

## Contains
The Generated images contain Android BuildTools, SDKs, Gradle for easy Android builds and creation of Emulators.

It contains a `androidemulatorstart` script, which based on the `$EMULATOR_API_LEVEL` ENV variable creates an Emulator then boots it.
The script can be found [here](./main/src/main/resources/startemulator).

## Configuration
There are a couple of things to configure in the images, described here with the format and example:
```toml
# UPDATE: build tools versions can be found via sdkmanager --list and filtering it. Example: `sdkmanager --list | grep -o "build-tools;[0-9.][0-9.]*" | sort | uniq`
# build tools installed into every image
buildTools = ["33.0.0", "32.0.0", "31.0.0", "30.0.3", "30.0.2"]
# UPDATE: sdk versions can be found via sdkmanager --list and filtering it. Example: `sdkmanager --list | grep -o "android-[0-9.][0-9.]*" | sort | uniq`
# sdks installed into every image
sdks = [30, 31, 32, 33]
# UPDATE: latest command lines version here: https://developer.android.com/studio#command-tools
androidCommandlineTools = "8512546_latest"
# UPDATE: gradle version can be found in projects gradle-wrapper.properties
gradleVersion = "7.3.3"
# folder to save generated images into
output = "outputs"

# additional variations
[variations]
# apiLevel variation create separate image with emulator sdk installed and emulator created
# this will create two additional images like fknives/android-test-img:1.0.0-api-21, fknives/android-test-img:1.0.0-api-25
# This images depend on fknives/android-test-img:1.0.0 and add an additional SDK and create the emulator itself.
apiLevels = [21,25]

# the docker image description
# <repository>/<namespace>:<tagPrefix><variationtag>
# Example: fknives/android-test-img:1.0.0, fknives/android-test-img:1.0.0-api-21
[image]
repository = "fknives"
namespace = "android-test-img"
tagPrefix = "1.0.0"
```

## Example Dockerfile
<details>
<summary>Dockerfile</summary>

```Dockerfile
FROM openjdk:11-jdk-slim
# Generated on 2022-08-25

# installing usual required tools
# build-essential, ruby and bundler is needed for fastlane
RUN apt-get update && apt-get install -y \
    sudo \
    wget \
    curl \
    unzip \
    android-sdk \
    build-essential \
    ruby-full && \
    gem install bundler

# versions
# latest command lines version here: https://developer.android.com/studio#command-tools
ENV ANDROID_COMMAND_LINES_TOOLS_VERSION "8512546_latest"
# gradle version can be found in projects gradle-wrapper.properties
ENV GRADLE_VERSION "7.3.3"

# set homes and paths
ENV ANDROID_HOME "/usr/lib/android-sdk"
ENV ANDROID_SDK_ROOT $ANDROID_HOME
ENV CMDLINE_TOOLS_ROOT "${ANDROID_HOME}/cmdline-tools/latest/bin"
ENV AVD_HOME "/root/.android/avd"
ENV PATH "$ANDROID_HOME/cmdline-tools/latest/bin:${PATH}"
ENV PATH "$ANDROID_HOME/emulator:${PATH}"
ENV PATH "/usr/local/gradle-${GRADLE_VERSION}/bin:${PATH}"

WORKDIR /root/

# install gradle
RUN curl -sSL -o /tmp/gradle.zip https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip && \
    unzip -d /usr/local /tmp/gradle.zip && \
    rm -rf /tmp/gradle.zip && \
    echo "1" | gradle init && \
    /root/gradlew && \
    rm -rf /root/* && \
    rm /root/.gitignore

# install command line tools
RUN wget https://dl.google.com/android/repository/commandlinetools-linux-$ANDROID_COMMAND_LINES_TOOLS_VERSION.zip && \
    unzip commandlinetools-linux-$ANDROID_COMMAND_LINES_TOOLS_VERSION.zip -d cmdline-tools && \
    rm commandlinetools-linux-$ANDROID_COMMAND_LINES_TOOLS_VERSION.zip && \
    mv cmdline-tools/cmdline-tools/ cmdline-tools/latest && \
    mv cmdline-tools $ANDROID_HOME/

# install emulator and setup sdkmanager
RUN yes | sdkmanager --licenses > /dev/null && \
    sdkmanager --install emulator --channel=0 > /dev/null

# support libraries and google play services
RUN echo y | sdkmanager "extras;android;m2repository" && \
    echo y | sdkmanager "extras;google;m2repository" && \
    echo y | sdkmanager "extras;google;google_play_services"

# build tools versions and sdk versions can be found via sdkmanager --list and filtering it
# install build-tools
# example: `echo y | sdkmanager "build-tools;33.0.0"`
RUN echo y | sdkmanager "build-tools;33.0.0" && \
    echo y | sdkmanager "build-tools;32.0.0" && \
    echo y | sdkmanager "build-tools;31.0.0" && \
    echo y | sdkmanager "build-tools;30.0.3" && \
    echo y | sdkmanager "build-tools;30.0.2"

# install sdks
# example: `echo y | sdkmanager "platforms;android-33"`
RUN echo y | sdkmanager "platforms;android-30" && \
    echo y | sdkmanager "platforms;android-31" && \
    echo y | sdkmanager "platforms;android-32" && \
    echo y | sdkmanager "platforms;android-33"

# copy script to install sdk, create emulator and boot it
COPY ./startemulator /usr/local/bin/androidemulatorstart
RUN chmod 744 /usr/local/bin/androidemulatorstart

CMD ["/bin/bash"]
```
</details>

## License
[License file](./LICENSE)