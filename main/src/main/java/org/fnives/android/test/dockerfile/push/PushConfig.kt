package org.fnives.android.test.dockerfile.push

/**
 * Configuration for [DockerBuildAndPush].
 *
 * @param dockerRepository the repository in DockerHub to push into.
 * @param dockerNamespace the namespace inside the [dockerRepository] defined on DockerHub.
 * @param output the path to the output folder.
 */
data class PushConfig(
    val dockerRepository: String,
    val dockerNamespace: String,
    val output: String,
)