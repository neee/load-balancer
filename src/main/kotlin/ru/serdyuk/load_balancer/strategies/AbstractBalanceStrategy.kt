package ru.serdyuk.load_balancer.strategies

import ru.serdyuk.load_balancer.exceptions.NoAnyAvailableProvider

abstract class AbstractBalanceStrategy : BalanceStrategy {

    private var providersNumber: Int = 0

    override fun setProvidersNumber(newProvidersNumber: Int) {
        this.providersNumber = newProvidersNumber
    }

    protected fun getProvidersNumber(): Int = providersNumber

    override fun get(): Int {
        validate()

        return getProviderNumber()
    }

    abstract fun getProviderNumber(): Int

    private fun validate() {
        if (providersNumber == 0) {
            throw NoAnyAvailableProvider("Provider's size wasn't set")
        }
    }
}
