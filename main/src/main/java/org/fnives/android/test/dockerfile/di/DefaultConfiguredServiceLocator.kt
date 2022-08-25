package org.fnives.android.test.dockerfile.di

import org.fnives.android.test.dockerfile.configuration.Configuration
import org.fnives.android.test.dockerfile.generator.DockerFileContentConfig
import org.fnives.android.test.dockerfile.generator.DockerFileContentGenerator
import org.fnives.android.test.dockerfile.push.DockerBuildAndPush
import org.fnives.android.test.dockerfile.push.PushConfig
import org.fnives.android.test.dockerfile.util.resourceFileContent
import org.fnives.android.test.dockerfile.util.clock.Clock
import org.fnives.android.test.dockerfile.write.DockerFileWriter
import org.fnives.android.test.dockerfile.write.ScriptCopier
import java.io.File

/** Syntax Sugar */
fun ConfiguredServiceLocator(configuration: Configuration): ConfiguredServiceLocator =
    DefaultConfiguredServiceLocator(configuration)

/**
 * Actual [ConfiguredServiceLocator]  keeping references and creating the Services that are dependent on [Configuration].
 */
class DefaultConfiguredServiceLocator(private val configuration: Configuration) : ConfiguredServiceLocator {
    private val pushConfig
        get() = PushConfig(
            dockerRepository = configuration.dockerRepository,
            dockerNamespace = configuration.dockerNamespace,
            output = configuration.output
        )
    private val outputFolder get() = File(configuration.output)

    private val processRunner get() = ServiceLocatorHolder.ServiceLocator.processRunner()
    private val clock: Clock get() = ServiceLocatorHolder.ServiceLocator.clock()
    private val scriptName: String get() = ServiceLocatorHolder.ServiceLocator.scriptToCopy()

    private val imageBuildAndPush by lazy { DockerBuildAndPush(config = pushConfig, processRunner = processRunner) }
    private val imageWriter by lazy { DockerFileWriter(outputFolder = outputFolder) }
    private val copyScript by lazy { ScriptCopier(outputFolder = outputFolder, scriptName = scriptName) }

    private val dockerFileContentConfig
        get() = DockerFileContentConfig(
            templateGeneric = resourceFileContent("Dockerfile.template"),
            templateSpecific = resourceFileContent("Dockerfile.api.template"),
            dockerRepository = configuration.dockerRepository,
            dockerNamespace = configuration.dockerNamespace,
            dockerTagPrefix = configuration.dockerTagPrefix,
        )

    private val contentGenerator by lazy {
        DockerFileContentGenerator(
            config = dockerFileContentConfig,
            clock = clock
        )
    }

    override fun contentGenerator(): DockerFileContentGenerator = contentGenerator

    override fun imageWriter(): DockerFileWriter = imageWriter

    override fun imageBuildAndPush(): DockerBuildAndPush = imageBuildAndPush

    override fun copyScript(): ScriptCopier = copyScript
}