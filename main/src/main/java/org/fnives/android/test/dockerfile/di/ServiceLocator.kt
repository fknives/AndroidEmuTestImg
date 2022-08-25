package org.fnives.android.test.dockerfile.di

import org.fnives.android.test.dockerfile.configuration.Configuration
import org.fnives.android.test.dockerfile.configuration.ConfigurationParser
import org.fnives.android.test.dockerfile.push.ProcessRunner
import org.fnives.android.test.dockerfile.util.clock.Clock

/**
 * Gives access to the Services and takes care of their creation and instances.
 */
interface ServiceLocator {
    fun parser(): ConfigurationParser
    fun clock(): Clock
    fun scriptToCopy(): String
    fun processRunner(): ProcessRunner
    fun loadConfiguration(configuration: Configuration): ConfiguredServiceLocator
}