package org.fnives.android.test.dockerfile.configuration

/**
 * Describes the specific that should be used to fill out the Dockerfile templates.
 */
sealed class ImageConfiguration {
    data class Generic(
        val buildTools: Set<String>,
        val sdks: Set<Int>,
        val commandlineTools: String,
        val gradleVersion: String
    ) : ImageConfiguration()

    data class ApiVersion(
        val version: Int
    ) : ImageConfiguration()
}