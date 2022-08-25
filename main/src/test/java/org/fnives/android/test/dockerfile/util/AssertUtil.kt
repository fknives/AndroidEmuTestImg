package org.fnives.android.test.dockerfile.util

import org.junit.jupiter.api.Assertions

inline fun <reified T> assertCollectionsEquals(expected: Collection<T>, actual: Collection<T>) {
    Assertions.assertArrayEquals(expected.toTypedArray(), actual.toTypedArray())
}