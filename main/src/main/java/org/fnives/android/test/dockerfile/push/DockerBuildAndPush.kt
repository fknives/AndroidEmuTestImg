package org.fnives.android.test.dockerfile.push

import org.fnives.android.test.dockerfile.write.DockerFileDescriptor
import java.io.File

/**
 * Runs the necessary docker commands to create, push and cleanup the DockerImage from the given Dockerfile.
 */
class DockerBuildAndPush(
    private val config: PushConfig,
    private val processRunner: ProcessRunner
) {

    private val workDir get() = File(config.output)

    fun build(dockerFileDescriptor: DockerFileDescriptor): String {
        "docker build -t ${dockerFileDescriptor.tag} -f ${dockerFileDescriptor.filePath} .".runCommand(workDir)

        return dockerFileDescriptor.tag
    }

    fun push(id: String) {
        "docker push $id".runCommand(workDir)
    }

    fun removeLocalImage(id: String) {
        "docker image rm -f $id".runCommand(workDir)
    }

    fun buildPushClean(dockerFileDescriptor: DockerFileDescriptor) {
        val id = build(dockerFileDescriptor)
        push(id)
        removeLocalImage(id)
    }

    private fun String.runCommand(workingDir: File) =
        processRunner.run(workingDirectory = workingDir, command = this)
}