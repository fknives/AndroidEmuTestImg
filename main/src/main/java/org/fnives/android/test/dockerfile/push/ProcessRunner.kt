package org.fnives.android.test.dockerfile.push

import java.io.File
import java.util.concurrent.TimeoutException

/**
 * Process Runner is able to run a given command in the System process.
 */
interface ProcessRunner {

    /**
     * Runs the given [command] as a System process in [workingDirectory].
     */
    @Throws(TimeoutException::class)
    fun run(workingDirectory: File, command: String)
}