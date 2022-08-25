package org.fnives.android.test.dockerfile.generator

/**
 * Configuration for the [DockerFileContentGenerator]
 *
 * @param templateGeneric the Dockerfile template content for a Generic image
 * @param templateSpecific the Dockerfile template content for a Specific image, which extends the Generic and adds additional installs.
 * @param dockerRepository the repository in DockerHub to push into.
 * @param dockerNamespace the namespace inside the [dockerRepository] defined on DockerHub.
 * @param dockerTagPrefix the prefix which should be added to all DockerImage tags when creating [DockerFileContent]
 */
data class DockerFileContentConfig(
    val templateGeneric: String,
    val templateSpecific: String,
    val dockerRepository: String,
    val dockerNamespace: String,
    val dockerTagPrefix: String,
)