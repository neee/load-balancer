package ru.serdyuk.load_balancer.strategies

import java.util.concurrent.locks.ReentrantLock

class RoundRobinBalanceStrategy : BalanceStrategy {
    private var value = 0
    private val lock = ReentrantLock()

    override fun get(providersCount: Int): Int {
        lock.lock()
        if (value >= providersCount) {
            value = 0
        }
        val providerNumber = value
        value++
        lock.unlock()

        return providerNumber
    }
}
