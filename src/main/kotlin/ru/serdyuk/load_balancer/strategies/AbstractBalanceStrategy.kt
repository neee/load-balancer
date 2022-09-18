package ru.serdyuk.load_balancer.strategies

import ru.serdyuk.load_balancer.exceptions.NoAnyAvailableProvider
import java.util.concurrent.atomic.AtomicInteger

abstract class AbstractBalanceStrategy : BalanceStrategy {

    private var providersNumber: AtomicInteger = AtomicInteger(0)

    override fun setProvidersNumber(newProvidersNumber: AtomicInteger) {
        this.providersNumber = newProvidersNumber
    }

    protected fun getProvidersNumber(): AtomicInteger = providersNumber

    override fun get(): Int {
        validate()

        return getProviderNumber()
    }

    abstract fun getProviderNumber(): Int

    private fun validate() {
        if (providersNumber.get() == 0) {
            throw NoAnyAvailableProvider("Provider's size wasn't set")
        }
    }
}
