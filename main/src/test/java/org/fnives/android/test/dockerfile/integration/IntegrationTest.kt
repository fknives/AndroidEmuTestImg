package org.fnives.android.test.dockerfile.integration

import org.fnives.android.test.dockerfile.Main
import org.fnives.android.test.dockerfile.di.ServiceLocatorHolder
import org.fnives.android.test.dockerfile.util.CapturingProcessRunner
import org.fnives.android.test.dockerfile.util.FixedClock
import org.fnives.android.test.dockerfile.util.OverrideableClockServiceLocator
import org.fnives.android.test.dockerfile.util.assertCollectionsEquals
import org.fnives.android.test.dockerfile.util.readResourceFile
import org.fnives.android.test.dockerfile.util.workDirAbsolutePathWithCommands
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class IntegrationTest {

    @TempDir
    lateinit var tempDir: File

    private lateinit var capturingProcessRunner: CapturingProcessRunner

    @BeforeEach
    fun setup() {
        capturingProcessRunner = CapturingProcessRunner()
        val mockServiceLocator = OverrideableClockServiceLocator()
        mockServiceLocator.clock = FixedClock(year = 2016, month = 5, dayOfMonth = 28)
        mockServiceLocator.processRunner = capturingProcessRunner

        ServiceLocatorHolder.swap(mockServiceLocator)
    }

    @AfterEach
    fun tearDown() {
        ServiceLocatorHolder.reset()
    }

    @Test
    fun noArgumentsGivesError() {
        val expectedMessage = "Required argument is missing! Required argument is the config.toml file path and just that!"

        val actual = Assertions.assertThrows(IllegalArgumentException::class.java) {
            Main.main(emptyArray())
        }

        Assertions.assertEquals(expectedMessage, actual.message)
    }

    @Test
    fun tooManyArgumentsGivesError() {
        val expectedMessage = "More arguments than expected! Required argument is the config.toml file path and just that!"

        val actual = Assertions.assertThrows(IllegalArgumentException::class.java) {
            Main.main(arrayOf("", ""))
        }

        Assertions.assertEquals(expectedMessage, actual.message)
    }

    @Test
    fun nonExistentConfigurationFileGivesError() {
        val nonExistentFile = File(tempDir, "alma")
        val actual = Assertions.assertThrows(IllegalArgumentException::class.java) {
            Main.main(arrayOf(nonExistentFile.absolutePath))
        }

        Assertions.assertEquals("Can't find file at given path: ${nonExistentFile.absolutePath}", actual.message)
    }

    @Test
    fun cannotParseConfigFile() {
        val file = File(tempDir, "alma")

        file.createNewFile()
        val actual = Assertions.assertThrows(IllegalArgumentException::class.java) {
            Main.main(arrayOf(file.absolutePath))
        }

        Assertions.assertEquals("Unparseable file at given path: ${file.absolutePath}", actual.message)
        Assertions.assertNotNull(actual.cause)
    }

    @Test
    fun singleConfiguration() {
        val file = File(tempDir, "config.toml")
        val outputDir = File(tempDir, "output")
        outputDir.mkdir()
        val updatedConfiguration = readResourceFile(resourceName = "single_configuration.toml")
            .replace("output = \"outputs\"", "output = \"${outputDir.absolutePath}\"")
        file.writeText(updatedConfiguration)

        val expectedDockerCommands = listOf(
            outputDir.absolutePath to "docker build -t fknives/android-test-img:1.0.0 -f ${File(outputDir, "Dockerfile").absolutePath} .",
            outputDir.absolutePath to "docker push fknives/android-test-img:1.0.0",
            outputDir.absolutePath to "docker image rm -f fknives/android-test-img:1.0.0"
        )
        val expectedDockerFileContent = readResourceFile("expected_single_configuration.Dockerfile")
        val expectedScriptContent = readResourceFile("startemulator")

        Main.main(arrayOf(file.absolutePath))
        val actualDockerfileContent = File(outputDir, "Dockerfile").readText()
        val actualScriptFileContent = File(outputDir, "startemulator").readText()

        assertCollectionsEquals(expectedDockerCommands, capturingProcessRunner.workDirAbsolutePathWithCommands)
        Assertions.assertEquals(expectedDockerFileContent, actualDockerfileContent)
        Assertions.assertEquals(expectedScriptContent, actualScriptFileContent)
    }

    @Test
    fun multiApiConfiguration() {
        val file = File(tempDir, "config.toml")
        val outputDir = File(tempDir, "output")
        outputDir.mkdir()
        val updatedConfiguration = readResourceFile(resourceName = "multi_api_configuration.toml")
            .replace("output = \"outputs\"", "output = \"${outputDir.absolutePath}\"")
        file.writeText(updatedConfiguration)

        val expectedDockerCommands = listOf(
            outputDir.absolutePath to "docker build -t fknives2/android-test-img-multi:1.0.1 -f ${File(outputDir, "Dockerfile").absolutePath} .",
            outputDir.absolutePath to "docker push fknives2/android-test-img-multi:1.0.1",
            outputDir.absolutePath to "docker image rm -f fknives2/android-test-img-multi:1.0.1",
            outputDir.absolutePath to "docker build -t fknives2/android-test-img-multi:1.0.1-api-22 -f ${File(outputDir, "Dockerfile-api-22").absolutePath} .",
            outputDir.absolutePath to "docker push fknives2/android-test-img-multi:1.0.1-api-22",
            outputDir.absolutePath to "docker image rm -f fknives2/android-test-img-multi:1.0.1-api-22",
            outputDir.absolutePath to "docker build -t fknives2/android-test-img-multi:1.0.1-api-30 -f ${File(outputDir, "Dockerfile-api-30").absolutePath} .",
            outputDir.absolutePath to "docker push fknives2/android-test-img-multi:1.0.1-api-30",
            outputDir.absolutePath to "docker image rm -f fknives2/android-test-img-multi:1.0.1-api-30"
        )
        val expectedDockerFileContent = readResourceFile("expected_multi_api_configuration.Dockerfile")
        val expectedDocker22FileContent = readResourceFile("expected_multi_api_configuration.22.Dockerfile")
        val expectedDocker30FileContent = readResourceFile("expected_multi_api_configuration.30.Dockerfile")
        val expectedScriptContent = readResourceFile("startemulator")

        Main.main(arrayOf(file.absolutePath))
        val actualDockerfileContent = File(outputDir, "Dockerfile").readText()
        val actualDocker22fileContent = File(outputDir, "Dockerfile-api-22").readText()
        val actualDocker30fileContent = File(outputDir, "Dockerfile-api-30").readText()
        val actualScriptFileContent = File(outputDir, "startemulator").readText()

        assertCollectionsEquals(expectedDockerCommands, capturingProcessRunner.workDirAbsolutePathWithCommands)
        Assertions.assertEquals(expectedDockerFileContent, actualDockerfileContent)
        Assertions.assertEquals(expectedDocker22FileContent, actualDocker22fileContent)
        Assertions.assertEquals(expectedDocker30FileContent, actualDocker30fileContent)
        Assertions.assertEquals(expectedScriptContent, actualScriptFileContent)
    }
}