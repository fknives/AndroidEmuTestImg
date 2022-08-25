package org.fnives.android.test.dockerfile.di

/**
 * Access point to [ServiceLocator][org.fnives.android.test.dockerfile.di.ServiceLocator] so it can be [swap]ed out in tests.
 */
object ServiceLocatorHolder {

    /**
     * Syntax Sugar
     */
    val ServiceLocator get() = get()

    val default: ServiceLocator = DefaultServiceLocator
    private var actual: ServiceLocator = default

    /**
     * Change the [ServiceLocator][org.fnives.android.test.dockerfile.di.ServiceLocator] returned in [get] and used.
     */
    fun swap(serviceLocator: ServiceLocator) {
        actual = serviceLocator
    }

    /**
     * Reset back to [default] [ServiceLocator][org.fnives.android.test.dockerfile.di.ServiceLocator] to be returned in [get]
     */
    fun reset() = swap(default)

    /**
     * Returns the actual [ServiceLocator][org.fnives.android.test.dockerfile.di.ServiceLocator]
     */
    fun get() = actual
}