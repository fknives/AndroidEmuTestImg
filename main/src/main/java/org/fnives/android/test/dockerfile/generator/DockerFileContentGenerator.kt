package org.fnives.android.test.dockerfile.generator

import org.fnives.android.test.dockerfile.configuration.ImageConfiguration
import org.fnives.android.test.dockerfile.util.clock.Clock
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Creates [DockerFileContent] from an [ImageConfiguration] based on the [DockerFileContentConfig].
 *
 * Uses the templates of [DockerFileContentConfig] and replaces the specifics described in the [ImageConfiguration].
 * The templates have keywords which are used to replace with the specific commands defined by [ImageConfiguration]
 */
class DockerFileContentGenerator(
    private val config: DockerFileContentConfig,
    private val clock: Clock
) {

    fun create(imageConfiguration: ImageConfiguration): DockerFileContent {
        val dockerfileName = dockerFileName(imageConfiguration.version())
        val tag = "${config.dockerRepository}/${config.dockerNamespace}:${config.dockerTagPrefix}${tagSuffix(imageConfiguration.version())}"
        val genericTag = "${config.dockerRepository}/${config.dockerNamespace}:${config.dockerTagPrefix}${tagSuffix(null)}"

        val content = when (imageConfiguration) {
            is ImageConfiguration.Generic ->
                config.templateGeneric.setCommandLineToolsVersion(imageConfiguration.commandlineTools)
                    .setGradleVersion(imageConfiguration.gradleVersion)
                    .setInstalledSDKs(imageConfiguration.sdks)
                    .setInstalledBuildTools(imageConfiguration.buildTools)
                    .setDateStamp()
            is ImageConfiguration.ApiVersion ->
                config.templateSpecific.setFromTag(genericTag)
                    .setENVAPILevel(imageConfiguration.version)
                    .setDateStamp()
        }

        return DockerFileContent(
            name = dockerfileName,
            content = content,
            tag = tag
        )
    }

    private fun String.setCommandLineToolsVersion(version: String): String =
        replace(COMMAND_LINES_KEY, version)

    private fun String.setGradleVersion(version: String): String =
        replace(GRADLE_VERSION_KEY, version)

    private fun String.setInstalledSDKs(sdks: Set<Int>): String {
        val sdkInstallCommand = sdks.joinToString(" && \\\n    ") { getSdkInstallCommand(it) }

        return replace(SDK_INSTALL_COMMANDS_KEY, sdkInstallCommand)
    }

    private fun String.setInstalledBuildTools(buildTools: Set<String>): String {
        val buildToolsInstallCommand = buildTools.joinToString(" && \\\n    ") { getBuildToolsInstallCommand(it) }

        return replace(BUILD_TOOLS_INSTALL_COMMANDS_KEY, buildToolsInstallCommand)
    }

    private fun dockerFileName(version: Int?): String =
        if (version == null) {
            "Dockerfile"
        } else {
            "Dockerfile-api-$version"
        }

    private fun tagSuffix(version: Int?): String = if (version == null) "" else "-api-$version"

    private fun String.setFromTag(tagPrefix: String): String {
        val tag = tagPrefix + tagSuffix(null)

        return replace(TAG_KEY, tag)
    }

    private fun String.setENVAPILevel(apiLevel: Int): String = replace(SET_API_LEVEL, apiLevel.toString())

    private fun String.setDateStamp(): String {
        val dateStamp = SimpleDateFormat("yyyy-MM-dd").format(Date(clock.currentTimeMillis()))

        return replace(DATE_STAMP_KEY, "Generated on $dateStamp")
    }

    companion object {
        private const val COMMAND_LINES_KEY = "{{COMMAND_LINE_TOOLS}}"
        private const val GRADLE_VERSION_KEY = "{{GRADLE_VERSION}}"
        private const val SDK_INSTALL_COMMANDS_KEY = "{{SDK_INSTALL_COMMANDS}}"
        private const val BUILD_TOOLS_INSTALL_COMMANDS_KEY = "{{BUILD_TOOLS_INSTALL_COMMANDS}}"
        private const val TAG_KEY = "{{IMG-TAG}}"
        private const val SET_API_LEVEL = "{{API_LEVEL}}"
        private const val DATE_STAMP_KEY = "{{GENERATED_AT}}"

        private fun getSdkInstallCommand(sdk: Int) = "echo y | sdkmanager \"platforms;android-$sdk\""

        private fun getBuildToolsInstallCommand(buildTool: String) = "echo y | sdkmanager \"build-tools;$buildTool\""

        private fun ImageConfiguration.version() = when (this) {
            is ImageConfiguration.ApiVersion -> version
            is ImageConfiguration.Generic -> null
        }
    }
}