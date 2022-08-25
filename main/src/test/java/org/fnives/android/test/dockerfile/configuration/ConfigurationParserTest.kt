package org.fnives.android.test.dockerfile.configuration

import org.fnives.android.test.dockerfile.util.resourceFileAsInputStream
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConfigurationParserTest {

    private lateinit var parser: ConfigurationParser

    @BeforeEach
    fun setUp() {
        parser = ConfigurationParser()
    }

    @Test
    fun example() {
        val expectedConfiguration = Configuration(
            buildTools = setOf("33.0.0", "32.0.0", "31.0.0", "30.0.3", "30.0.2"),
            sdks = setOf(30, 31, 32, 33),
            commandlineTools = "8512546_latest",
            gradleVersion = "7.3.3",
            output = "outputs",
            dockerRepository = "fknives",
            dockerNamespace = "android-test-img",
            dockerTagPrefix = "1.0.0",
            apiLevelVariations = setOf(21, 22, 23)
        )
        val rawConfiguration = resourceFileAsInputStream("example_configuration.toml")

        val actual = parser.invoke(rawConfiguration)

        Assertions.assertEquals(expectedConfiguration, actual)
    }

    @Test
    fun singleConfiguration() {
        val expectedConfiguration = Configuration(
            buildTools = setOf("33.0.0", "32.0.0", "31.0.0"),
            sdks = setOf(32, 33),
            commandlineTools = "8512546_latest",
            gradleVersion = "7.3.3",
            output = "outputs",
            dockerRepository = "fknives",
            dockerNamespace = "android-test-img",
            dockerTagPrefix = "1.0.0",
            apiLevelVariations = emptySet()
        )
        val rawConfiguration = resourceFileAsInputStream("single_configuration.toml")

        val actual = parser.invoke(rawConfiguration)

        Assertions.assertEquals(expectedConfiguration, actual)
    }
}