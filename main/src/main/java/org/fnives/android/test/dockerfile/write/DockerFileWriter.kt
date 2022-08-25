package org.fnives.android.test.dockerfile.write

import org.fnives.android.test.dockerfile.generator.DockerFileContent
import java.io.File

/**
 * Simple configured class which can write a [DockerFileContent] into a file
 * within the given [outputFolder] configuration, returns [DockerFileDescriptor] describing the written file.
 */
class DockerFileWriter(private val outputFolder: File) {

    fun write(dockerFile: DockerFileContent): DockerFileDescriptor {
        outputFolder.mkdirs()

        val file = File(outputFolder, dockerFile.name)
        file.createNewFile()
        file.writeText(dockerFile.content)

        return DockerFileDescriptor(filePath = file.absolutePath, tag = dockerFile.tag)
    }
}