package org.fnives.android.test.dockerfile.util

import org.fnives.android.test.dockerfile.util.clock.Clock
import java.util.Calendar

class FixedClock(val timeMillis: Long) : Clock {

    override fun currentTimeMillis(): Long = timeMillis
}

fun FixedClock(year: Int, month: Int, dayOfMonth: Int): Clock {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1 + Calendar.JANUARY)
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

    return FixedClock(calendar.timeInMillis)
}