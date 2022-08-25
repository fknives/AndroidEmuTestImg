package org.fnives.android.test.dockerfile.util

import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

internal fun Any.readResourceFile(resourceName: String): String = try {
    BufferedReader(InputStreamReader(resourceFileAsInputStream(resourceName)))
        .readLines().joinToString("\n")
} catch (nullPointerException: NullPointerException) {
    throw IllegalArgumentException("$resourceName file not found!", nullPointerException)
}

internal fun Any.resourceFileAsInputStream(resourceName: String): InputStream = try {
    this.javaClass.classLoader.getResourceAsStream(resourceName)!!
} catch (nullPointerException: NullPointerException) {
    throw IllegalArgumentException("$resourceName file not found!", nullPointerException)
}