package org.fnives.android.test.dockerfile.configuration

import cc.ekblad.toml.TomlMapper
import cc.ekblad.toml.decode
import cc.ekblad.toml.tomlMapper
import java.io.InputStream

/**
 * Parses the given [toml][https://toml.io/en/] file into a [Configuration] to be usable by the other parts of the app.
 */
class ConfigurationParser(private val mapper: TomlMapper = tomlMapper {}) {

    @Throws(IllegalArgumentException::class)
    fun invoke(content: InputStream): Configuration {
        val raw = try {
            mapper.decode<RawConfiguration>(content)
        } catch (cause: Throwable) {
            throw IllegalArgumentException("Couldn't process configuration", cause)
        }
        return raw.convert()
    }

    private fun RawConfiguration.convert(): Configuration = Configuration(
        buildTools = buildTools,
        sdks = sdks,
        commandlineTools = androidCommandlineTools,
        gradleVersion = gradleVersion,
        output = output,
        apiLevelVariations = variations?.apiLevels.orEmpty(),
        dockerNamespace = image.namespace,
        dockerRepository = image.repository,
        dockerTagPrefix = image.tagPrefix
    )

    private data class RawConfiguration(
        val buildTools: Set<String>,
        val sdks: Set<Int>,
        val androidCommandlineTools: String,
        val gradleVersion: String,
        val output: String,
        val variations: Variations?,
        val image: Image
    ) {

        class Variations(
            val apiLevels: Set<Int> = emptySet()
        )

        class Image(
            val namespace: String,
            val repository: String,
            val tagPrefix: String,
        )
    }
}