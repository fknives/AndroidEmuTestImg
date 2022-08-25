package org.fnives.android.test.dockerfile.push

import org.fnives.android.test.dockerfile.util.CapturingProcessRunner
import org.fnives.android.test.dockerfile.util.assertCollectionsEquals
import org.fnives.android.test.dockerfile.util.workDirAbsolutePathWithCommands
import org.fnives.android.test.dockerfile.write.DockerFileDescriptor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class DockerBuildAndPushTest {

    private lateinit var sut: DockerBuildAndPush
    private lateinit var capturingProcessRunner: CapturingProcessRunner
    private lateinit var expectedFolder: String

    @BeforeEach
    fun setup() {
        capturingProcessRunner = CapturingProcessRunner(
            defaultReturnValue = true,
            fallbackToDefault = true
        )
        val folderName = "folder"
        expectedFolder = File(folderName).absolutePath
        sut = DockerBuildAndPush(
            config = PushConfig(
                dockerRepository = "repo",
                dockerNamespace = "name",
                output = folderName
            ),
            processRunner = capturingProcessRunner
        )
    }

    @Test
    fun verifyBuildCommand() {
        val expected = listOf(expectedFolder to "docker build -t 123 -f /banan/alma .")

        sut.build(DockerFileDescriptor("/banan/alma", "123"))
        val actual = capturingProcessRunner.workDirAbsolutePathWithCommands

        assertCollectionsEquals(expected, actual)
    }

    @Test
    fun verifyPushCommand() {
        val expected = listOf(expectedFolder to "docker push id")

        sut.push("id")
        val actual = capturingProcessRunner.workDirAbsolutePathWithCommands

        assertCollectionsEquals(expected, actual)
    }

    @Test
    fun verifyCleanCommand() {
        val expected = listOf(expectedFolder to "docker image rm -f id")

        sut.removeLocalImage("id")
        val actual = capturingProcessRunner.workDirAbsolutePathWithCommands

        assertCollectionsEquals(expected, actual)
    }

    @Test
    fun verifyBuildPushClean() {
        val expected = listOf(
            expectedFolder to "docker build -t 123 -f banan/alma .",
            expectedFolder to "docker push 123",
            expectedFolder to "docker image rm -f 123"
        )

        sut.buildPushClean(DockerFileDescriptor("banan/alma", "123"))
        val actual = capturingProcessRunner.workDirAbsolutePathWithCommands

        assertCollectionsEquals(expected, actual)
    }

    @Test
    fun verifyMultipleBuildPushClean() {
        val expected = listOf(
            expectedFolder to "docker build -t 123 -f banan/alma .",
            expectedFolder to "docker push 123",
            expectedFolder to "docker image rm -f 123",
            expectedFolder to "docker build -t fknives/421:512.3 -f banan/alma2 .",
            expectedFolder to "docker push fknives/421:512.3",
            expectedFolder to "docker image rm -f fknives/421:512.3"
        )

        sut.buildPushClean(DockerFileDescriptor("banan/alma", "123"))
        sut.buildPushClean(DockerFileDescriptor("banan/alma2", "fknives/421:512.3"))
        val actual = capturingProcessRunner.workDirAbsolutePathWithCommands

        assertCollectionsEquals(expected, actual)
    }
}