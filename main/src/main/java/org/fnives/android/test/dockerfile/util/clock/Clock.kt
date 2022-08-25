package org.fnives.android.test.dockerfile.util.clock

/**
 * Simple Utility interface providing the [currentTimeMillis].
 */
interface Clock {

    fun currentTimeMillis(): Long
}