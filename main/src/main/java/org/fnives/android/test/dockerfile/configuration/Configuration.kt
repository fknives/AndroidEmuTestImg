package org.fnives.android.test.dockerfile.configuration

import org.fnives.android.test.dockerfile.generator.DockerFileContent

/**
 * Describes all the configurations which can be set inside the [toml][https://toml.io/en/] file.
 *
 * @param buildTools android build tools installed into the DockerImages
 * @param sdks AndroidSDKs installed into the DockerImages
 * @param commandlineTools AndroidCommandLinesTools installed into the DockerImages
 * @param gradleVersion GradleVersion to be installed into the DockerImages
 * @param output the folder which will contain the generated Dockerfiles
 * @param dockerRepository the repository in DockerHub to push into.
 * @param dockerNamespace the namespace inside the [dockerRepository] defined on DockerHub.
 * @param dockerTagPrefix the prefix which should be added to all DockerImage tags when creating [DockerFileContent]
 * @param apiLevelVariations the specific api levels that should have their own image. This is used so everything is ready for the Emulator for a specific API version.
 */
data class Configuration(
    val buildTools: Set<String>,
    val sdks: Set<Int>,
    val commandlineTools: String,
    val gradleVersion: String,
    val output: String,
    val dockerRepository: String,
    val dockerNamespace: String,
    val dockerTagPrefix: String,
    val apiLevelVariations: Set<Int> = emptySet()
)
