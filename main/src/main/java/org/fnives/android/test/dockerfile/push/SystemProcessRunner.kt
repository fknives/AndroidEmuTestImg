package org.fnives.android.test.dockerfile.push

import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Actual [ProcessRunner] which converts the command into the Process then starts and awaits it.
 */
class SystemProcessRunner(
    private val timeout: Long = 60,
    private val timeoutUnit: TimeUnit = TimeUnit.MINUTES
) : ProcessRunner {

    override fun run(workingDirectory: File, command: String) {
        val result = ProcessBuilder(*command.split(" ").toTypedArray())
            .directory(workingDirectory)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor(timeout, timeoutUnit)
        if (!result) {
            throw TimeoutException("The command have not finished in $timeout ${timeoutUnit}! `$command`")
        }
    }
}