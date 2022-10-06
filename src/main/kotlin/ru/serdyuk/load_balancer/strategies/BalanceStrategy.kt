package ru.serdyuk.load_balancer.strategies

interface BalanceStrategy {
    fun get(providersCount: Int): Int
}
