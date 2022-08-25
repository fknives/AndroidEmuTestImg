package org.fnives.android.test.dockerfile.util

import org.fnives.android.test.dockerfile.push.ProcessRunner
import java.io.File

class CapturingProcessRunner : ProcessRunner {

    private val _receivedCommands = mutableListOf<Pair<File, String>>()
    val receivedCommands get() = _receivedCommands.toList()

    override fun run(workingDirectory: File, command: String) {
        _receivedCommands.add(workingDirectory to command)
    }
}

val CapturingProcessRunner.workDirAbsolutePathWithCommands: List<Pair<String, String>>
    get() = receivedCommands.map { (workdir, command) -> workdir.absolutePath to command }