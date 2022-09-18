package ru.serdyuk.load_balancer.strategies

interface BalanceStrategy {
    fun get(): Int
    fun setProvidersNumber(newProvidersNumber: Int)
}
