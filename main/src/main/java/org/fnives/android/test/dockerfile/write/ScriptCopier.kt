package org.fnives.android.test.dockerfile.write

import org.fnives.android.test.dockerfile.util.resourceFileStream
import java.io.File

class ScriptCopier(private val outputFolder: File, private val scriptName: String) {

    fun invoke() {
        outputFolder.mkdirs()
        val outputScript = File(outputFolder, scriptName)
        outputScript.createNewFile()
        outputScript.outputStream().use {
            resourceFileStream(scriptName).copyTo(it)
        }
    }
}