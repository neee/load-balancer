package ru.serdyuk.load_balancer.strategies

import java.util.concurrent.locks.ReentrantLock

class RoundRobinBalanceStrategy() : AbstractBalanceStrategy() {
    private var value = 0
    private val lock = ReentrantLock()

    override fun getProviderNumber(): Int {
        lock.lock()
        if (value >= this.getProvidersNumber().get()) {
            value = 0
        }
        val providerNumber = value
        value++
        lock.unlock()

        return providerNumber
    }
}
