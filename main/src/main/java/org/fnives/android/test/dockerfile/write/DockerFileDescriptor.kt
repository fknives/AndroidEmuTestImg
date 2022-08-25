package org.fnives.android.test.dockerfile.write

/**
 * Describes [DockerFileContent][org.fnives.android.test.dockerfile.generator.DockerFileContent] which was written into a file.
 * @param filePath is the absolut path of the File
 * @param tag is the id of the DockerImage.
 */
data class DockerFileDescriptor(val filePath: String, val tag: String)