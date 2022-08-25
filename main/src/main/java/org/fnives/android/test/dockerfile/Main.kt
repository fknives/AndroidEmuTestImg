package org.fnives.android.test.dockerfile

import org.fnives.android.test.dockerfile.configuration.Configuration
import org.fnives.android.test.dockerfile.configuration.ImageConfiguration
import org.fnives.android.test.dockerfile.di.ServiceLocatorHolder.ServiceLocator
import java.io.File

/**
 * Step into the Script.
 *
 * This script is intended to:
 * - Generate Dockerfiles from the given configuration.
 * - Create DockerImages from the generated Dockerfiles
 * - Push those images up to DockerHub based on the configucation.
 *
 * Needs a single argument to the path of the configuration.toml file.
 *
 * Usually run like: ./gradlew run --args="$(pwd)/configuration.toml
 *
 * **Docker needs to be installed**
 */
object Main {

    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun main(args: Array<String>) {
        val configuration = getConfigurationFromArgument(args)

        val configuredServiceLocator = ServiceLocator.loadConfiguration(configuration)
        val imageConfigurations = listOf(ImageConfiguration(configuration))
            .plus(configuration.apiLevelVariations.map(::ImageConfiguration))

        configuredServiceLocator.copyScript().invoke()
        imageConfigurations.map(configuredServiceLocator.contentGenerator()::create)
            .map(configuredServiceLocator.imageWriter()::write)
            .forEach(configuredServiceLocator.imageBuildAndPush()::buildPushClean)
    }

    @Throws(IllegalArgumentException::class)
    private fun getConfigurationFromArgument(args: Array<String>): Configuration {
        val configFile = getFileFromArgument(args)

        return try {
            configFile.inputStream().use(ServiceLocator.parser()::invoke)
        } catch (illegalArgumentException: IllegalArgumentException) {
            throw IllegalArgumentException("Unparseable file at given path: ${configFile.absolutePath}", illegalArgumentException)
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun getFileFromArgument(args: Array<String>): File {
        require(args.isNotEmpty()) {
            "Required argument is missing! Required argument is the config.toml file path and just that!"
        }
        require(args.size < 2) {
            "More arguments than expected! Required argument is the config.toml file path and just that!"
        }
        val configFile = File(args[0])
        require(configFile.exists()) {
            "Can't find file at given path: ${configFile.absolutePath}"
        }

        return configFile
    }
}