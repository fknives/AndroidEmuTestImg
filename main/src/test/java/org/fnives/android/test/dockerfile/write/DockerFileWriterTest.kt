package org.fnives.android.test.dockerfile.write

import org.fnives.android.test.dockerfile.generator.DockerFileContent
import org.fnives.android.test.dockerfile.util.assertCollectionsEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class DockerFileWriterTest {

    @TempDir
    lateinit var tempDirectory: File

    @Test
    fun createsFolder() {
        val folderToCreate = File(tempDirectory, "childFolder")
        val sut = DockerFileWriter(outputFolder = folderToCreate)

        sut.write(DockerFileContent(name = "alma", content = "-", tag = "123"))

        Assertions.assertTrue(folderToCreate.exists()) { "Temp folder does not exist!" }
        Assertions.assertFalse(folderToCreate.isFile) { "Temp folder is a file not a Folder!" }
    }

    @Test
    fun writesFileProperly() {
        val sut = DockerFileWriter(outputFolder = tempDirectory)
        val expected = listOf("123", "abc")

        sut.write(DockerFileContent(name = "alma", content = "123\nabc", tag = "123"))
        val actual = File(tempDirectory, "alma")

        assertCollectionsEquals(expected, actual.readLines())
    }

    @Test
    fun assertReturnsCorrectDescriptor() {
        val sut = DockerFileWriter(outputFolder = tempDirectory)
        val expectedFile = File(tempDirectory, "alma")
        val expected = DockerFileDescriptor(filePath = expectedFile.absolutePath, tag = "123")

        val actual = sut.write(DockerFileContent(name = "alma", content = "123\nabc", tag = "123"))

        Assertions.assertEquals(expected, actual)
    }
}