FROM openjdk:11-jdk-slim
# Generated on 2022-08-26

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