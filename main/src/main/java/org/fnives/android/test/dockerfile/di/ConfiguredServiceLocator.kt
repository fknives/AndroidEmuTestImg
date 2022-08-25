package org.fnives.android.test.dockerfile.di

import org.fnives.android.test.dockerfile.generator.DockerFileContentGenerator
import org.fnives.android.test.dockerfile.push.DockerBuildAndPush
import org.fnives.android.test.dockerfile.write.DockerFileWriter
import org.fnives.android.test.dockerfile.write.ScriptCopier

/**
 * Gives access to the Services that depend on [Configuration][org.fnives.android.test.dockerfile.configuration.Configuration] and takes care of their creation and instances.
 */
interface ConfiguredServiceLocator {
    fun contentGenerator(): DockerFileContentGenerator
    fun imageWriter(): DockerFileWriter
    fun imageBuildAndPush(): DockerBuildAndPush
    fun copyScript(): ScriptCopier
}