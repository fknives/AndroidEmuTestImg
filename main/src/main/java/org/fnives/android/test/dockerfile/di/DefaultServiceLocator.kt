package org.fnives.android.test.dockerfile.di

import org.fnives.android.test.dockerfile.configuration.Configuration
import org.fnives.android.test.dockerfile.configuration.ConfigurationParser
import org.fnives.android.test.dockerfile.push.ProcessRunner
import org.fnives.android.test.dockerfile.push.SystemProcessRunner
import org.fnives.android.test.dockerfile.util.clock.Clock
import org.fnives.android.test.dockerfile.util.clock.SystemCock

/**
 * Actual [ServiceLocator] keeping references and creating the Services.
 */
object DefaultServiceLocator : ServiceLocator {

    private val parser by lazy { ConfigurationParser() }
    private val processRunner by lazy { SystemProcessRunner() }

    override fun loadConfiguration(configuration: Configuration): ConfiguredServiceLocator =
        ConfiguredServiceLocator(configuration)

    override fun parser(): ConfigurationParser = parser

    override fun clock(): Clock = SystemCock

    override fun scriptToCopy(): String = "startemulator"

    override fun processRunner(): ProcessRunner = processRunner
}