package org.fnives.android.test.dockerfile.generator

/**
 * Describes the content and necessary information to create a Dockerfile and DockerImage from that.
 *
 * @param name the name of the Dockerfile to be created
 * @param tag the id of the DockerImage to be created
 * @param content the commands which should be in the Dockerfile
 */
data class DockerFileContent(
    val name: String,
    val tag: String,
    val content: String
)