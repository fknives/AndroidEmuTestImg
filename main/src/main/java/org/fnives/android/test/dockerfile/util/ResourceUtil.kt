package org.fnives.android.test.dockerfile.util

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Helper function to read contents of files baked into the resources folder
 */
internal fun Any.resourceFileContent(filePath: String): String = try {
    BufferedReader(InputStreamReader(resourceFileStream(filePath)))
        .readLines().joinToString("\n")
} catch (nullPointerException: NullPointerException) {
    throw IllegalArgumentException("$filePath file not found!", nullPointerException)
}

/**
 * Helper function to access contents of files baked into the resources folder
 */
internal fun Any.resourceFileStream(filePath: String): InputStream = try {
    this.javaClass.classLoader.getResourceAsStream(filePath)!!
} catch (nullPointerException: NullPointerException) {
    throw IllegalArgumentException("$filePath file not found!", nullPointerException)
}