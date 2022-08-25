package org.fnives.android.test.dockerfile.util

import org.fnives.android.test.dockerfile.configuration.Configuration
import org.fnives.android.test.dockerfile.configuration.ConfigurationParser
import org.fnives.android.test.dockerfile.di.ConfiguredServiceLocator
import org.fnives.android.test.dockerfile.di.ServiceLocator
import org.fnives.android.test.dockerfile.di.ServiceLocatorHolder
import org.fnives.android.test.dockerfile.push.ProcessRunner
import org.fnives.android.test.dockerfile.util.clock.Clock

class OverrideableClockServiceLocator(private val delegate: ServiceLocator = ServiceLocatorHolder.default) : ServiceLocator by delegate {

    var clock: Clock? = null
    var configuredServiceLocator: ConfiguredServiceLocator? = null
    var configurationParser: ConfigurationParser? = null
    var processRunner: ProcessRunner? = null
    var scriptToCopy: String? = null

    override fun clock(): Clock = clock ?: delegate.clock()

    override fun loadConfiguration(configuration: Configuration): ConfiguredServiceLocator =
        configuredServiceLocator ?: delegate.loadConfiguration(configuration)

    override fun parser(): ConfigurationParser =
        configurationParser ?: delegate.parser()

    override fun processRunner(): ProcessRunner =
        processRunner ?: delegate.processRunner()

    override fun scriptToCopy(): String =
        scriptToCopy ?: delegate.scriptToCopy()
}