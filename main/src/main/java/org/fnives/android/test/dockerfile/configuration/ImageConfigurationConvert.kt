package org.fnives.android.test.dockerfile.configuration

/**
 * Utility function to ease the creation of a generic [ImageConfiguration]
 */
fun ImageConfiguration(configuration: Configuration): ImageConfiguration =
    ImageConfiguration.Generic(
        buildTools = configuration.buildTools,
        sdks = configuration.sdks,
        commandlineTools = configuration.commandlineTools,
        gradleVersion = configuration.gradleVersion,
    )

/**
 * Utility function to ease the creation of a specific [ImageConfiguration], that will depend on a Generic one.
 */
fun ImageConfiguration(version: Int): ImageConfiguration = ImageConfiguration.ApiVersion(version)