package org.fnives.android.test.dockerfile.util.clock

/**
 * Actual [Clock] returning the System time.
 */
object SystemCock : Clock {

    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}