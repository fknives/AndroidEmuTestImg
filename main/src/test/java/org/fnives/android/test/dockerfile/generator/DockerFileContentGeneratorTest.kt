package org.fnives.android.test.dockerfile.generator

import org.fnives.android.test.dockerfile.configuration.ImageConfiguration
import org.fnives.android.test.dockerfile.util.FixedClock
import org.fnives.android.test.dockerfile.util.readResourceFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DockerFileContentGeneratorTest {

    private lateinit var generator: DockerFileContentGenerator

    @BeforeEach
    fun setup() {
        generator = DockerFileContentGenerator(
            config = DockerFileContentConfig(
                templateGeneric = readResourceFile("Dockerfile.template"),
                templateSpecific = readResourceFile("Dockerfile.api.template"),
                dockerRepository = "fknives",
                dockerTagPrefix = "1.0",
                dockerNamespace = "ati"
            ),
            clock = FixedClock(year = 2021, month = 11, dayOfMonth = 15)
        )
    }

    @Test
    fun generic() {
        val expected = DockerFileContent(
            content = readResourceFile("expected_generic_dockerfile"),
            name = "Dockerfile",
            tag = "fknives/ati:1.0"
        )
        val config = ImageConfiguration.Generic(
            buildTools = setOf("30.0.0", "32.0.0", "31.0.0", "30.0.3", "30.0.2"),
            sdks = setOf(33, 32, 31, 30),
            commandlineTools = "8512546_latest",
            gradleVersion = "7.3.3"
        )

        val actual = generator.create(config)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun api27() {
        val expected = DockerFileContent(
            content = readResourceFile("expected_api_27_dockerfile"),
            name = "Dockerfile-api-27",
            tag = "fknives/ati:1.0-api-27"
        )
        val config = ImageConfiguration.ApiVersion(27)

        val actual = generator.create(config)

        Assertions.assertEquals(expected, actual)
    }
}