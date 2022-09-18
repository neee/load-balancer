package ru.serdyuk.load_balancer.strategies

import java.util.concurrent.atomic.AtomicInteger

interface BalanceStrategy {
    fun get(): Int

    fun setProvidersNumber(newProvidersNumber: AtomicInteger)
}
