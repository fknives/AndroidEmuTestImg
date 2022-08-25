package org.fnives.android.test.dockerfile.write

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class ScriptCopierTest {

    @TempDir
    lateinit var tempDirectory: File

    @Test
    fun copyExample() {
        val sut = ScriptCopier(
            outputFolder = tempDirectory,
            scriptName = "example_to_copy"
        )

        val actual = File(tempDirectory, "example_to_copy")
        sut.invoke()

        val expected = arrayOf("123", "ab", "456")
        Assertions.assertArrayEquals(expected, actual.readLines().toTypedArray())
    }

    @Test
    fun checkActualCopyIsNotEmpty() {
        val sut = ScriptCopier(
            outputFolder = tempDirectory,
            scriptName = "startemulator"
        )

        val actual = File(tempDirectory, "startemulator")
        sut.invoke()

        Assertions.assertTrue(actual.readLines().isNotEmpty()) { "Content is empty when trying to copy startemulator" }
    }

    @Test
    fun createsFolder() {
        val folderToCreate = File(tempDirectory, "childFolder")
        val sut = ScriptCopier(
            outputFolder = folderToCreate,
            scriptName = "example_to_copy"
        )

        sut.invoke()

        Assertions.assertTrue(folderToCreate.exists()) { "Temp folder does not exist!" }
        Assertions.assertFalse(folderToCreate.isFile) { "Temp folder is a file not a Folder!" }
    }
}